package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseDao;
import com.model2.mvc.service.purchase.PurchaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("purchaseService")
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    @Qualifier("purchaseDao")
    private PurchaseDao purchaseDao;

    public void addPurchase(Purchase purchase) throws Exception {
        purchaseDao.addPurchase(purchase);
    }

    public Purchase findPurchase(int tranNo) throws Exception {
        return purchaseDao.findPurchase(tranNo);
    }

    public List<Purchase> findPurchaseList(Map<String, Object> map) throws Exception {
        return purchaseDao.findPurchaseList(map);
    }

    public int getTotalCount(String buyerId) throws Exception {
        return purchaseDao.getTotalCount(buyerId);
    }

    public void updatePurchase(Purchase purchase) throws Exception {
        purchaseDao.updatePurchase(purchase);
    }

    public void updateTranCode(Map<String, Object> map) throws Exception {
        purchaseDao.updateTranCode(map);
    }

    public void updateTranCodeByProd(Map<String, Object> map) throws Exception {
        purchaseDao.updateTranCodeByProd(map);
    }

    // ===== 테스트 시그니처 대응 : Search + buyerId -> Map(list, totalCount)
    public Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception {
        // 1) Search가 제공하는 ROWNUM 보정값 사용(중복계산 방지)
        int startRowNum = search.getStartRowNum();
        int endRowNum   = search.getEndRowNum();

        // 2) DAO 호출 파라미터 구성
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("buyerId", buyerId);
        param.put("startRowNum", startRowNum);
        param.put("endRowNum", endRowNum);

        // 3) 조회
        List<Purchase> list = purchaseDao.findPurchaseList(param);
        int totalCount = purchaseDao.getTotalCount(buyerId);

        // 4) 반환 맵 구성
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);
        result.put("totalCount", totalCount);
        // 필요 시 result.put("search", search) 추가 가능(테스트가 안 쓰면 생략)

        return result;
    }
}
