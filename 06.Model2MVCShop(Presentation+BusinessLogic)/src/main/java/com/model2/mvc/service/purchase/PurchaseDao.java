package com.model2.mvc.service.purchase;

import java.util.List;
import java.util.Map;

import com.model2.mvc.service.domain.Purchase;

public interface PurchaseDao {

    // INSERT
    int addPurchase(Purchase purchase) throws Exception;

    // SELECT ONE
    Purchase findPurchase(int tranNo) throws Exception;

    // SELECT LIST
    List<Purchase> findPurchaseList(Map<String, Object> map) throws Exception;

    // COUNT
    int getTotalCount(String buyerId) throws Exception;

    // UPDATE
    int updatePurchase(Purchase purchase) throws Exception;

    int updateTranCode(Map<String, Object> map) throws Exception;

    int updateTranCodeByProd(Map<String, Object> map) throws Exception;
}
