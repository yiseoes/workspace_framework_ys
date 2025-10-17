package com.model2.mvc.service.purchase;

import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;

public interface PurchaseService {

    void addPurchase(Purchase purchase) throws Exception;

    Purchase findPurchase(int tranNo) throws Exception;

    List<Purchase> findPurchaseList(Map<String, Object> map) throws Exception;

    int getTotalCount(String buyerId) throws Exception;

    void updatePurchase(Purchase purchase) throws Exception;

    void updateTranCode(Map<String, Object> map) throws Exception;

    void updateTranCodeByProd(Map<String, Object> map) throws Exception;

    // 테스트 시그니처 대응 : Search + buyerId -> Map(list, totalCount)
    Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception;
}
