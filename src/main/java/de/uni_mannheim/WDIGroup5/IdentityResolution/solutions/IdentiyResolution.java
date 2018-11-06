package de.uni_mannheim.WDIGroup5.IdentityResolution.solutions;

import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.*;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.*;
import java.io.File;

import org.apache.logging.log4j.Logger;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Game;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.GameXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.MaximumBipartiteMatchingAlgorithm;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.Blocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.matching.rules.MatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Group;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.processing.RecordKeyValueMapper;
import de.uni_mannheim.informatik.dws.winter.utils.Distribution;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;



public class IdentiyResolution {


    public static void main( String[] args ) throws Exception
	    {
		
		System.out.println("*\n*\tLoading datasets\n*");
		HashedDataSet<Game, Attribute> dataPublisher = new HashedDataSet<>();
		new GameXMLReader().loadFromXML(new File("data/input/publisher.xml"), "/Games/Game", dataPublisher);
		HashedDataSet<Game, Attribute> dataTop1000JapanSales = new HashedDataSet<>();
		new GameXMLReader().loadFromXML(new File("data/input/top1000.xml"), "/Games/Game", dataTop1000JapanSales);
		HashedDataSet<Game, Attribute> dataVgaGames = new HashedDataSet<>();
		new GameXMLReader().loadFromXML(new File("data/input/vga_games_json.xml"), "/Games/Game", dataVgaGames);
		HashedDataSet<Game, Attribute> dataGameList = new HashedDataSet<>();
		new GameXMLReader().loadFromXML(new File("data/input/GameList.xml"), "/Games/Game", dataGameList);
		System.out.println("*\n*\tFinished loading the datasets\n*");
		
		
		
		// load the gold standard (test set)
		System.out.println("*\n*\tLoading gold standard\n*");
		MatchingGoldStandard gsTestTopGamelist = new MatchingGoldStandard();
		gsTestTopGamelist.loadFromCSVFile(new File("/IdentityRes/data/goldstandard/gold_standard_top1000_DS1_test.csv"));

		MatchingGoldStandard gsTestTopPublisher = new MatchingGoldStandard();
		gsTestTopPublisher.loadFromCSVFile(new File("/IdentityRes/data/goldstandard/gold_standard_top1000_publisher_test.csv"));
		
		MatchingGoldStandard gsTestTopVga = new MatchingGoldStandard();
		gsTestTopVga.loadFromCSVFile(new File("/IdentityRes/data/goldstandard/gold_standard_top1000_vga_test.csv"));
		
		MatchingGoldStandard gsTestGamelistVga = new MatchingGoldStandard();
		gsTestGamelistVga.loadFromCSVFile(new File("/IdentityRes/data/goldstandard/GS_gamelist_vga_testing.csv"));
		
		MatchingGoldStandard gsTestPublisherGamelist = new MatchingGoldStandard();
		gsTestPublisherGamelist.loadFromCSVFile(new File("/IdentityRes/data/goldstandard/GS_publisher_gamelist_testing.csv"));
		
		MatchingGoldStandard gsTestVgaPublisher = new MatchingGoldStandard();
		gsTestVgaPublisher.loadFromCSVFile(new File("/IdentityRes/data/goldstandard/GS_vga_publisher_testing.csv"));
		
		
		// create a matching rule
		LinearCombinationMatchingRule<Game, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0.7);
		
		// add comparators
		matchingRule.addComparator(new GameTitleComparatorJaccard(), 1.0);
		
	/*
		// create a blocker (blocking strategy)
		Blocker<Game,Attribute,Game,Attribute> blocker = new StandardRecordBlocker<>(new GameBlockingKeyByTitleGenerator());
		
		System.out.println("*\n*\tStandard Blocker: by title\n*");
		testBlocker(blocker, dataAcademyAwards, dataActors, matchingRule, gsTest);

		System.out.println("*\n*\tStandard Blocker: by decade\n*");
		blocker = new StandardRecordBlocker<>(new GameBlockingKeyByDecadeGenerator());
		testBlocker(blocker, dataAcademyAwards, dataActors, matchingRule, gsTest);
	*/
		
	    }
	
    
    
    
	protected static void testBlocker(Blocker<Game,Attribute,Game,Attribute> blocker, DataSet<Game,Attribute> ds1, DataSet<Game,Attribute> ds2, MatchingRule<Game,Attribute> rule, MatchingGoldStandard gsTest) {
		// Initialize Matching Engine
		MatchingEngine<Game, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		Processable<Correspondence<Game, Attribute>> correspondences = engine.runIdentityResolution(ds1, ds2, null, rule,blocker);

		// evaluate your result
		System.out.println("*\n*\tEvaluating result\n*");
		MatchingEvaluator<Game, Attribute> evaluator = new MatchingEvaluator<Game, Attribute>();
		Performance perfTest = evaluator.evaluateMatching(correspondences,
			gsTest);

		// print the evaluation result
		System.out.println("Academy Awards <-> Actors");
		System.out.println(String.format(
			"Precision: %.4f",perfTest.getPrecision()));
		System.out.println(String.format(
			"Recall: %.4f",	perfTest.getRecall()));
		System.out.println(String.format(
			"F1: %.4f",perfTest.getF1()));
	}
    
	
}
