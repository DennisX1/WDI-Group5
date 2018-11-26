package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PublisherXMLFormatter extends XMLFormatter<Publisher> {

    @Override
    public Element createRootElement(Document document) {
        return document.createElement("Publisher");
    }

    public Element createElementFromRecord(Element publisher, Publisher record, Document document) {
        //Element publisher = document.createElement("Publisher");

        //System.out.println(publisher);
        //System.out.println(publisher.getParentNode());

        //Strings
        //publisher.appendChild(createTextElement("PublisherName",record.getPublisherName(),document));
        publisher.appendChild(createTextElement("PublisherName",record.getPublisherName(),document));
		if(record.getHeadQuarters()!=null) {
			publisher.appendChild(createTextElement("HeadQuarters",record.getHeadQuarters(),document));
		}
		if(record.getNotableGamesPublished()!=null) {
			publisher.appendChild(createTextElement("NotableGamesPublished",record.getNotableGamesPublished(),document));
		}
		if(record.getNotes()!=null) {
			publisher.appendChild(createTextElement("Notes",record.getNotes(),document));
		}
		
        //double
		if(record.getEstablished()!=0) {
			publisher.appendChild(createTextElement("Established", ((Integer) record.getEstablished()).toString(),document));
		}
			
        return publisher;
    }

	@Override
	public Element createElementFromRecord(Publisher record, Document doc) {
		// TODO Auto-generated method stub
		return null;
	}
}
