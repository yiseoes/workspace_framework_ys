package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseDao;

@Repository("purchaseDaoImpl")
public class PurchaseDaoImpl implements PurchaseDao {

    @Autowired
    @Qualifier("sqlSessionTemplate")
    private SqlSession sqlSession;

    public PurchaseDaoImpl() {
        System.out.println("[디버깅][DAO] " + getClass().getSimpleName() + " 생성자 호출");
    }

    // INSERT
    public void addPurchase(Purchase vo) throws Exception {
        System.out.println("[디버깅][DAO] addPurchase() 시작");
        System.out.println("  - 구매자ID=" + (vo!=null && vo.getBuyer()!=null ? vo.getBuyer().getUserId() : "null")
                + ", 상품번호=" + (vo!=null && vo.getPurchaseProd()!=null ? vo.getPurchaseProd().getProdNo() : "null")
                + ", 결제옵션=" + (vo!=null ? vo.getPaymentOption() : "null")
                + ", 수령자=" + (vo!=null ? vo.getReceiverName() : "null")
                + ", 연락처=" + (vo!=null ? vo.getReceiverPhone() : "null")
                + ", 주소=" + (vo!=null ? vo.getDlvyAddr() : "null")
                + ", 요청사항=" + (vo!=null ? vo.getDlvyRequest() : "null")
                + ", 배송일(문자)=" + (vo!=null ? vo.getDlvyDate() : "null"));
        sqlSession.insert("PurchaseMapper.addPurchase", vo);
        System.out.println("[디버깅][DAO] addPurchase() 끝");
    }

    // SELECT ONE
    public Purchase findPurchase(int tranNo) throws Exception {
        System.out.println("[디버깅][DAO] findPurchase() 시작 :: tranNo=" + tranNo);
        Purchase out = sqlSession.selectOne("PurchaseMapper.findPurchase", tranNo);
        System.out.println("[디버깅][DAO] findPurchase() 끝 :: 결과 tranNo="
                + (out!=null ? out.getTranNo() : "null")
                + ", 상태코드=" + (out!=null ? out.getTranCode() : "null"));
        return out;
    }

    // LIST (DAO는 통상 리스트만 반환, 서비스에서 totalCount 결합)
    public Map<String, Object> findPurchaseList(Search search, String buyerId) throws Exception {
        System.out.println("[디버깅][DAO] findPurchaseList() 시작 :: buyerId=" + buyerId
                + ", page=" + (search!=null ? search.getCurrentPage() : 0)
                + ", size=" + (search!=null ? search.getPageSize() : 0)
                + ", startRowNum=" + (search!=null ? search.getStartRowNum() : 0)
                + ", endRowNum=" + (search!=null ? search.getEndRowNum() : 0));
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("buyerId", buyerId);
        param.put("startRowNum", search.getStartRowNum());
        param.put("endRowNum", search.getEndRowNum());

        List<Purchase> list = sqlSession.selectList("PurchaseMapper.findPurchaseList", param);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);

        System.out.println("[디버깅][DAO] findPurchaseList() 끝 :: 조회건수=" + (list!=null ? list.size() : 0));
        return result;
    }

    // UPDATE 본문
    public void updatePurchase(Purchase vo) throws Exception {
        System.out.println("[디버깅][DAO] updatePurchase() 시작 :: tranNo=" + (vo!=null ? vo.getTranNo() : 0)
                + ", dlvyRequest=" + (vo!=null ? vo.getDlvyRequest() : "null")
                + ", 주소=" + (vo!=null ? vo.getDlvyAddr() : "null"));
        sqlSession.update("PurchaseMapper.updatePurchase", vo);
        System.out.println("[디버깅][DAO] updatePurchase() 끝");
    }

    // 상태코드 변경(단건)
    public void updateTranCode(int tranNo, String code) throws Exception {
        System.out.println("[디버깅][DAO] updateTranCode() 시작 :: tranNo=" + tranNo + ", code=" + code);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tranNo", tranNo);
        param.put("code", code);
        sqlSession.update("PurchaseMapper.updateTranCode", param);
        System.out.println("[디버깅][DAO] updateTranCode() 끝");
    }

    // 상태코드 변경(상품 기준 일괄)
    public void updateTranCodeByProd(int prodNo, String code) throws Exception {
        System.out.println("[디버깅][DAO] updateTranCodeByProd() 시작 :: prodNo=" + prodNo + ", code=" + code);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("prodNo", prodNo);
        param.put("code", code);
        sqlSession.update("PurchaseMapper.updateTranCodeByProd", param);
        System.out.println("[디버깅][DAO] updateTranCodeByProd() 끝");
    }

    // 총건수
    public int getTotalCount(String buyerId) throws Exception {
        System.out.println("[디버깅][DAO] getTotalCount() 시작 :: buyerId=" + buyerId);
        int cnt = sqlSession.selectOne("PurchaseMapper.getTotalCount", buyerId);
        System.out.println("[디버깅][DAO] getTotalCount() 끝 :: totalCount=" + cnt);
        return cnt;
    }
}
