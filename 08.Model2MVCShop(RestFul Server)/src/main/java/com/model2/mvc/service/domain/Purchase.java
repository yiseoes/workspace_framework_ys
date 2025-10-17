package com.model2.mvc.service.domain;

import java.sql.Date;

import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;

/*
 * 역할
 * - 구매/거래 도메인 VO
 * - 주문자/상품/배송/결제/거래상태 코드값 보관
 * 주의
 * - 필드/메서드명 임의 변경 금지
 */
public class Purchase {

  // 필드
  private User buyer;          // 구매자
  private String dlvyAddr;     // 배송지 주소
  private String dlvyDate;     // 배송 희망일(yyyyMMdd)
  private String dlvyRequest;  // 배송 요청사항
  private Date orderDate;      // 주문일
  private String paymentOption;// 결제수단
  private Product purchaseProd;// 구매 상품
  private String receiverName; // 수령인
  private String receiverPhone;// 수령인 연락처
  private int tranNo;          // 거래번호
  private String tranCode;     // 거래상태코드(1/2/3 등) - 코드값 그대로 사용

  // 게터/세터 (원본 시그니처 유지)
  public User getBuyer() { return buyer; }
  public void setBuyer(User buyer) { this.buyer = buyer; }

  public String getDlvyAddr() { return dlvyAddr; }
  public void setDlvyAddr(String dlvyAddr) { this.dlvyAddr = dlvyAddr; }

  public String getDlvyDate() { return dlvyDate; }
  public void setDlvyDate(String dlvyDate) { this.dlvyDate = dlvyDate; }

  public String getDlvyRequest() { return dlvyRequest; }
  public void setDlvyRequest(String dlvyRequest) { this.dlvyRequest = dlvyRequest; }

  public Date getOrderDate() { return orderDate; }
  public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

  public String getPaymentOption() { return paymentOption; }
  public void setPaymentOption(String paymentOption) { this.paymentOption = paymentOption; }

  public Product getPurchaseProd() { return purchaseProd; }
  public void setPurchaseProd(Product purchaseProd) { this.purchaseProd = purchaseProd; }

  public String getReceiverName() { return receiverName; }
  public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

  public String getReceiverPhone() { return receiverPhone; }
  public void setReceiverPhone(String receiverPhone) { this.receiverPhone = receiverPhone; }

  public int getTranNo() { return tranNo; }
  public void setTranNo(int tranNo) { this.tranNo = tranNo; }

  public String getTranCode() { return tranCode; }
  public void setTranCode(String tranCode) { this.tranCode = tranCode; }

  // 디버깅용 문자열 표현 (한글)
  // - NPE 방지: buyer/purchaseProd null 체크
  // - 개인정보는 최소한으로(아이디/번호만)
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Purchase{");
    sb.append("거래번호=").append(tranNo);
    sb.append(", 거래상태코드=").append(tranCode);
    sb.append(", 구매자ID=").append(buyer != null ? buyer.getUserId() : null);
    sb.append(", 상품번호=").append(purchaseProd != null ? purchaseProd.getProdNo() : null);
    sb.append(", 결제수단=").append(paymentOption);
    sb.append(", 수령인=").append(receiverName);
    sb.append(", 수령인연락처=").append(receiverPhone);
    sb.append(", 배송지주소=").append(dlvyAddr);
    sb.append(", 배송요청=").append(dlvyRequest);
    sb.append(", 배송희망일=").append(dlvyDate);
    sb.append(", 주문일=").append(orderDate);
    sb.append('}');
    return sb.toString();
  }
}
