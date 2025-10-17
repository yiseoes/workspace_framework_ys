package com.model2.mvc.service.purchase.impl;

import java.util.List;
import java.util.Map;

import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseDao;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * Mapper 네임스페이스/ID와 파라미터 타입을 정확히 맞춘다.
 */
@Repository("purchaseDao")
public class PurchaseDaoImpl implements PurchaseDao {

    @Autowired
    @Qualifier("sqlSessionTemplate")
    private SqlSessionTemplate sqlSession; // 스프링에서 빈으로 등록된 SqlSessionTemplate 사용

    public int addPurchase(Purchase purchase) throws Exception {
        return sqlSession.insert("PurchaseMapper.addPurchase", purchase);
    }

    public Purchase findPurchase(int tranNo) throws Exception {
        return sqlSession.selectOne("PurchaseMapper.findPurchase", tranNo);
    }

    public List<Purchase> findPurchaseList(Map<String, Object> map) throws Exception {
        return sqlSession.selectList("PurchaseMapper.findPurchaseList", map);
    }

    public int getTotalCount(String buyerId) throws Exception {
        return sqlSession.selectOne("PurchaseMapper.getTotalCount", buyerId);
    }

    public int updatePurchase(Purchase purchase) throws Exception {
        return sqlSession.update("PurchaseMapper.updatePurchase", purchase);
    }

    public int updateTranCode(Map<String, Object> map) throws Exception {
        return sqlSession.update("PurchaseMapper.updateTranCode", map);
    }

    public int updateTranCodeByProd(Map<String, Object> map) throws Exception {
        return sqlSession.update("PurchaseMapper.updateTranCodeByProd", map);
    }
}
