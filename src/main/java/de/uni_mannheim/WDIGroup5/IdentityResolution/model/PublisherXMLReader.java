package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;
import org.w3c.dom.Node;

public class PublisherXMLReader extends XMLMatchableReader<Publisher, Attribute> {



    @Override
    public Publisher createModelFromElement(Node node, String provenanceInfo) {

        String id = getValueFromChildElement(node.getParentNode(), "ID");

        Publisher publisher = new Publisher(id,provenanceInfo);

        //fill String attributes
        publisher.setPublisherName(getValueFromChildElement(node,"PublisherName"));
        publisher.setHeadQuarters(getValueFromChildElement(node,"HeadQuarters"));
        publisher.setNotableGamesPublished(getValueFromChildElement(node,"NotableGamesPublished"));
        publisher.setNotes(getValueFromChildElement(node,"Notes"));
        
        
        /*
         * 
         */
        String establishedString = getValueFromChildElement(node, "Established");
        int established = new Integer (establishedString);
        
        publisher.setEstablished(established);
        
        return publisher;
    }
}
