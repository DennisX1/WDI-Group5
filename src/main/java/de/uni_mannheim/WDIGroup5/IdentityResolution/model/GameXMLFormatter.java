package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GameXMLFormatter extends XMLFormatter<Game> {

    @Override
    public Element createRootElement(Document document) {
        return null;
    }

    @Override
    public Element createElementFromRecord(Game game, Document document) {
        return null;
    }
}
