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

    public ProductServiceImpl() {
        System.out.println("[SERVICE-초기화] " + getClass().getName() + " 가 초기화되었습니다.");
    }

    @Override
    public int addProduct(Product product) throws Exception {
        System.out.println("[SERVICE] 상품 등록 요청 수신 → " + product);
        int rows = productDao.addProduct(product);
        System.out.println("[SERVICE] 상품 등록 완료, 반영 행수=" + rows);
        return rows;
    }

    @Override
    public Product getProduct(int prodNo) throws Exception {
        System.out.println("[SERVICE] 상품 단건 조회 요청 → 상품번호=" + prodNo);
        Product p = productDao.getProduct(prodNo);
        System.out.println("[SERVICE] 상품 단건 조회 결과 → " + p);
        return p;
    }

    @Override
    public int updateProduct(Product product) throws Exception {
        System.out.println("[SERVICE] 상품 수정 요청 수신 → " + product);
        int rows = productDao.updateProduct(product);
        System.out.println("[SERVICE] 상품 수정 완료, 반영 행수=" + rows);
        return rows;
    }

    @Override
    public Map<String, Object> getProductList(Search search) throws Exception {
        // 페이징 방어 보정
        int current = search.getCurrentPage() > 0 ? search.getCurrentPage() : 1;
        int size    = search.getPageSize() > 0 ? search.getPageSize() : 10;
        search.setCurrentPage(current);
        search.setPageSize(size);

        System.out.println("[SERVICE] 상품 목록 조회 요청 → page=" + search.getCurrentPage()
                + ", size=" + search.getPageSize()
                + ", 조건=" + search.getSearchCondition()
                + ", 검색어=" + search.getSearchKeyword());

        List<Product> list = productDao.getProductList(search);
        int totalCount = productDao.getTotalCount(search);

        System.out.println("[SERVICE] 상품 목록 조회 결과 → 조회수=" + (list!=null?list.size():0)
                + ", 총건수=" + totalCount);

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("totalCount", totalCount);
        return map;
    }
    
    @Override
    public int deleteProduct(int prodNo) throws Exception {
        System.out.println("[SERVICE] 상품 삭제 요청 → 상품번호=" + prodNo);

        // 안전 삭제: 참조 있으면 중단
        int refs = 0;
        try { refs = productDao.countRefByProduct(prodNo); } catch (Exception ignore) {}
        if (refs > 0) {
            System.out.println("[SERVICE] 삭제 불가 - 거래 내역 존재 (prodNo=" + prodNo + ", 참조수=" + refs + ")");
            return 0; // 필요하면 예외 throw 로 바꿔도 됨
        }

        try {
            int rows = productDao.deleteProduct(prodNo);
            System.out.println("[SERVICE] 상품 삭제 완료, 반영 행수=" + rows);
            return rows;
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            System.out.println("[SERVICE] 삭제 실패(참조 무결성 위반 추정: ORA-02292), prodNo=" + prodNo);
            return 0;
        }
    }

}
