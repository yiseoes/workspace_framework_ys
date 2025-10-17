package com.model2.mvc.web.purchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 06 규칙
 *  - ViewResolver prefix/suffix 미사용 → 풀경로 JSP 반환
 *  - URL 패턴 *.do
 *  - 네비게이션은 컨트롤러에서만 결정
 */
@Controller
public class PurchaseController {

    @Autowired
    @Qualifier("purchaseService")
    private PurchaseService purchaseService;

    // ProductService는 타입 주입으로 처리
    @Autowired
    private ProductService productService;

    // 구매 등록 화면
    @RequestMapping("/addPurchaseView.do")
    public String addPurchaseView(@RequestParam("prodNo") int prodNo,
                                  HttpSession session,
                                  Model model) throws Exception {

        // 로그인 체크
        User loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "/user/loginView.jsp";
        }

        // 상품 조회
        Product product = productService.getProduct(prodNo);
        if (product == null) {
            // 상품이 없으면 목록으로 네비
            return "redirect:/listProduct.do";
        }

        // 화면 표시용 데이터
        model.addAttribute("product", product);
        model.addAttribute("user", loginUser);

        // 구매작성 화면으로 네비게이션
        return "/purchase/addPurchaseView.jsp";
    }

    // 구매 등록 처리
    @RequestMapping("/addPurchase.do")
    public String addPurchase(Purchase purchase,
                              @RequestParam("prodNo") int prodNo,
                              HttpSession session,
                              Model model) throws Exception {

        // 로그인 체크
        User loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "/user/loginView.jsp";
        }

        // 상품/구매자 바인딩
        Product product = productService.getProduct(prodNo);
        if (product == null) {
            return "redirect:/listProduct.do";
        }
        purchase.setBuyer(loginUser);
        purchase.setPurchaseProd(product);

        // 등록
        purchaseService.addPurchase(purchase);

        // tranNo 확보(selectKey로 세팅되지만 방어로직 유지)
        Integer tranNo = purchase.getTranNo();
        if (tranNo == null || tranNo.intValue() == 0) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("buyerId", loginUser.getUserId());
            param.put("startRowNum", 1);
            param.put("endRowNum", 1);
            List<Purchase> lastOne = purchaseService.findPurchaseList(param);
            if (lastOne != null && !lastOne.isEmpty()) {
                tranNo = lastOne.get(0).getTranNo();
            }
        }

        // 네비게이션 결정
        if (tranNo == null || tranNo.intValue() == 0) {
            // 예외 상황: 상세 키를 못 찾으면 목록으로
            return "redirect:/listPurchase.do";
        }

        // 정상: 구매 상세로 네비
        return "redirect:/getPurchase.do?tranNo=" + tranNo;
    }

    // 구매 단건 조회
    @RequestMapping("/getPurchase.do")
    public String getPurchase(@RequestParam("tranNo") int tranNo,
                              HttpSession session,
                              Model model) throws Exception {

        // 로그인 체크
        User loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "/user/loginView.jsp";
        }

        // 데이터 조회
        Purchase result = purchaseService.findPurchase(tranNo);
        if (result == null) {
            return "redirect:/listPurchase.do";
        }

        model.addAttribute("purchase", result);

        // 상세 화면으로 네비
        return "/purchase/getPurchase.jsp";
    }

    // 구매 목록
    @RequestMapping("/listPurchase.do")
    public String listPurchase(@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                               @RequestParam(value = "pageSize",    required = false, defaultValue = "3") int pageSize,
                               HttpSession session,
                               Model model) throws Exception {

        // 로그인 체크
        User loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "/user/loginView.jsp";
        }

        // 페이징 계산(ROWNUM 기준)
        int startRowNum = ((currentPage - 1) * pageSize) + 1;
        int endRowNum   = currentPage * pageSize;

        // 조회
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("buyerId", loginUser.getUserId());
        param.put("startRowNum", startRowNum);
        param.put("endRowNum", endRowNum);

        List<Purchase> list = purchaseService.findPurchaseList(param);
        int totalCount = purchaseService.getTotalCount(loginUser.getUserId());

        // 모델 바인딩
        model.addAttribute("list", list);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);

        // 목록 화면으로 네비
        return "/purchase/listPurchase.jsp";
    }

    // 구매 수정 화면
    @RequestMapping("/updatePurchaseView.do")
    public String updatePurchaseView(@RequestParam("tranNo") int tranNo,
                                     HttpSession session,
                                     Model model) throws Exception {

        // 로그인 체크
        User loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "/user/loginView.jsp";
        }

        Purchase result = purchaseService.findPurchase(tranNo);
        if (result == null) {
            return "redirect:/listPurchase.do";
        }

        model.addAttribute("purchase", result);

        // 수정 화면으로 네비
        return "/purchase/updatePurchaseView.jsp";
    }

    // 구매 수정 처리
    @RequestMapping("/updatePurchase.do")
    public String updatePurchase(Purchase purchase,
                                 @RequestParam("tranNo") int tranNo,
                                 HttpSession session) throws Exception {

        // 로그인 체크
        User loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "/user/loginView.jsp";
        }

        // 수정 후 네비게이션
        purchase.setTranNo(tranNo);
        purchaseService.updatePurchase(purchase);
        return "redirect:/getPurchase.do?tranNo=" + tranNo;
    }

    // 거래상태코드 변경(단건)
    @RequestMapping("/updateTranCode.do")
    public String updateTranCode(@RequestParam("tranNo") int tranNo,
                                 @RequestParam("code") String code,
                                 HttpSession session) throws Exception {

        // 로그인 체크
        User loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "/user/loginView.jsp";
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tranNo", tranNo);
        map.put("code", code);

        purchaseService.updateTranCode(map);

        // 변경 후 네비게이션
        return "redirect:/getPurchase.do?tranNo=" + tranNo;
    }

    // 거래상태코드 변경(상품 기준)
    @RequestMapping("/updateTranCodeByProd.do")
    public String updateTranCodeByProd(@RequestParam("prodNo") int prodNo,
                                       @RequestParam("code") String code,
                                       HttpSession session) throws Exception {

        // 로그인 체크
        User loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "/user/loginView.jsp";
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("prodNo", prodNo);
        map.put("code", code);

        purchaseService.updateTranCodeByProd(map);

        // 변경 후 네비게이션
        return "redirect:/getProduct.do?prodNo=" + prodNo;
    }

    // 내부 공용: 로그인 사용자
    private User getLoginUser(HttpSession session) {
        Object obj = session.getAttribute("user");
        if (obj == null) {
            obj = session.getAttribute("userVO");
        }
        if (obj instanceof User) {
            return (User) obj;
        }
        return null;
    }
}
