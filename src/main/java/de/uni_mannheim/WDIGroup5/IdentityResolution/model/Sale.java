package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.io.Serializable;

public class Sale extends AbstractRecord<Attribute> implements Serializable {

    private double japanSales;
    private double firstWeekSales;
    private double price;




    @Override
    public boolean hasValue(Attribute attribute) {
        return false;
    }



    public double getJapanSales() {
        return japanSales;
    }

    public void setJapanSales(double japanSales) {
        this.japanSales = japanSales;
    }

    public double getFirstWeekSales() {
        return firstWeekSales;
    }

    public void setFirstWeekSales(double firstWeekSales) {
        this.firstWeekSales = firstWeekSales;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


}
