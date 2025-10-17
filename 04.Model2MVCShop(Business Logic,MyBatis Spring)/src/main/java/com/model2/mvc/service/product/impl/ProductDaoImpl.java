package com.model2.mvc.service.product.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductDao;

@Repository("productDaoImpl")
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private SqlSession sqlSession;

    public ProductDaoImpl() {
        System.out.println("[DAO-초기화] " + getClass().getName() + " 가 초기화되었습니다.");
    }

    @Override
    public int addProduct(Product product) throws Exception {
        System.out.println("[DAO] INSERT 실행 → " + product);
        int rows = sqlSession.insert("ProductMapper.addProduct", product);
        System.out.println("[DAO] INSERT 완료, 반영 행수=" + rows);
        return rows;
    }

    @Override
    public Product getProduct(int prodNo) throws Exception {
        System.out.println("[DAO] SELECT ONE 실행 → 상품번호=" + prodNo);
        Product p = sqlSession.selectOne("ProductMapper.getProduct", prodNo);
        System.out.println("[DAO] SELECT ONE 결과 → " + p);
        return p;
    }

    @Override
    public int updateProduct(Product product) throws Exception {
        System.out.println("[DAO] UPDATE 실행 → " + product);
        int rows = sqlSession.update("ProductMapper.updateProduct", product);
        System.out.println("[DAO] UPDATE 완료, 반영 행수=" + rows);
        return rows;
    }

    @Override
    public List<Product> getProductList(Search search) throws Exception {
        System.out.println("[DAO] LIST 실행 → page=" + search.getCurrentPage()
                + ", size=" + search.getPageSize()
                + ", 조건=" + search.getSearchCondition()
                + ", 검색어=" + search.getSearchKeyword());
        List<Product> list = sqlSession.selectList("ProductMapper.getProductList", search);
        System.out.println("[DAO] LIST 결과 건수=" + (list!=null?list.size():0));
        return list;
    }

    @Override
    public int getTotalCount(Search search) throws Exception {
        System.out.println("[DAO] COUNT 실행 → 조건=" + search.getSearchCondition()
                + ", 검색어=" + search.getSearchKeyword());
        int cnt = sqlSession.selectOne("ProductMapper.getTotalCount", search);
        System.out.println("[DAO] COUNT 결과=" + cnt);
        return cnt;
    }
    
    @Override
    public int deleteProduct(int prodNo) throws Exception {
        System.out.println("[DAO] DELETE 실행 → 상품번호=" + prodNo);
        int rows = sqlSession.delete("ProductMapper.deleteProduct", prodNo);
        System.out.println("[DAO] DELETE 완료, 반영 행수=" + rows);
        return rows;
    }

    @Override
    public int countRefByProduct(int prodNo) throws Exception {
        return sqlSession.selectOne("ProductMapper.countRefByProduct", prodNo);
    }

}
