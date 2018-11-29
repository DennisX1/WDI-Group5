package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GameXMLFormatter extends XMLFormatter<Game> {

	PublisherXMLFormatter publisherFormatter = new PublisherXMLFormatter();
	SaleXMLFormatter saleFormatter = new SaleXMLFormatter();

    @Override
    public Element createRootElement(Document document) {
        return document.createElement("Games");
    }

    @Override
    public Element createElementFromRecord(Game record, Document document) {
        Element game = document.createElement("Game");
        
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd")
                .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter(Locale.ENGLISH);
        
        //String elements from Game
        //System.out.println(record);
        //System.out.println(record.getGameTitle());
        //System.out.println(record.getPublisher().getPublisherName());
        //System.out.println(record.getReleaseDate());
        game.appendChild(createTextElement("ID",record.getIdentifier(), document));
        game.appendChild(createTextElementWithProvenance("GameTitle",record.getGameTitle(), record.getMergedAttributeProvenance(Game.GAMETITLE), document));
        game.appendChild(createTextElementWithProvenance("Genre",record.getGenre(), record.getMergedAttributeProvenance(Game.GENRE), document));
        game.appendChild(createTextElementWithProvenance("Platform",record.getPlatform(), record.getMergedAttributeProvenance(Game.PLATFORM), document));
		if(record.getReleaseDate()!=null) {
			game.appendChild(createTextElementWithProvenance("ReleaseDate",record.getReleaseDate().format(formatter), record.getMergedAttributeProvenance(Game.RELEASEDATE), document));
		}
			
        game.appendChild(createPublisherElement(record, document));
        game.appendChild(createSaleElement(record, document));

        
       //Not sure how to handle nested elements!


        return game;
    }
    
	protected Element createTextElementWithProvenance(String name, String value, String provenance, Document doc) {
		Element elem = createTextElement(name, value, doc);
		elem.setAttribute("provenance", provenance);
		return elem;
	}
	
	protected Element createPublisherElement(Game record, Document doc) {
		Element publisherRoot = publisherFormatter.createRootElement(doc);
		publisherRoot.setAttribute("provenance", record.getMergedAttributeProvenance(Game.PUBLISHER));

		publisherFormatter.createElementFromRecord(publisherRoot, record.getPublisher(), doc);

		return publisherRoot;
	}
	
	protected Element createSaleElement(Game record, Document doc) {
		Element saleRoot = saleFormatter.createRootElement(doc);
		saleRoot.setAttribute("provenance", record.getMergedAttributeProvenance(Game.SALES));

		saleFormatter.createElementFromRecord(saleRoot, record.getSales(), doc);

		return saleRoot;
	}
    
}
