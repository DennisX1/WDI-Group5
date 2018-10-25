package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;
import org.w3c.dom.Node;

public class GameXMLReader extends XMLMatchableReader<Game, Attribute> {

    @Override
    public Game createModelFromElement(Node node, String provenanceInfo) {

        String id = getValueFromChildElement(node, "id");

        Game game = new Game(id,provenanceInfo);

        //Fill String attributes
        game.setGameTitle(getValueFromChildElement(node,"GameTitle"));
        game.setGenre(getValueFromChildElement(node,"Genre"));
        game.setPlatform(getValueFromChildElement(node,"Platform"));




        return null;
    }
}
