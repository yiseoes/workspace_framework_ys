package com.model2.mvc.service.domain;

import java.sql.Date;

public class Product {

    // === Field (table: product) ===
    private int    prodNo;       // 상품 번호 (PK)
    private String prodName;     // 상품명
    private String prodDetail;   // 상품 상세 설명
    private String manuDate;     // 제조일자 (YYYYMMDD 문자열)
    private int    price;        // 상품 가격
    private Date   regDate;      // 등록일 (DB SYSDATE)
    private String fileName;     // 업로드 파일명 (이미지)

    // 상품 상태 코드를 위한 필드
    private String proTranCode;

    // === Constructor ===
    public Product() {}  // 기본 생성자

    // === Getter/Setter ===
    public int getProdNo() {
        return prodNo;
    }
    public void setProdNo(int prodNo) {
        this.prodNo = prodNo;
    }
    public String getProdName() {
        return prodName;
    }
    public void setProdName(String prodName) {
        this.prodName = prodName;
    }
    public String getProdDetail() {
        return prodDetail;
    }
    public void setProdDetail(String prodDetail) {
        this.prodDetail = prodDetail;
    }
    public String getManuDate() {
        return manuDate;
    }
    public void setManuDate(String manuDate) {
        this.manuDate = manuDate;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public Date getRegDate() {
        return regDate;
    }
    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getProTranCode() { 
        return proTranCode; 
    }
    public void setProTranCode(String proTranCode) { 
        this.proTranCode = proTranCode; 
    }

    // === toString ===
    @Override
    public String toString() {
        return "ProductVO : [prodNo] "+prodNo+" [prodName] "+prodName+" [price] "+price;
    }
}