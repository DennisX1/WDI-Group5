package de.uni_mannheim.WDIGroup5.IdentityResolution.solutions;

import java.io.File;

import de.uni_mannheim.WDIGroup5.IdentityResolution.blockers.BlockingByGameTitleGenerator;
import de.uni_mannheim.WDIGroup5.IdentityResolution.blockers.BlockingByPublisherNameGenerator;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.GameGenreComparatorEqual;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.GameGenreComparatorJaccard;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.GameGenreComparatorLevenshtein;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.GamePlatformComparatorEqual;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.GamePlatformComparatorJaccard;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.GamePlatformComparatorLevenshtein;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.GameReleaseDateComparator2Years;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.GameTitleComparatorEqual;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.GameTitleComparatorJaccard;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.GameTitleComparatorLevenshtein;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.PublisherNameComparatorEqual;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.PublisherNameComparatorJaccard;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.PublisherNameComparatorLevenshtein;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Game;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.GameXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.MatchingRule;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class IR_machine_learning_GList_Pub {

    public static void main(String[] args) throws Exception {

	 //loading data
    System.out.println("*\n*\tLoading datasets\n*");
    HashedDataSet<Game, Attribute> dataGameList = new HashedDataSet<>();
    new GameXMLReader().loadFromXML(new File("data/input/GameList.xml"), "/Games/Game", dataGameList);
    HashedDataSet<Game, Attribute> dataPublisher = new HashedDataSet<>();
    new GameXMLReader().loadFromXML(new File("data/input/publisher.xml"), "/Games/Game", dataPublisher);
    System.out.println("*\n*\tFinished loading the datasets\n*");
        
    // create a matching rule
    String options[] = new String[]{"-S"};
    String modelType = "SimpleLogistic"; // use a logistic regression
    WekaMatchingRule<Game, Attribute> matchingRule = new WekaMatchingRule<>(0.7, modelType, options);
//    matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000);
    
    matchingRule.addComparator(new PublisherNameComparatorEqual());
    matchingRule.addComparator(new PublisherNameComparatorJaccard());
    matchingRule.addComparator(new PublisherNameComparatorLevenshtein());
    
    System.out.println("*\n*\tLoading gold standard - training\n*");
    MatchingGoldStandard gsTrainGListPub = new MatchingGoldStandard();
    gsTrainGListPub.loadFromCSVFile(new File("data/goldstandard/GS_publisher_gamelist_training.csv"));

    System.out.println("*\n*\tLoading gold standard - testing\n*");
    MatchingGoldStandard gsTestGListPub = new MatchingGoldStandard();
    gsTestGListPub.loadFromCSVFile(new File("data/goldstandard/GS_publisher_gamelist_testing.csv"));

	System.out.println("*\n*\tLearning matching rule\n*");
	RuleLearner<Game, Attribute> learner = new RuleLearner<>();
	learner.learnMatchingRule(dataPublisher, dataGameList, null, matchingRule, gsTrainGListPub);
	System.out.println(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));
    
	
	StandardRecordBlocker<Game, Attribute> blocker = new StandardRecordBlocker<Game, Attribute>(new BlockingByPublisherNameGenerator());
//	blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);
	
	// Initialize Matching Engine
	MatchingEngine<Game, Attribute> engine = new MatchingEngine<>();

	// Execute the matching
	System.out.println("*\n*\tRunning identity resolution\n*");
	Processable<Correspondence<Game, Attribute>> correspondences = engine.runIdentityResolution(
			dataPublisher, dataGameList, null, matchingRule,
			blocker);
	
	// Testing Error Analysis - unfinished
//	System.out.println("*\n*\tTesting Error Analysis\n*");
//    ErrorAnalysis analysis = new ErrorAnalysis();
//	analysis.printFalseNegatives(dataVgaGames, dataGameList, correspondences, gsTest);
//    analysis.printFalsePositives(correspondences, gsTrainTopGamelistTest);
       
	
	System.out.println("*\n*\tRunning global matching\n*");

	// Create a top-1 global matching
	//  correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);

	// Alternative: Create a maximum-weight, bipartite matching
	//MaximumBipartiteMatchingAlgorithm<Movie,Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondences);
	//maxWeight.run();
	//correspondences = maxWeight.getResult();

	// evaluate your result
	System.out.println("*\n*\tEvaluating result\n*");
	MatchingEvaluator<Game, Attribute> evaluator = new MatchingEvaluator<Game, Attribute>();
	Performance perfTest = evaluator.evaluateMatching(correspondences,
			gsTestGListPub);
	
	// print the evaluation result
	System.out.println("GameList <-> VGA");
	System.out.println(String.format(
			"Precision: %.4f",perfTest.getPrecision()));
	System.out.println(String.format(
			"Recall: %.4f",	perfTest.getRecall()));
	System.out.println(String.format(
			"F1: %.4f",perfTest.getF1()));


	// write the correspondences to the output file
	new CSVCorrespondenceFormatter().writeCSV(new File("data/output/machine_learning_GLIST_Pub_correspondences.csv"), correspondences);
	

	
	
	
    }
    
    
}
