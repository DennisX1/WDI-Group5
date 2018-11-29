package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SaleXMLFormatter extends XMLFormatter<Sale> {

    @Override
    public Element createRootElement(Document document) {
        return document.createElement("Sales");
    }

    public Element createElementFromRecord(Element sale, Sale record, Document document) {
        //Element sale = document.createElement("Sales");
       
    	if(record != null) {
			if(record.getJapanSales()!=0) {
				sale.appendChild(createTextElement("JapanSales", ("" + record.getJapanSales()), document));
			}
			if(record.getFirstWeekSales()!=0) {
				sale.appendChild(createTextElement("FirstWeekSales", ("" + record.getFirstWeekSales()), document));
			}
			if(record.getPrice()!=0) {
				sale.appendChild(createTextElement("Price", ("" + record.getPrice()), document));
			}
    	}

    	
    	return sale;
    }

	@Override
	public Element createElementFromRecord(Sale record, Document doc) {
		// TODO Auto-generated method stub
		return null;
	}
}
