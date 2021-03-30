package com.FiveSGroup.TMS.TakePhotoFragment;

public class GetOrderCDEventbus {
    private String orderCD;

    public String getOrderCD() {
        return orderCD;
    }

    public void setOrderCD(String orderCD) {
        this.orderCD = orderCD;
    }

    public GetOrderCDEventbus(String orderCD) {
        this.orderCD = orderCD;
    }
}
