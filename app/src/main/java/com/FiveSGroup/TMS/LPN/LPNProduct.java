package com.FiveSGroup.TMS.LPN;

public class LPNProduct {
    private String productCode;
    private String productName;
    private String productAmount;
    private String productUnit;
    private String productCurrentPosition;
    private String productExpDate;
    private String productStockDate;

    public LPNProduct() {
    }

    public LPNProduct(String productCode, String productName, String productAmount, String productUnit, String productCurrentPosition, String productExpDate, String productStockDate) {
        this.productCode = productCode;
        this.productName = productName;
        this.productAmount = productAmount;
        this.productUnit = productUnit;
        this.productCurrentPosition = productCurrentPosition;
        this.productExpDate = productExpDate;
        this.productStockDate = productStockDate;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(String productAmount) {
        this.productAmount = productAmount;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductCurrentPosition() {
        return productCurrentPosition;
    }

    public void setProductCurrentPosition(String productCurrentPosition) {
        this.productCurrentPosition = productCurrentPosition;
    }

    public String getProductExpDate() {
        return productExpDate;
    }

    public void setProductExpDate(String productExpDate) {
        this.productExpDate = productExpDate;
    }

    public String getProductStockDate() {
        return productStockDate;
    }

    public void setProductStockDate(String productStockDate) {
        this.productStockDate = productStockDate;
    }
}
