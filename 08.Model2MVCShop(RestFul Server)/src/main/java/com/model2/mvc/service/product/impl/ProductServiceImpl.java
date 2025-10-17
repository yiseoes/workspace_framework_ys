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
    
    public ProductServiceImpl(ProductDao productDao){
        this.productDao = productDao;
    }



    @Override
    public int updateProTranCodeToSold(int prodNo) throws Exception {
        int cnt = productDao.updateProTranCodeToSold(prodNo);
        if (cnt != 1) {
            throw new IllegalStateException("상품 상태 전이 실패(판매중->판매완료) 혹은 중복요청 : prodNo=" + prodNo);
        }
        return cnt;
    }

    @Override
    public int updateProTranCodeToSale(int prodNo) throws Exception {
        int cnt = productDao.updateProTranCodeToSale(prodNo);
        if (cnt != 1) {
            throw new IllegalStateException("상품 상태 전이 실패(판매완료->판매중) 혹은 중복요청 : prodNo=" + prodNo);
        }
        return cnt;
    }
    
    @Override
    public void updateProTranCode(int prodNo, String proTranCode) throws Exception {
        productDao.updateProTranCode(prodNo, proTranCode);
    }

}
