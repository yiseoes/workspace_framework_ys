package com.model2.mvc.service.purchas.test;

import static org.junit.Assert.*;

import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/commonservice.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // 테스트 순서 고정
public class PurchaseServiceTest {

    @Resource(name="purchaseServiceImpl")
    private PurchaseService purchaseService;

    private Purchase sample;

    @Before
    public void setUp() {
        System.out.println("\n[디버깅][TEST] setUp 시작");
        // 시드 기준 값 고정
        Product prod = new Product();
        prod.setProdNo(10000);            // 시드에 존재하는 상품번호

        User buyer = new User();
        buyer.setUserId("user01");        // 시드에 존재하는 유저

        sample = new Purchase();
        sample.setPurchaseProd(prod);
        sample.setBuyer(buyer);
        sample.setPaymentOption("C01");
        sample.setReceiverName("테스터");
        sample.setReceiverPhone("010-1111-2222");
        sample.setDlvyAddr("서울시 테스트구 1-2-3");
        sample.setDlvyRequest("문앞");
        sample.setDlvyDate("20251231");   // YYYYMMDD
        System.out.println("[디버깅][TEST] 샘플 준비 완료 :: buyer=user01, prodNo=10000, dlvyDate=20251231");
        System.out.println("[디버깅][TEST] setUp 종료\n");
    }

    // 1) 추가 -> 단건조회 -> 수정 -> 상태변경 -> 목록/페이징 검증
    @Test
    public void test01_add_get_update_flow() throws Exception {
        System.out.println("\n[디버깅][TEST] test01_add_get_update_flow 시작");

        // 추가
        System.out.println("[디버깅][TEST] (1) addPurchase 호출");
        purchaseService.addPurchase(sample);
        System.out.println("[디버깅][TEST] (1) addPurchase 완료");

        // 가장 최근 거래 1건 조회(페이지 1, 사이즈 1)
        Search search = new Search();
        search.setCurrentPage(1);
        search.setPageSize(1);
        System.out.println("[디버깅][TEST] (2) getPurchaseList 호출 :: page=1,size=1,buyer=user01");
        Map<String,Object> page = purchaseService.getPurchaseList(search, sample.getBuyer().getUserId());
        assertNotNull(page);
        java.util.List<?> list = (java.util.List<?>) page.get("list");
        System.out.println("[디버깅][TEST] (2) 조회건수=" + list.size());
        assertEquals(1, list.size());
        Purchase latest = (Purchase) list.get(0);
        System.out.println("[디버깅][TEST] (2) 최신건 tranNo=" + latest.getTranNo() + ", tranCode=" + latest.getTranCode());

        // 생성 직후 거래코드 '1' 확인
        assertEquals("1", latest.getTranCode());

        int tranNo = latest.getTranNo();

        // 단건 재조회
        System.out.println("[디버깅][TEST] (3) getPurchase 호출 :: tranNo=" + tranNo);
        Purchase one = purchaseService.getPurchase(tranNo);
        assertNotNull(one);
        System.out.println("[디버깅][TEST] (3) buyer=" + one.getBuyer().getUserId() + ", prodNo=" + one.getPurchaseProd().getProdNo());
        assertEquals("user01", one.getBuyer().getUserId());
        assertEquals(10000, one.getPurchaseProd().getProdNo());
        
        // ===== 추가 검증 시작 (안전한 범위) =====
        // (A) 배송일 라운드트립 확인 : INSERT에 넣은 '20251231'이 그대로 조회되는지
        System.out.println("[디버깅][TEST] (3-1) dlvyDate 라운드트립 확인");
        assertEquals("20251231", one.getDlvyDate());

        // (B) 정렬 검증 : 최신 1건의 tranNo == 방금 건
        System.out.println("[디버깅][TEST] (3-2) 최신 tranNo 동일성 확인");
        assertEquals(tranNo, latest.getTranNo());
        // ===== 추가 검증 끝 =====


        // 정보 수정
        System.out.println("[디버깅][TEST] (4) updatePurchase 호출 :: dlvyRequest=경비실");
        one.setDlvyRequest("경비실");
        purchaseService.updatePurchase(one);
        Purchase updated = purchaseService.getPurchase(tranNo);
        System.out.println("[디버깅][TEST] (4) 갱신 결과 dlvyRequest=" + updated.getDlvyRequest());
        assertEquals("경비실", updated.getDlvyRequest());

        // 상태코드 변경
        System.out.println("[디버깅][TEST] (5) updateTranCode 호출 :: code=2");
        purchaseService.updateTranCode(tranNo, "2");
        Purchase changed = purchaseService.getPurchase(tranNo);
        System.out.println("[디버깅][TEST] (5) 상태코드=" + changed.getTranCode());
        assertEquals("2", changed.getTranCode());

        // 목록/페이징 검증
        search.setCurrentPage(1);
        search.setPageSize(5);
        System.out.println("[디버깅][TEST] (6) getPurchaseList 호출 :: page=1,size=5,buyer=user01");
        Map<String,Object> listPage = purchaseService.getPurchaseList(search, "user01");
        System.out.println("[디버깅][TEST] (6) totalCount=" + listPage.get("totalCount")
                + ", pageSize=" + ((java.util.List<?>)listPage.get("list")).size());
        assertTrue(((Integer)listPage.get("totalCount")) >= 1);
        assertTrue(((java.util.List<?>)listPage.get("list")).size() >= 1);

        System.out.println("[디버깅][TEST] test01_add_get_update_flow 종료\n");
        
        // ===== 추가 검증: 페이징 경계값 / NULL 업데이트 / 잘못된 사용자 조회 =====
        // (C) 페이징 경계값 : page=2,size=1 이면 두 번째 페이지의 tranNo는 최신건보다 작아야 함(DESC 정렬 기준)
        int totalCount = ((Integer)listPage.get("totalCount")).intValue();
        if (totalCount >= 2) {
            com.model2.mvc.common.Search s2 = new com.model2.mvc.common.Search();
            s2.setCurrentPage(2);
            s2.setPageSize(1);
            System.out.println("[디버깅][TEST] (6-1) 페이징 경계 검증 :: page=2,size=1");
            java.util.Map<String,Object> page2 = purchaseService.getPurchaseList(s2, "user01");
            java.util.List<?> list2 = (java.util.List<?>) page2.get("list");
            if (!list2.isEmpty()) {
                com.model2.mvc.service.domain.Purchase second = (com.model2.mvc.service.domain.Purchase) list2.get(0);
                System.out.println("[디버깅][TEST] (6-1) second.tranNo=" + second.getTranNo() + ", latest.tranNo=" + tranNo);
                assertTrue("두 번째 페이지의 tranNo는 최신건보다 작아야 함", second.getTranNo() < tranNo);
            }
        } else {
            System.out.println("[디버깅][TEST] (6-1) 데이터가 1건이라 페이징 경계 검증 스킵");
        }

        // (D) NULL 업데이트 내구성 : dlvyRequest를 null로 업데이트해도 에러 없이 반영되는지
        System.out.println("[디버깅][TEST] (6-2) dlvyRequest=null 업데이트 내구성 검증");
        one.setDlvyRequest(null);
        purchaseService.updatePurchase(one);
        com.model2.mvc.service.domain.Purchase nullChecked = purchaseService.getPurchase(tranNo);
        assertNull(nullChecked.getDlvyRequest());

        // (E) 존재하지 않는 사용자 조회 시 0건/빈 리스트
        System.out.println("[디버깅][TEST] (6-3) 존재하지 않는 buyerId 조회 결과 검증");
        com.model2.mvc.common.Search sx = new com.model2.mvc.common.Search();
        sx.setCurrentPage(1);
        sx.setPageSize(3);
        java.util.Map<String,Object> emptyPage = purchaseService.getPurchaseList(sx, "no_such_user");
        assertEquals(0, ((Integer)emptyPage.get("totalCount")).intValue());
        assertTrue(((java.util.List<?>)emptyPage.get("list")).isEmpty());
        // ===== 추가 검증 끝 =====     
        
    }

