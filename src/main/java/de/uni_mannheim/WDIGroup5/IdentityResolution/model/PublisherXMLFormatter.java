package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PublisherXMLFormatter extends XMLFormatter<Publisher> {

    @Override
    public Element createRootElement(Document document) {
        return document.createElement("Publisher");
    }

    @Override
    public Element createElementFromRecord(Publisher record, Document document) {
        Element publisher = document.createElement("Publisher");


        //Strings
        publisher.appendChild(createTextElement("PublisherName",record.getPublisherName(),document));
        publisher.appendChild(createTextElement("HeadQuarters",record.getHeadQuarters(),document));
        publisher.appendChild(createTextElement("NotableGamesPublished",record.getNotableGamesPublished(),document));
        publisher.appendChild(createTextElement("Notes",record.getNotes(),document));

        //double
        publisher.appendChild(createTextElement("Established", ((Double) record.getEstablished()).toString(),document));

        return publisher;
    }
}
