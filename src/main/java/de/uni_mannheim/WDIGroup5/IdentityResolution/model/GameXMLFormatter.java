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
        game.appendChild(createTextElement("id",record.getId(), document));
        game.appendChild(createTextElement("GameTitle",record.getGameTitle(), document));
        game.appendChild(createTextElement("Genre",record.getGenre(), document));
        game.appendChild(createTextElement("Platform",record.getPlatform(), document));
        game.appendChild(createTextElement("ReleaseDate",record.getReleaseDate().toString(), document));

       //Not sure how to handle nested elements!


        return game;
    }
}
