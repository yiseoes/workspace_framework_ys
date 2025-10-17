package com.model2.mvc.service.product;

import java.util.List;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;

public interface ProductDao {
	int addProduct(Product p) throws Exception;
	Product getProduct(int prodNo) throws Exception;
	int updateProduct(Product p) throws Exception;
	List<Product> getProductList(Search s) throws Exception;
	int getTotalCount(Search s) throws Exception;
	int deleteProduct(int prodNo) throws Exception;
	    // ★ 추가: 판매 상태 전환
    int updateProTranCodeToSold(int prodNo) throws Exception;
    int updateProTranCodeToSale(int prodNo) throws Exception;
    void updateProTranCode(int prodNo, String proTranCode) throws Exception;


    // (선택) 참조 카운트가 필요하면
    // int countRefByProduct(int prodNo) throws Exception;
}
