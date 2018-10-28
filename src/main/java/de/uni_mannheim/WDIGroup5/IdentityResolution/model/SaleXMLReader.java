package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;
import org.w3c.dom.Node;

public class SaleXMLReader extends XMLMatchableReader<Sale, Attribute> {


    @Override
    public Sale createModelFromElement(Node salesNode, String provenanceInfo) {
        String id = getValueFromChildElement(salesNode.getParentNode(),"ID");

        Sale sale = new Sale(id,provenanceInfo);
                
        String firstWeekSalesString = getValueFromChildElement(salesNode, "FirstWeekSales");
        Double firstWeekSales = new Double(firstWeekSalesString);
        
        String japanSalesString = getValueFromChildElement(salesNode, "JapanSales");
        Double japanSales = new Double(japanSalesString);
        
        String priceString = getValueFromChildElement(salesNode, "Price");
        Double price = new Double(priceString);
        
        
        sale.setFirstWeekSales(firstWeekSales);
        sale.setJapanSales(japanSales);
        sale.setPrice(price);

        return sale;
    }
}
