package de.uni_mannheim.WDIGroup5.IdentityResolution.solutions;

import de.uni_mannheim.WDIGroup5.IdentityResolution.blockers.BlockingByGameTitleGenerator;
import de.uni_mannheim.WDIGroup5.IdentityResolution.blockers.BlockingByPlatformGenerator;
import de.uni_mannheim.WDIGroup5.IdentityResolution.blockers.BlockingByPublisherNameGenerator;
import de.uni_mannheim.WDIGroup5.IdentityResolution.blockers.BlockingByReleaseYearGenerator;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.GameTitleComparatorEqual;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.PublisherNameComparatorEqual;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.PublisherNameComparatorJaccard;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.PublisherNameComparatorLevenshtein;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Game;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.GameXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.Blocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.matching.rules.MatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.*;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

import java.io.File;

public class IR_linear_combination_GList_Pub {

    private static long startTime;
    private static long endTime;
    
    private static ErrorAnalysis a = new ErrorAnalysis();

    public static void main(String[] args) throws Exception {


        // possible combinations:
        // GameList - Publisher
        // GameList - Top 1000
        // GameList - VGA
        // Publisher - Top 1000
        // Publisher - VGA
        // Top 1000 - VGA

        //loading data
        System.out.println("*\n*\tLoading datasets\n*");
        HashedDataSet<Game, Attribute> dataPublisher = new HashedDataSet<>();
        new GameXMLReader().loadFromXML(new File("data/input/publisher.xml"), "/Games/Game", dataPublisher);
        HashedDataSet<Game, Attribute> dataGameList = new HashedDataSet<>();
        new GameXMLReader().loadFromXML(new File("data/input/GameList.xml"), "/Games/Game", dataGameList);
        System.out.println("*\n*\tFinished loading the datasets\n*");

        // load the gold standard (training set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTrainingPublisherGamelist = new MatchingGoldStandard();
        gsTrainingPublisherGamelist.loadFromCSVFile(new File("data/goldstandard/GS_publisher_gamelist_testing.csv"));

        startTime = System.nanoTime();
        System.out.println("*\n*\tStart Counting Time\n*");

        //create a matching rule
        LinearCombinationMatchingRule<Game, Attribute> matchingRule = new LinearCombinationMatchingRule<>(1);
        //  matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", -1, gsTraining);

        //add comparators @Anne: PublisherName
        matchingRule.addComparator(new PublisherNameComparatorLevenshtein(),1);   
        

        // create a blocker (blocking strategy)
        System.out.println("*\n*\tStandard Blocker: by publisher\n*");
        Blocker<Game, Attribute, Game, Attribute> blocker = new StandardRecordBlocker<>(new BlockingByPublisherNameGenerator());
        testBlocker(blocker, dataGameList, dataPublisher, matchingRule, gsTrainingPublisherGamelist);
    }


    protected static void testBlocker(Blocker<Game, Attribute, Game, Attribute> blocker, DataSet<Game, Attribute> ds1, DataSet<Game, Attribute> ds2, MatchingRule<Game, Attribute> rule, MatchingGoldStandard gsTest) throws Exception {
        // blocker.setMeasureBlockSizes(true);

        //Write debug results to file
        // blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

        // Initialize Matching Engine
        MatchingEngine<Game, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");
        Processable<Correspondence<Game, Attribute>> correspondences = engine.runIdentityResolution(ds1, ds2, null, rule, blocker);       
       
        // Testing Error Analysis ***** Does not work correctly!
        // ErrorAnalysis analysis = new ErrorAnalysis();
		// testErrorAnalysis(analysis, correspondences, ds1, ds2, rule, gsTest);
		
        
        // Optional!????

//        // Create a top-1 global matching
//        //  correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);
//
//        // Alternative: Create a maximum-weight, bipartite matching
//        // MaximumBipartiteMatchingAlgorithm<Movie,Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondences);
//        // maxWeight.run();
//        // correspondences = maxWeight.getResult();
//
//        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/GList_Publisher_correspondences.csv"), correspondences);
//
//     
        // evaluate your result
        System.out.println("*\n*\tEvaluating result\n*");
        MatchingEvaluator<Game, Attribute> evaluator = new MatchingEvaluator<Game, Attribute>();
        Performance perfTest = evaluator.evaluateMatching(correspondences,
                gsTest);

        // print the evaluation result
        System.out.println("GameList <-> Publisher");
        System.out.println(String.format(
                "Precision: %.4f", perfTest.getPrecision()));
        System.out.println(String.format(
                "Recall: %.4f", perfTest.getRecall()));
        System.out.println(String.format(
                "F1: %.4f", perfTest.getF1()));
    }

    protected static void testErrorAnalysis(ErrorAnalysis analysis, Processable<Correspondence<Game, Attribute>> correspondences,DataSet<Game, Attribute> ds1, DataSet<Game, Attribute> ds2, MatchingRule<Game, Attribute> rule, MatchingGoldStandard gsTest) throws Exception {
    	System.out.println("*\n*\tTesting Error Analysis\n*");
    	analysis.printFalseNegatives(ds1, ds2, correspondences, gsTest);
    	analysis.printFalsePositives(correspondences, gsTest);
       
    }
}
