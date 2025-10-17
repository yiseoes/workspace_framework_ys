package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;

@Controller
@RequestMapping("/purchase") // ★ 07 스타일: 베이스 경로 통일
public class PurchaseController {

    @Autowired
    @Qualifier("purchaseServiceImpl")
    private PurchaseService purchaseService;

    @Autowired
    @Qualifier("productServiceImpl")
    private ProductService productService;

    @Value("#{commonProperties['pageUnit'] ?: 5}")
    private int pageUnit;

    @Value("#{commonProperties['pageSize'] ?: 5}")
    private int pageSize;

    public PurchaseController() {
        System.out.println("[CTRL] PurchaseController 생성 (07)");
    }

    /** 1) 구매 폼 진입 : GET /purchase/addPurchaseView?prodNo=... */
    @GetMapping("addPurchaseView")
    public String addPurchaseView(@RequestParam int prodNo, HttpSession session, Model model) throws Exception {
        model.addAttribute("product", productService.getProduct(prodNo));
        Object login = session.getAttribute("user") != null ? session.getAttribute("user")
                                                            : session.getAttribute("userVO");
        model.addAttribute("user", login);
        return "/purchase/addPurchaseView.jsp";
    }

    /** 2) 구매 등록 : POST /purchase/addPurchase → 등록 후 상세로 리다이렉트 */
    @PostMapping("addPurchase")
    public String addPurchase(
            @RequestParam int prodNo,
            @RequestParam String paymentOption,
            @RequestParam String receiverName,
            @RequestParam String receiverPhone,
            @RequestParam String dlvyAddr,
            @RequestParam(required=false) String dlvyRequest,
            @RequestParam(required=false) String dlvyDate,
            HttpSession session
    ) throws Exception {

        User buyer = (User)( session.getAttribute("user") != null ? session.getAttribute("user")
                                                                  : session.getAttribute("userVO") );

        Purchase vo = new Purchase();
        vo.setBuyer(buyer);
        Product p = new Product();
        p.setProdNo(prodNo);
        vo.setPurchaseProd(p);

        vo.setPaymentOption(paymentOption);
        vo.setReceiverName(receiverName);
        vo.setReceiverPhone(receiverPhone);
        vo.setDlvyAddr(dlvyAddr);
        vo.setDlvyRequest(dlvyRequest);
        vo.setDlvyDate(dlvyDate != null ? dlvyDate.replaceAll("-", "") : null);
        vo.setTranCode("1"); // 기본 배송상태

        // INSERT (selectKey로 tranNo 셋)
        purchaseService.addPurchase(vo);

        // 상품 상태 SOLD 처리(정책 코드에 맞게)
        try {
            productService.updateProTranCode(prodNo, "SOLD");
        } catch (Exception e) {
            System.out.println("[CTRL] updateProTranCode 실패 : " + e.getMessage());
        }

        return "redirect:/purchase/getPurchase?tranNo=" + vo.getTranNo();
    }

    /** 3) 구매 상세 : GET /purchase/getPurchase?tranNo=... */
    @GetMapping("getPurchase")
    public String getPurchase(@RequestParam int tranNo, Model model) throws Exception {
        model.addAttribute("purchase", purchaseService.getPurchase(tranNo));
        return "/purchase/getPurchase.jsp";
    }

    /** 4) 내 구매 목록 : GET /purchase/listPurchase */
    @GetMapping("listPurchase")
    public String listPurchase(@ModelAttribute("search") Search search, HttpSession session, Model model) throws Exception {

        if (search == null) search = new Search();
        if (search.getCurrentPage() == 0) search.setCurrentPage(1);
        if (search.getPageSize() == 0) search.setPageSize(pageSize);

        User login = (User)( session.getAttribute("user") != null ? session.getAttribute("user")
                                                                  : session.getAttribute("userVO") );
        String buyerId = (login != null ? login.getUserId() : null);

        Map<String, Object> map = purchaseService.getPurchaseList(search, buyerId);
        int totalCount = (map.get("totalCount") instanceof Integer) ? (Integer)map.get("totalCount") : 0;

        Page resultPage = new Page(search.getCurrentPage(), totalCount, pageUnit, search.getPageSize());
        model.addAttribute("list", map.get("list"));
        model.addAttribute("resultPage", resultPage);
        model.addAttribute("currentPage", search.getCurrentPage());
        model.addAttribute("pageSize", search.getPageSize());
        model.addAttribute("startPage", resultPage.getBeginUnitPage());
        model.addAttribute("endPage", resultPage.getEndUnitPage());
        model.addAttribute("totalCount", totalCount);

        return "/purchase/listPurchase.jsp";
    }

