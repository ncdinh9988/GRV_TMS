package com.FiveSGroup.TMS.AddCustomerFragment;

public class CustomerEventbus {
    private CCustomer cCustomer;

    public CustomerEventbus(CCustomer cCustomer) {
        this.cCustomer = cCustomer;
    }

    public CCustomer getcCustomer() {
        return cCustomer;
    }

    public void setcCustomer(CCustomer cCustomer) {
        this.cCustomer = cCustomer;
    }
}
