package com.model2.mvc.service.product.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductDao;
import com.model2.mvc.service.product.ProductService;

@Service("productServiceImpl")
public class ProductServiceImpl implements ProductService {

    @Autowired
    @Qualifier("productDaoImpl")
    private ProductDao productDao;

    @Override public int addProduct(Product p) throws Exception { return productDao.addProduct(p); }
    @Override public Product getProduct(int no) throws Exception { return productDao.getProduct(no); }
    @Override public int updateProduct(Product p) throws Exception { return productDao.updateProduct(p); }
    @Override public Map<String,Object> getProductList(Search s) throws Exception { 
      List<Product> list = productDao.getProductList(s);
      int total = productDao.getTotalCount(s);
      Map<String,Object> m = new HashMap<>(); m.put("list",list); m.put("totalCount",total); return m;
    }
    @Override public int deleteProduct(int no) throws Exception { return productDao.deleteProduct(no); }

}
