package com.apinayami.demo.util.Decorator;

import com.apinayami.demo.model.BillModel;

public abstract class BillDecorator extends BillModel {
    protected BillModel billModel;

    public BillDecorator(BillModel billModel) {
        this.billModel = billModel;
    }

}
