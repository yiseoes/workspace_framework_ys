// src/main/java/com/model2/mvc/service/product/ProductDao.java
package com.model2.mvc.service.product;

import java.util.List;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;

public interface ProductDao {

    int addProduct(Product product) throws Exception;

    Product getProduct(int prodNo) throws Exception;

    int updateProduct(Product product) throws Exception;

    List<Product> getProductList(Search search) throws Exception;

    int getTotalCount(Search search) throws Exception;
    
    int deleteProduct(int prodNo) throws Exception;
    int countRefByProduct(int prodNo) throws Exception; // 안전 삭제면 필요
}
