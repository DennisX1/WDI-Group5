package de.uni_mannheim.WDIGroup5.IdentityResolution.solutions;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.Logger;

import de.uni_mannheim.WDIGroup5.IdentityResolution.model.*;
import de.uni_mannheim.informatik.dws.winter.model.*;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.*;
import de.uni_mannheim.informatik.dws.winter.processing.*;
import de.uni_mannheim.informatik.dws.winter.utils.*;

public class ErrorAnalysis {
		
	/**
	 * NOT YET FINISHED
	 * For Publisher + Game atm not working bc cannot resolve publisher_id to game
	 */
	
	private static final Logger logger = WinterLogManager.getLogger();

	public void printFalsePositives(Processable<Correspondence<Game, Attribute>> correspondences, MatchingGoldStandard gs) {
		
		// go through the correspondences and check if they are incorrect
		for(Correspondence<Game, Attribute> c : correspondences.get()) {
			// is the match incorrect?
						
			if(gs.containsNegative(c.getFirstRecord(), c.getSecondRecord())) {
				
				// if yes, print the records to the console
				Game g1 = c.getFirstRecord();
				Game g2 = c.getSecondRecord();
				
				// print both records to the console
				logger.info("[Incorrect Correspondence]");
				logger.info(String.format("\t%s", g1));	
				logger.info(String.format("\t%s", g2));	
			}
		}		
	}
	
	public void printFalseNegatives(DataSet<Game, Attribute> ds1, DataSet<Game, Attribute> ds2, Processable<Correspondence<Game, Attribute>> correspondences, MatchingGoldStandard gs) {
		
		// first generate a set of all correct correspondences in the gold standard
		// (if a pair is not in the gold standard, we cannot say if its correct or not)
		
		Set<Pair<String,String>> allPairs = new HashSet<>();
		allPairs.addAll(gs.getPositiveExamples());
		
		// then go through the correspondences and remove all correct matches from the set
		for(Correspondence<Game, Attribute> c : correspondences.get()) {
			
			// create a pair of both record ids
			Pair<String, String> p1 = new Pair<>(c.getFirstRecord().getIdentifier(), c.getSecondRecord().getIdentifier());
			
			// create a second pair where record1 and record2 are switched 
			// (we don't know in which direction the ids were entered in the gold standard 
			Pair<String, String> p2 = new Pair<>(c.getSecondRecord().getIdentifier(), c.getFirstRecord().getIdentifier());
			
			// check if one of the pairs is in the set of correct matches
			if(allPairs.contains(p1) || allPairs.contains(p2)) {
				// if so, remove it
				allPairs.remove(p1);
				allPairs.remove(p2);
			}
		}
		
		
		// now, the remaining pairs in the set are those that were not found by the matching rule
		// we go through them and print them to the console
		for(Pair<String, String> p : allPairs) {
						
			// get the first record
			Game g1 = ds1.getRecord(p.getFirst());
			if(g1==null) {
				g1 = ds2.getRecord("publisher_id_2");
			}
			
			// get the second record
			Game g2 = ds1.getRecord(p.getSecond());
			if(g2==null) {
				g2 = ds2.getRecord(p.getSecond());
			}
			
			// print both records to the console
			logger.info("[Missing Correspondence]");
			
			if (g1 == null) {
				logger.info(String.format("\t%s", "null"));	
			} else {
				logger.info(String.format("\t%s", g1.getId() ));	
			}
			logger.info(String.format("\t%s", g2.getId() + " / " + g2.getGameTitle()));	
		}
	}
}

	

