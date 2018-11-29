package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*
 * Main reader class. It is invocated after loading the data. It subsumes the other existing readers 
 * to generate a full game entity with all the information from one xml!
 * if some attributes aren't filled, there is N/A
 */


import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GameXMLReader extends XMLMatchableReader<Game, Attribute> {

	@Override
	protected void initialiseDataset(DataSet<Game, Attribute> dataset) {
		super.initialiseDataset(dataset);
		
		dataset.addAttribute(Game.GAMETITLE);
		dataset.addAttribute(Game.GENRE);
		dataset.addAttribute(Game.PLATFORM);
		dataset.addAttribute(Game.RELEASEDATE);
		dataset.addAttribute(Game.PUBLISHER);
		dataset.addAttribute(Game.SALES);

		
	}
	
	
	
	@Override
    public Game createModelFromElement(Node node, String provenanceInfo) {

		
		// get all child nodes
		NodeList children = node.getChildNodes();
		
        String id = getValueFromChildElement(node, "ID");
              
        Game game = new Game(id,provenanceInfo);
        
        
        //Fill String attribute        
        game.setGameTitle(getValueFromChildElement(node,"GameTitle"));
        game.setGenre(getValueFromChildElement(node,"Genre"));
        game.setPlatform(getValueFromChildElement(node,""
        		+ "Platform"));
        
        //Parse Date - <ReleaseDate>1999-11-21</ReleaseDate>
        String date = getValueFromChildElement(node, "ReleaseDate");
        if (date != null && !date.isEmpty()) {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd")
                    .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter(Locale.ENGLISH);
            if(date.endsWith("T00:00")) {
            	date = date.replace("T00:00","");
            }
            LocalDateTime dt = LocalDateTime.parse(date, formatter);
            game.setReleaseDate(dt);
        }
        else {
        	DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd")
                    .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter(Locale.ENGLISH);
        	date = "0003-11-01";
            LocalDateTime dt = LocalDateTime.parse(date, formatter);
            game.setReleaseDate(dt);

        }

                
        
        
        //Get the publisher Data
        List <Publisher> publisherList;
        
        		
        publisherList = getObjectListFromChildElement(node, "Publisher",
				"PublisherName", new PublisherXMLReader(), provenanceInfo);
        if(!publisherList.isEmpty()) {
            game.setPublisher(publisherList.get(0));

		} else {
			game.setPublisher(new Publisher(id, provenanceInfo));
		}
        
        
		//Get Sales Data
		List<Sale> salesList = getObjectListFromChildElement(node, "Sales",
					"JapanSales", new SaleXMLReader(), provenanceInfo);
		if(salesList != null && !salesList.isEmpty()) {
			game.setSales(salesList.get(0));

		}		
		
		
        return game;
    }
}