    @RequestMapping(value="updatePurchase", method=RequestMethod.GET)
    public String updatePurchaseView(@RequestParam("tranNo") int tranNo, Model model) throws Exception {
        model.addAttribute("purchase", purchaseService.getPurchase(tranNo));
        model.addAttribute("tranNo", tranNo);
        return "/purchase/updatePurchaseView.jsp";
    }

    // 수정 처리 (POST)
    @RequestMapping(value="updatePurchase", method=RequestMethod.POST)
    public String updatePurchase(
            @RequestParam("tranNo") int tranNo,
            @RequestParam(value="paymentOption", required=false) String paymentOption,
            @RequestParam(value="receiverName",  required=false) String receiverName,
            @RequestParam(value="receiverPhone", required=false) String receiverPhone,
            @RequestParam(value="dlvyAddr",     required=false) String dlvyAddr,
            @RequestParam(value="dlvyRequest",  required=false) String dlvyRequest,
            @RequestParam(value="dlvyDate",     required=false) String dlvyDate
    ) throws Exception {
        Purchase vo = new Purchase();
        vo.setTranNo(tranNo);
        if (paymentOption != null) vo.setPaymentOption(paymentOption);
        if (receiverName  != null) vo.setReceiverName(receiverName);
        if (receiverPhone != null) vo.setReceiverPhone(receiverPhone);
        if (dlvyAddr      != null) vo.setDlvyAddr(dlvyAddr);
        if (dlvyRequest   != null) vo.setDlvyRequest(dlvyRequest);
        if (dlvyDate      != null) vo.setDlvyDate(dlvyDate.replaceAll("-", ""));
        purchaseService.updatePurchase(vo);
        return "redirect:/purchase/getPurchase?tranNo=" + tranNo;
    }


    /** 7) 배송상태코드 변경(단건) : GET /purchase/updateTranCode?tranNo=&tranCode= */
    @GetMapping("updateTranCode")
    public String updateTranCode(@RequestParam int tranNo, @RequestParam String tranCode) throws Exception {
        purchaseService.updateTranCode(tranNo, tranCode);
        return "redirect:/purchase/getPurchase?tranNo=" + tranNo;
    }

    /** 8) 상품 기준 배송상태 일괄 변경 : GET /purchase/updateTranCodeByProd?prodNo=&tranCode= */
    @GetMapping("updateTranCodeByProd")
    public String updateTranCodeByProd(
            @RequestParam int prodNo,
            @RequestParam String tranCode,
            HttpServletRequest req
    ) throws Exception {

        // 1) 배송상태 업데이트
        purchaseService.updateTranCodeByProd(prodNo, tranCode);

        // 2) 들어온 쿼리스트링을 그대로 회수해서 목록으로 돌려보내기
        //    (검색조건/키워드/페이지 등 유지) 단, prodNo/tranCode 는 목록에 불필요하니 제거
        String qs = req.getQueryString(); // 예: prodNo=1001&tranCode=2&menu=manage&currentPage=3&searchCondition=0&searchKeyword=%EC%8B%A0%EB%B0%9C

        if (qs != null) {
            // prodNo, tranCode 제거 (앞/중간/뒤 모든 위치)
            qs = qs.replaceAll("(^|&)(prodNo|tranCode)=[^&]*", "")
                   .replaceAll("^&+", "")
                   .replaceAll("&+$", "")
                   .replaceAll("&&+", "&");
        }

        // 3) 목록으로 리다이렉션 (쿼리스트링이 남아있으면 그대로 부착)
        return "redirect:/product/listProduct" + (qs != null && !qs.isEmpty() ? "?" + qs : "");
    }

}
