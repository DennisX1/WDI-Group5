package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SaleXMLFormatter extends XMLFormatter<Sale> {

    @Override
    public Element createRootElement(Document document) {
        return document.createElement("Sale");
    }

    @Override
    public Element createElementFromRecord(Sale record, Document document) {
        return null;
    }
}
