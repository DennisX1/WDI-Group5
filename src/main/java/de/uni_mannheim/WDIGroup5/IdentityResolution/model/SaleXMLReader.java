package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;
import org.w3c.dom.Node;

public class SaleXMLReader extends XMLMatchableReader<Sale, Attribute> {


    @Override
    public Sale createModelFromElement(Node node, String provenanceInfo) {
        String id = getValueFromChildElement(node,"ID");

        Sale sale = new Sale(id,provenanceInfo);


        return null;
    }
}
