package com.model2.mvc.service.product.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductDao;

@Repository("productDaoImpl")
public class ProductDaoImpl implements ProductDao {

    @Autowired
    @Qualifier("sqlSessionTemplate")
    private SqlSession sqlSession;

    @Override 
    public int addProduct(Product product) throws Exception { return sqlSession.insert("ProductMapper.addProduct", product); }
    
    @Override 
    public Product getProduct(int prodNo) throws Exception { return sqlSession.selectOne("ProductMapper.getProduct", prodNo); }
    
    @Override 
    public int updateProduct(Product product) throws Exception { return sqlSession.update("ProductMapper.updateProduct", product); }
    
    @Override 
    public List<Product> getProductList(Search search) throws Exception { return sqlSession.selectList("ProductMapper.getProductList", search); }
    
    @Override 
    public int getTotalCount(Search search) throws Exception { return sqlSession.selectOne("ProductMapper.getTotalCount", search); }
    
    @Override 
    public int deleteProduct(int prodNo) throws Exception { return sqlSession.delete("ProductMapper.deleteProduct", prodNo); }
    
    @Override
    public int updateProTranCodeToSold(int prodNo) throws Exception {
        return sqlSession.update("ProductMapper.updateProTranCodeToSold", prodNo);
    }

    @Override
    public int updateProTranCodeToSale(int prodNo) throws Exception {
        return sqlSession.update("ProductMapper.updateProTranCodeToSale", prodNo);
    }


    // (선택)
    // @Override
    // public int countRefByProduct(int prodNo) throws Exception {
    //     return sqlSession.selectOne("ProductMapper.countRefByProduct", prodNo);
    // }
}
