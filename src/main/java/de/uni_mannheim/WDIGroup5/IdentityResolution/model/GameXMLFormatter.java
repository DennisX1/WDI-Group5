package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GameXMLFormatter extends XMLFormatter<Game> {

    @Override
    public Element createRootElement(Document document) {
        return document.createElement("Games");
    }

    @Override
    public Element createElementFromRecord(Game record, Document document) {
        Element game = document.createElement("Game");
        
        //String elements from Game
        //System.out.println(record);
        //System.out.println(record.getGameTitle());
        //System.out.println(record.getPublisher().getPublisherName());
        //System.out.println(record.getReleaseDate());
        game.appendChild(createTextElement("id",record.getIdentifier(), document));
        game.appendChild(createTextElement("GameTitle",record.getGameTitle(), document));
        game.appendChild(createTextElement("Genre",record.getGenre(), document));
        game.appendChild(createTextElement("Platform",record.getPlatform(), document));
        game.appendChild(createTextElement("ReleaseDate",record.getReleaseDate().toString(), document));
        game.appendChild(createTextElement("Publisher",record.getPublisher().getPublisherName(), document));
        game.appendChild(createTextElement("Sales", ("" + record.getSales().getJapanSales()), document));


       //Not sure how to handle nested elements!


        return game;
    }
}
