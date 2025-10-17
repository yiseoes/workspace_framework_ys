// src/main/java/com/model2/mvc/service/domain/Product.java
package com.model2.mvc.service.domain;

import java.util.Date;

public class Product {

    private int prodNo;
    private String prodName;
    private String prodDetail;
    private String manufactureDay; // VARCHAR2(8) 그대로 보관
    private int price;             // NUMBER(10)
    private String imageFile;
    private Date regDate;

    public int getProdNo() { return prodNo; }
    public void setProdNo(int prodNo) { this.prodNo = prodNo; }

    public String getProdName() { return prodName; }
    public void setProdName(String prodName) { this.prodName = prodName; }

    public String getProdDetail() { return prodDetail; }
    public void setProdDetail(String prodDetail) { this.prodDetail = prodDetail; }

    public String getManufactureDay() { return manufactureDay; }
    public void setManufactureDay(String manufactureDay) { this.manufactureDay = manufactureDay; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getImageFile() { return imageFile; }
    public void setImageFile(String imageFile) { this.imageFile = imageFile; }

    public Date getRegDate() { return regDate; }
    public void setRegDate(Date regDate) { this.regDate = regDate; }
    
    @Override
    public String toString() {
        return "상품{번호=" + prodNo
            + ", 이름='" + prodName + '\''
            + ", 제조일='" + manufactureDay + '\''
            + ", 가격=" + price
            + ", 이미지='" + imageFile + '\'' + "}";
    }    
    
}
