package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseDao;
import com.model2.mvc.service.purchase.PurchaseService;

@Service("purchaseServiceImpl")
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    @Qualifier("purchaseDaoImpl")
    private PurchaseDao purchaseDao;

    public PurchaseServiceImpl() {
        System.out.println(":: " + getClass().getName() + " default Constructor");
        // ===================== 디버깅(한글) =====================
        System.out.println("[디버깅][SERVICE] PurchaseServiceImpl 생성자 호출");
        // ======================================================
    }

    public void addPurchase(Purchase purchase) throws Exception {
        // ===================== 디버깅(한글) =====================
        System.out.println("[디버깅][SERVICE] addPurchase 시작");
        System.out.println("  - 구매자ID=" + (purchase!=null && purchase.getBuyer()!=null ? purchase.getBuyer().getUserId() : "null")
                + ", 상품번호=" + (purchase!=null && purchase.getPurchaseProd()!=null ? purchase.getPurchaseProd().getProdNo() : "null")
                + ", 결제옵션=" + (purchase!=null ? purchase.getPaymentOption() : "null")
                + ", 수령자=" + (purchase!=null ? purchase.getReceiverName() : "null")
                + ", 연락처=" + (purchase!=null ? purchase.getReceiverPhone() : "null")
                + ", 주소=" + (purchase!=null ? purchase.getDlvyAddr() : "null")
                + ", 요청사항=" + (purchase!=null ? purchase.getDlvyRequest() : "null")
                + ", 배송일(문자)=" + (purchase!=null ? purchase.getDlvyDate() : "null"));
        // ======================================================
        purchaseDao.addPurchase(purchase);
        // ===================== 디버깅(한글) =====================
        System.out.println("[디버깅][SERVICE] addPurchase 종료");
        // ======================================================
    }

    public Purchase getPurchase(int tranNo) throws Exception {
        // ===================== 디버깅(한글) =====================
        System.out.println("[디버깅][SERVICE] getPurchase 시작 :: tranNo=" + tranNo);
        // ======================================================
        Purchase out = purchaseDao.findPurchase(tranNo);
        // ===================== 디버깅(한글) =====================
        System.out.println("[디버깅][SERVICE] getPurchase 결과 :: "
                + (out!=null ? ("tranNo=" + out.getTranNo() + ", 상태코드=" + out.getTranCode()) : "null"));
        System.out.println("[디버깅][SERVICE] getPurchase 종료");
        // ======================================================
        return out;
    }

    public Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception {
        // ===================== 디버깅(한글) =====================
        System.out.println("[디버깅][SERVICE] getPurchaseList 시작 :: buyerId=" + buyerId
                + ", 요청 page=" + (search!=null ? search.getCurrentPage() : 0)
                + ", 요청 size=" + (search!=null ? search.getPageSize() : 0));
        // ======================================================

        int currentPage = (search != null && search.getCurrentPage() > 0) ? search.getCurrentPage() : 1;
        int pageSize    = (search != null && search.getPageSize() > 0)    ? search.getPageSize()    : 10;

        int startRowNum = (currentPage - 1) * pageSize + 1;
        int endRowNum   = currentPage * pageSize;

        int totalCount = purchaseDao.getTotalCount(buyerId);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("list", purchaseDao.findPurchaseList(search, buyerId).get("list"));
        result.put("totalCount", totalCount);

        System.out.println("[SERVICE] getPurchaseList buyerId=" + buyerId
                + " page=" + currentPage + "/" + pageSize
                + " rows=" + startRowNum + "~" + endRowNum
                + " total=" + totalCount);

        // ===================== 디버깅(한글) =====================
        System.out.println("[디버깅][SERVICE] 페이징 계산 :: currentPage=" + currentPage
                + ", pageSize=" + pageSize + ", startRowNum=" + startRowNum + ", endRowNum=" + endRowNum);
        System.out.println("[디버깅][SERVICE] 조회 결과 요약 :: totalCount=" + totalCount
                + ", page내 리스트크기=" + (((result.get("list")) instanceof java.util.List) ? ((java.util.List<?>)result.get("list")).size() : -1));
        System.out.println("[디버깅][SERVICE] getPurchaseList 종료");
        // ======================================================

        return result;
    }

    public void updatePurchase(Purchase vo) throws Exception {
        // ===================== 디버깅(한글) =====================
        System.out.println("[디버깅][SERVICE] updatePurchase 시작 :: "
                + "tranNo=" + (vo!=null ? vo.getTranNo() : 0)
                + ", 주소=" + (vo!=null ? vo.getDlvyAddr() : "null")
                + ", 요청사항=" + (vo!=null ? vo.getDlvyRequest() : "null")
                + ", 배송일(문자)=" + (vo!=null ? vo.getDlvyDate() : "null"));
        // ======================================================
        purchaseDao.updatePurchase(vo);
        // ===================== 디버깅(한글) =====================
        System.out.println("[디버깅][SERVICE] updatePurchase 종료");
        // ======================================================
    }

    public void updateTranCode(int tranNo, String code) throws Exception {
        // ===================== 디버깅(한글) =====================
        System.out.println("[디버깅][SERVICE] updateTranCode 시작 :: tranNo=" + tranNo + ", code=" + code);
        // ======================================================
        purchaseDao.updateTranCode(tranNo, code);
        // ===================== 디버깅(한글) =====================
        System.out.println("[디버깅][SERVICE] updateTranCode 종료");
        // ======================================================
    }

    public void updateTranCodeByProd(int prodNo, String code) throws Exception {
        // ===================== 디버깅(한글) =====================
        System.out.println("[디버깅][SERVICE] updateTranCodeByProd 시작 :: prodNo=" + prodNo + ", code=" + code);
        // ======================================================
        purchaseDao.updateTranCodeByProd(prodNo, code);
        // ===================== 디버깅(한글) =====================
        System.out.println("[디버깅][SERVICE] updateTranCodeByProd 종료");
        // ======================================================
    }
}
