package com.model2.mvc.web.purchase;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;

/**
 * PurchaseRestController (08 버전용)
 * - JSON 기반 퍼처스 API
 * - 기존 Service/VO/매퍼 시그니처 그대로 호출
 * - 세션 사용자(user / userVO) 자동 판단
 * - [중요] 프로덕트 상태 연계 : addPurchase 성공 후 SOLD 반영(기존 방식 유지)
 *
 * URL 규칙
 *  - /purchase/json/addPurchase            : POST  (구매 생성)
 *  - /purchase/json/getPurchase/{tranNo}   : GET   (구매 단건 조회)
 *  - /purchase/json/getPurchaseList        : POST  (내 구매 목록)
 *  - /purchase/json/updatePurchase         : POST  (구매 수정)
 *  - /purchase/json/updateTranCode         : POST  (단건 코드 변경)
 *  - /purchase/json/updateTranCodeByProd   : POST  (상품 기준 일괄 코드 변경)
 *
 * 응답은 가벼운 Map/boolean/VO 로 통일 (프론트 연동 간단)
 */
@RestController
@RequestMapping("/purchase/*")
public class PurchaseRestController {

    @Autowired
    @Qualifier("purchaseServiceImpl")
    private PurchaseService purchaseService;

    @Autowired
    @Qualifier("productServiceImpl")
    private ProductService productService;

    public PurchaseRestController() {
        System.out.println("==> [REST] PurchaseRestController 생성됨");
    }

    // ─────────────────────────────────────────────────────────────
    // 공통 : 세션 로그인 사용자 조회(user / userVO 혼재 대응)
    // ─────────────────────────────────────────────────────────────
    private User resolveLoginUser(HttpSession session) {
        if (session == null) return null;
        Object obj = (session.getAttribute("user") != null)
                   ? session.getAttribute("user")
                   : session.getAttribute("userVO");
        if (obj instanceof User) {
            return (User)obj;
        }
        return null;
    }

