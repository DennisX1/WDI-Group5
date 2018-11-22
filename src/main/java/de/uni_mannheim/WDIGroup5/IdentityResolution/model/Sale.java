package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.io.Serializable;

public class Sale extends AbstractRecord<Attribute> implements Serializable {

    private double japanSales;
    private double firstWeekSales;
    private double price;


    public Sale(String identifier, String provenance) {
        super(identifier, provenance);
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




    public static final Attribute JAPANSALES = new Attribute("JapanSales");
    public static final Attribute FIRSTWEEKSALES = new Attribute("FirstWeekSales");
    public static final Attribute PRICE = new Attribute("Price");


    @Override
    public boolean hasValue(Attribute attribute) {

        if(attribute==JAPANSALES)
            return getJapanSales() == 0;
        else if(attribute==FIRSTWEEKSALES)
            return getFirstWeekSales()== 0;
        else if(attribute==PRICE)
            return getPrice()== 0;
        return false;
    }


}
