package com.model2.mvc.web.purchase;

import java.util.Map;
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
            @RequestParam(value="menu", required=false) String menu,
            @RequestParam(value="currentPage", required=false) Integer currentPage
    ) throws Exception {

        purchaseService.updateTranCodeByProd(prodNo, tranCode);

        // 07 스타일: 상품 목록 경로도 /product/listProduct 로 통일
        StringBuilder redirect = new StringBuilder("redirect:/product/listProduct");
        boolean hasQuery = false;
        if (menu != null && !menu.isEmpty()) {
            redirect.append(hasQuery ? "&" : "?").append("menu=").append(menu);
            hasQuery = true;
        }
        if (currentPage != null && currentPage > 0) {
            redirect.append(hasQuery ? "&" : "?").append("currentPage=").append(currentPage);
        }
        return redirect.toString();
    }
}
