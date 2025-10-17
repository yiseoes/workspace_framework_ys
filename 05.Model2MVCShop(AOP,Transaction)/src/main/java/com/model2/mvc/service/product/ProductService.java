package com.model2.mvc.service.product;

import java.util.Map;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;

public interface ProductService {
	int addProduct(Product product) throws Exception;
	Product getProduct(int prodNo) throws Exception;
	int updateProduct(Product product) throws Exception;
	Map<String,Object> getProductList(Search search) throws Exception;
	int deleteProduct(int prodNo) throws Exception;

}
