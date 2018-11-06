package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
 * Main reader class. It is invocated after loading the data. It subsumes the other existing readers 
 * to generate a full game entity with all the information from one xml!
 * if some attributes aren't filled, there is N/A
 */


import org.w3c.dom.Node;

public class GameXMLReader extends XMLMatchableReader<Game, Attribute> {

    @Override
    public Game createModelFromElement(Node node, String provenanceInfo) {

        String id = getValueFromChildElement(node, "ID");

        Game game = new Game(id,provenanceInfo);

        //Fill String attribute        
        game.setGameTitle(getValueFromChildElement(node,"GameTitle"));
        game.setGenre(getValueFromChildElement(node,"Genre"));
        game.setPlatform(getValueFromChildElement(node,"Platform"));
        
        //Parse Date - <ReleaseDate>1999-11-21</ReleaseDate>
        SimpleDateFormat relDateFormat = new SimpleDateFormat ("yyyy-MM-dd");
        String releaseDateXML = getValueFromChildElement(node, "ReleaseDate");
        try {
        	if (releaseDateXML != null) {
				Date releaseDate = relDateFormat.parse(releaseDateXML);
				game.setReleaseDate(releaseDate);
        	}
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        
        //Get the publisher Data
        List <Publisher> publisherList;
        Publisher publisher;
        
        publisherList = getObjectListFromChildElement(node.getFirstChild(), "Game",
				"Publisher", new PublisherXMLReader(), provenanceInfo);
		game.setPublisher(publisherList.get(0));
        

		//Get Sales DAta
		List<Sale> salesList = getObjectListFromChildElement(node, "Game",
					"Sales", new SaleXMLReader(), provenanceInfo);
		Sale sales = salesList.get(0);
		game.setSales(sales);
		
		
		
		game.setSales(sales);



        return game;
    }
}