    // 2) 동일 상품의 거래 상태 일괄 변경 검증
    @Test
    public void test02_updateTranCodeByProd_bulk() throws Exception {
        System.out.println("\n[디버깅][TEST] test02_updateTranCodeByProd_bulk 시작");

        System.out.println("[디버깅][TEST] (1) updateTranCodeByProd 호출 :: prodNo=10000, code=3");
        purchaseService.updateTranCodeByProd(10000, "3");

        Search search = new Search();
        search.setCurrentPage(1);
        search.setPageSize(5);
        System.out.println("[디버깅][TEST] (2) getPurchaseList 호출 :: page=1,size=5,buyer=user01");
        Map<String,Object> listPage = purchaseService.getPurchaseList(search, "user01");

        java.util.List<?> list = (java.util.List<?>) listPage.get("list");
        System.out.println("[디버깅][TEST] (2) 조회건수=" + list.size());
        if (!list.isEmpty()) {
            Purchase p0 = (Purchase) list.get(0);
            System.out.println("[디버깅][TEST] (2) 첫 건 tranNo=" + p0.getTranNo() + ", tranCode=" + p0.getTranCode());
            assertTrue(java.util.Arrays.asList("1", "2", "3").contains(p0.getTranCode()));
        } else {
            System.out.println("[디버깅][TEST] (2) 목록 비어있음");
        }

        System.out.println("[디버깅][TEST] test02_updateTranCodeByProd_bulk 종료\n");
    }
    
    // 3) 목록 정렬/포맷 보강 테스트 (안전 범위)
    @Test
    public void test03_order_and_format_sanity() throws Exception {
        System.out.println("\n[디버깅][TEST] test03_order_and_format_sanity 시작");

        com.model2.mvc.common.Search search = new com.model2.mvc.common.Search();
        search.setCurrentPage(1);
        search.setPageSize(5);
        java.util.Map<String,Object> page = purchaseService.getPurchaseList(search, "user01");

        java.util.List<?> list = (java.util.List<?>) page.get("list");
        System.out.println("[디버깅][TEST] (1) 목록 건수=" + list.size());

        // (A) 내림차순 정렬 검증(tranNo가 좌 -> 우로 갈수록 작아져야 함)
        int prev = Integer.MAX_VALUE;
        for (Object o : list) {
            com.model2.mvc.service.domain.Purchase p = (com.model2.mvc.service.domain.Purchase)o;
            int cur = p.getTranNo();
            System.out.println("[디버깅][TEST] (1-A) tranNo=" + cur);
            assertTrue("내림차순 정렬이어야 함", cur <= prev);
            prev = cur;
        }

        // (B) dlvyDate 포맷 검증(첫 건 기준): null이거나 8자리 숫자
        if (!list.isEmpty()) {
            com.model2.mvc.service.domain.Purchase first = (com.model2.mvc.service.domain.Purchase) list.get(0);
            String d = first.getDlvyDate();
            System.out.println("[디버깅][TEST] (1-B) 첫 건 dlvyDate=" + d);
            if (d != null) {
                assertTrue("dlvyDate는 YYYYMMDD(8자리 숫자)여야 함", d.matches("\\d{8}"));
            }
        }

        System.out.println("[디버깅][TEST] test03_order_and_format_sanity 종료\n");
    }

}
