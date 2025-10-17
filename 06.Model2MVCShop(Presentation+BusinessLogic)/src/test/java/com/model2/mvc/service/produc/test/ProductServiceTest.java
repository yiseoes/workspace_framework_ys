package com.model2.mvc.service.produc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:config/commonservice.xml",
    "classpath:sql/mybatis-config.xml"
})
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // 테스트 순서 고정
public class ProductServiceTest {

    @Autowired
    @Qualifier("productServiceImpl")
    private ProductService productService;

    // ===== 유틸: 페이징 계산 =====
    private void applyPaging(Search search) {
    	int current = search.getCurrentPage() > 0 ? search.getCurrentPage() : 1;
        int size    = search.getPageSize() > 0 ? search.getPageSize() : 10;
        search.setCurrentPage(current);
        search.setPageSize(size);
        // ⚠ startRowNum / endRowNum 세터는 호출하지 않음 (게터가 자동 계산)
    }

    // 1) Add → List(by name) → Get → Update end-to-end
    @Test
    public void test01_add_get_update_Product() throws Exception {
        // --- Add
        Product p = new Product();
        p.setProdName("JUnit-" + System.currentTimeMillis());
        p.setProdDetail("테스트 상품 상세");
        p.setManufactureDay("20250101"); // VARCHAR2(8) 그대로
        p.setPrice(123456);
        p.setImageFile("junit-test.jpg");

        int inserted = productService.addProduct(p);
        assertEquals(1, inserted);
        
        System.out.println("[TEST] 상품 INSERT 결과 행수=" + inserted);

        // --- List (이름검색으로 방금 건 찾기)
        Search s = new Search();
        s.setSearchCondition("0");               // ← String
        s.setSearchKeyword(p.getProdName());
        s.setCurrentPage(1);
        s.setPageSize(5);
        applyPaging(s);                          // ← 세터 없는 값 보정만(페이지/사이즈)

        Map<String, Object> result = productService.getProductList(s);
        assertNotNull(result);
        @SuppressWarnings("unchecked")
        List<Product> list = (List<Product>) result.get("list");
        assertNotNull(list);
        assertTrue(list.size() >= 1);

        Product found = list.get(0);
        assertEquals(p.getProdName(), found.getProdName());

        // --- Get (단건 재조회)
        Product one = productService.getProduct(found.getProdNo());
        assertNotNull(one);
        assertEquals(found.getProdNo(), one.getProdNo());
        assertEquals(found.getProdName(), one.getProdName());
        
        System.out.println("[TEST] 검색으로 찾은 첫 상품=" + found);

        // --- Update (가격/이미지 변경)
        one.setPrice(654321);
        one.setImageFile("junit-test-updated.jpg");
        int updated = productService.updateProduct(one);
        assertEquals(1, updated);

        Product updatedOne = productService.getProduct(one.getProdNo());
        assertEquals(654321, updatedOne.getPrice());
        assertEquals("junit-test-updated.jpg", updatedOne.getImageFile());
    }

    // 2) 목록 + 페이징 + 총건수 검증
    @Test
    public void test02_getProductList_Paging() throws Exception {
        Search s = new Search();
        s.setSearchCondition(null);  // 전체
        s.setSearchKeyword(null);
        s.setCurrentPage(1);
        s.setPageSize(5);
        applyPaging(s);

        Map<String, Object> result = productService.getProductList(s);
        @SuppressWarnings("unchecked")
        List<Product> list = (List<Product>) result.get("list");
        int totalCount = (int) result.get("totalCount");

        assertNotNull(list);
        assertTrue(list.size() <= 5);
        assertTrue(totalCount >= list.size());
        
        System.out.println("[TEST] 총건수=" + result.get("totalCount"));
    }
    
    @Test
    public void test03_delete_Product() throws Exception {
        // 1) 임시 상품 등록
        Product p = new Product();
        p.setProdName("DEL-" + System.currentTimeMillis());
        p.setProdDetail("삭제 테스트");
        p.setManufactureDay("20250101");
        p.setPrice(1000);
        p.setImageFile("del.jpg");
        productService.addProduct(p);

        // 2) 방금 등록건 조회(이름 검색으로 prodNo 확보)
        Search s = new Search();
        s.setSearchCondition("0");
        s.setSearchKeyword(p.getProdName());
        s.setCurrentPage(1);
        s.setPageSize(1);
        Map<String,Object> r = productService.getProductList(s);
        @SuppressWarnings("unchecked")
        Product found = ((java.util.List<Product>) r.get("list")).get(0);

        // 3) 삭제
        int rows = productService.deleteProduct(found.getProdNo());
        System.out.println("[TEST] 삭제 결과 행수=" + rows);
        org.junit.Assert.assertEquals(1, rows);

        // 4) 삭제 확인 (null 이어야 정상)
        Product gone = productService.getProduct(found.getProdNo());
        org.junit.Assert.assertNull(gone);
    }

}
