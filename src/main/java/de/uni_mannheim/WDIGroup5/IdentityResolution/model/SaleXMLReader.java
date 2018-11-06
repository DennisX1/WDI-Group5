package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;
import org.w3c.dom.Node;

public class SaleXMLReader extends XMLMatchableReader<Sale, Attribute> {


    @Override
    public Sale createModelFromElement(Node salesNode, String provenanceInfo) {
        String id = getValueFromChildElement(salesNode.getParentNode().getParentNode(),"ID");

        Sale sale = new Sale(id,provenanceInfo);
                
        String firstWeekSalesString = getValueFromChildElement(salesNode.getParentNode(), "FirstWeekSales");
        if(!(firstWeekSalesString == null || firstWeekSalesString.equals("N/A"))) {
            Double firstWeekSales = new Double(firstWeekSalesString);
            sale.setFirstWeekSales(firstWeekSales);

        }
        else {
        	Double firstWeekSales = 0.00;
            sale.setFirstWeekSales(firstWeekSales);
        }
        
        
        String japanSalesString = getValueFromChildElement(salesNode.getParentNode(), "JapanSales");
        if(!(japanSalesString == null || japanSalesString.equals("N/A"))) {
            Double japanSales = new Double(japanSalesString);
            sale.setJapanSales(japanSales);

        }
        else {
        	Double japanSales = 0.00;
            sale.setJapanSales(japanSales);
        }
        String priceString = getValueFromChildElement(salesNode.getParentNode(), "Price");
        if(!(priceString == null || priceString.equals("N/A"))) {
            Double price = new Double(priceString);
            sale.setPrice(price);

        }
        else {
        	Double price = 0.00;
            sale.setPrice(price);
        }
       

        return sale;
    }
}
