package com.model2.mvc.service.purchase;

import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;

public interface PurchaseDao {
    // 기존 메서드명/시그니처 유지
    void addPurchase(Purchase vo) throws Exception;

    Purchase findPurchase(int tranNo) throws Exception;

    Map<String, Object> findPurchaseList(Search search, String buyerId) throws Exception;

    void updatePurchase(Purchase vo) throws Exception;

    void updateTranCode(int tranNo, String code) throws Exception;

    void updateTranCodeByProd(int prodNo, String code) throws Exception;
    // 총건수
    int getTotalCount(String buyerId) throws Exception;
}
