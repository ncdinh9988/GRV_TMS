package com.FiveSGroup.TMS.ChangeCusFragment;

public class UpdateCustomer {
    private String CUSTOMER_CODE;
    private String CustomerCd;
    private String LONGITUDE_LATITUDE;

    private String CUSTOMER_NAME;
    private String CUSTOMER_ADDRESS;
    private String PHONE_NUMBER;


    public String getCustomerGeocode() {
        return LONGITUDE_LATITUDE;
    }

    public void setCustomerGeocode(String customerGeocode) {
        LONGITUDE_LATITUDE = customerGeocode;
    }

    public String getCustomerCd() {
        return CustomerCd;
    }

    public void setCustomerCd(String customerCd) {
        CustomerCd = customerCd;
    }

    public String getCustomerCode() {
        return CUSTOMER_CODE;
    }

    public void setCustomerCode(String customerCode) {
        CUSTOMER_CODE = customerCode;
    }

    public String getCustomerName() {
        return CUSTOMER_NAME;
    }

    public void setCustomerName(String customerName) {
        CUSTOMER_NAME = customerName;
    }

    public String getCustomerAddress() {
        return CUSTOMER_ADDRESS;
    }

    public void setCustomerAddress(String customerAddress) {
        CUSTOMER_ADDRESS = customerAddress;
    }

    public String getCustomerPhone() {
        return PHONE_NUMBER;
    }

    public void setCustomerPhone(String customerPhone) {
        PHONE_NUMBER = customerPhone;
    }


}
