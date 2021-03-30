package com.FiveSGroup.TMS.TakePhotoFragment;

import android.view.Menu;

public class TransferMenuEventbus {
    private Menu menu;

    public TransferMenuEventbus(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