    // ─────────────────────────────────────────────────────────────
    // 1) 구매 생성 : POST /purchase/json/addPurchase
    //    - 바디로 Purchase 일부 필드 수신 (prodNo 등)
    //    - buyerId는 세션에서 자동 주입 (없으면 401 스타일 메시지)
    //    - 성공 후 product.proTranCode = "SOLD" (기존 연동 유지)
    // ─────────────────────────────────────────────────────────────
    @PostMapping(
        value   = "json/addPurchase",
        consumes= MediaType.APPLICATION_JSON_VALUE,
        produces= MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String,Object> addPurchase(@RequestBody Map<String,Object> body, HttpSession session) throws Exception {
        Map<String,Object> res = new HashMap<String,Object>();
        System.out.println("[REST][구매등록] /purchase/json/addPurchase 요청 수신 : " + body);

        User buyer = resolveLoginUser(session);
        if (buyer == null) {
            res.put("success", false);
            res.put("message", "로그인이 필요합니다.");
            return res;
        }

        // 필수값 : prodNo
        Object prodNoObj = body.get("prodNo");
        if (prodNoObj == null) {
            res.put("success", false);
            res.put("message", "필수값 누락 : prodNo");
            return res;
        }
        int prodNo = Integer.parseInt(String.valueOf(prodNoObj));

        // Purchase VO 구성
        Purchase vo = new Purchase();
        vo.setBuyer(buyer);

        Product p = new Product();
        p.setProdNo(prodNo);
        vo.setPurchaseProd(p);

        // 선택/일반 필드들 매핑
        Object paymentOption = body.get("paymentOption");
        Object receiverName  = body.get("receiverName");
        Object receiverPhone = body.get("receiverPhone");
        Object dlvyAddr      = body.get("dlvyAddr");
        Object dlvyRequest   = body.get("dlvyRequest");
        Object dlvyDate      = body.get("dlvyDate"); // "YYYYMMDD" 또는 "YYYY-MM-DD"

        if (paymentOption != null) vo.setPaymentOption(String.valueOf(paymentOption));
        if (receiverName  != null) vo.setReceiverName(String.valueOf(receiverName));
        if (receiverPhone != null) vo.setReceiverPhone(String.valueOf(receiverPhone));
        if (dlvyAddr      != null) vo.setDlvyAddr(String.valueOf(dlvyAddr));
        if (dlvyRequest   != null) vo.setDlvyRequest(String.valueOf(dlvyRequest));
        if (dlvyDate      != null) {
            String raw = String.valueOf(dlvyDate);
            vo.setDlvyDate(raw != null ? raw.replaceAll("-", "") : null);
        }

        // 기본 배송상태 코드 : "1"(상품준비중) → 매퍼 INSERT에서 '1'로 넣고 있어도 VO 일관성 위해 보정
        vo.setTranCode("1");

        // INSERT (selectKey 로 tranNo 셋팅)
        purchaseService.addPurchase(vo);

        // 상품 상태 SOLD 처리 (프로덕트 모듈은 손대지 않기로 했으니 기존 방식 유지)
        try {
            productService.updateProTranCode(prodNo, "SOLD");
        } catch (Exception e) {
            System.out.println("[REST][WARN] updateProTranCode 실패(무시하고 진행) : " + e.getMessage());
        }

        res.put("success", true);
        res.put("tranNo", vo.getTranNo());
        return res;
    }

    // ─────────────────────────────────────────────────────────────
    // 2) 구매 단건 조회 : GET /purchase/json/getPurchase/{tranNo}
    // ─────────────────────────────────────────────────────────────
    @GetMapping(
        value   = "json/getPurchase/{tranNo}",
        produces= MediaType.APPLICATION_JSON_VALUE
    )
    public Purchase getPurchase(@PathVariable int tranNo) throws Exception {
        System.out.println("[REST][상세] tranNo=" + tranNo);
        return purchaseService.getPurchase(tranNo);
    }

    // ─────────────────────────────────────────────────────────────
    // 3) 내 구매 목록 : POST /purchase/json/getPurchaseList
    //    - body : { currentPage, pageSize, ... }
    //    - buyerId 는 세션에서 가져옴
    // ─────────────────────────────────────────────────────────────
    @PostMapping(
        value   = "json/getPurchaseList",
        consumes= MediaType.APPLICATION_JSON_VALUE,
        produces= MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String,Object> getPurchaseList(@RequestBody Search search, HttpSession session) throws Exception {
        Map<String,Object> res = new HashMap<String,Object>();
        User login = resolveLoginUser(session);
        if (login == null) {
            res.put("success", false);
            res.put("message", "로그인이 필요합니다.");
            return res;
        }

        if (search == null) {
            search = new Search();
        }
        if (search.getCurrentPage() == 0) search.setCurrentPage(1);
        if (search.getPageSize() == 0)   search.setPageSize(5);

        String buyerId = login.getUserId();
        Map<String,Object> data = purchaseService.getPurchaseList(search, buyerId);

        res.put("success", true);
        res.putAll(data); // {list, totalCount}
        res.put("currentPage", search.getCurrentPage());
        res.put("pageSize", search.getPageSize());
        return res;
    }

    // ─────────────────────────────────────────────────────────────
    // 4) 구매 수정 : POST /purchase/json/updatePurchase
    //    - 바디로 들어온 필드만 보정하여 전달(VO null 허용)
    // ─────────────────────────────────────────────────────────────
    @PostMapping(
        value   = "json/updatePurchase",
        consumes= MediaType.APPLICATION_JSON_VALUE,
        produces= MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String,Object> updatePurchase(@RequestBody Map<String,Object> body) throws Exception {
        Map<String,Object> res = new HashMap<String,Object>();
        System.out.println("[REST][수정] updatePurchase : " + body);

        Object tranNoObj = body.get("tranNo");
        if (tranNoObj == null) {
            res.put("success", false);
            res.put("message", "필수값 누락 : tranNo");
            return res;
        }
        int tranNo = Integer.parseInt(String.valueOf(tranNoObj));

        Purchase vo = new Purchase();
        vo.setTranNo(tranNo);

        if (body.containsKey("paymentOption") && body.get("paymentOption") != null) {
            vo.setPaymentOption(String.valueOf(body.get("paymentOption")));
        }
        if (body.containsKey("receiverName") && body.get("receiverName") != null) {
            vo.setReceiverName(String.valueOf(body.get("receiverName")));
        }
        if (body.containsKey("receiverPhone") && body.get("receiverPhone") != null) {
            vo.setReceiverPhone(String.valueOf(body.get("receiverPhone")));
        }
        if (body.containsKey("dlvyAddr") && body.get("dlvyAddr") != null) {
            vo.setDlvyAddr(String.valueOf(body.get("dlvyAddr")));
        }
        if (body.containsKey("dlvyRequest")) {
            Object v = body.get("dlvyRequest");
            vo.setDlvyRequest(v == null ? null : String.valueOf(v));
        }
        if (body.containsKey("dlvyDate") && body.get("dlvyDate") != null) {
            String raw = String.valueOf(body.get("dlvyDate"));
            vo.setDlvyDate(raw.replaceAll("-", ""));
        }

        purchaseService.updatePurchase(vo);

        res.put("success", true);
        res.put("tranNo", tranNo);
        return res;
    }

    // ─────────────────────────────────────────────────────────────
    // 5) 배송상태코드 변경(단건) : POST /purchase/json/updateTranCode
    //    - param : tranNo, tranCode
    // ─────────────────────────────────────────────────────────────
    @PostMapping(
        value   = "json/updateTranCode",
        produces= MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String,Object> updateTranCode(@RequestParam("tranNo") int tranNo,
                                             @RequestParam("tranCode") String tranCode) throws Exception {
        Map<String,Object> res = new HashMap<String,Object>();
        purchaseService.updateTranCode(tranNo, tranCode);
        res.put("success", true);
        res.put("tranNo", tranNo);
        res.put("tranCode", tranCode);
        return res;
    }

    // ─────────────────────────────────────────────────────────────
    // 6) 상품 기준 배송상태 일괄 변경 : POST /purchase/json/updateTranCodeByProd
    //    - param : prodNo, tranCode
    // ─────────────────────────────────────────────────────────────
    @PostMapping(
        value   = "json/updateTranCodeByProd",
        produces= MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String,Object> updateTranCodeByProd(@RequestParam("prodNo") int prodNo,
                                                   @RequestParam("tranCode") String tranCode) throws Exception {
        Map<String,Object> res = new HashMap<String,Object>();
        purchaseService.updateTranCodeByProd(prodNo, tranCode);
        res.put("success", true);
        res.put("prodNo", prodNo);
        res.put("tranCode", tranCode);
        return res;
    }
}
