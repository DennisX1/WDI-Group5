package de.uni_mannheim.WDIGroup5.IdentityResolution.solutions;

import de.uni_mannheim.WDIGroup5.IdentityResolution.blockers.BlockingByGameTitleGenerator;
import de.uni_mannheim.WDIGroup5.IdentityResolution.blockers.BlockingByPlatformGenerator;
import de.uni_mannheim.WDIGroup5.IdentityResolution.blockers.BlockingByPublisherNameGenerator;
import de.uni_mannheim.WDIGroup5.IdentityResolution.blockers.BlockingByReleaseYearGenerator;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.*;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Game;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.GameXMLReader;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
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

public class IR_linear_combination_Pub_VGA {

    private static long startTime;
    private static long endTime;

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
        HashedDataSet<Game, Attribute> dataVgaGames = new HashedDataSet<>();
        new GameXMLReader().loadFromXML(new File("data/input/vga_games_json.xml"), "/Games/Game", dataVgaGames);
        System.out.println("*\n*\tFinished loading the datasets\n*");

        // load the gold standard (training set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTestVgaPublisher = new MatchingGoldStandard();
        gsTestVgaPublisher.loadFromCSVFile(new File("data/goldstandard/GS_vga_publisher_training.csv"));

        startTime = System.nanoTime();
        System.out.println("*\n*\tStart Counting Time\n*");

        //create a matching rule
        LinearCombinationMatchingRule<Game, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0.7);
        matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", -1, gsTestVgaPublisher);

        //add comparators Publisher Name
        matchingRule.addComparator(new PublisherNameComparatorJaccard(), 1);

        // create a blocker (blocking strategy)
        System.out.println("*\n*\tStandard Blocker: by publisher\n*");

        Blocker<Game, Attribute, Game, Attribute> blocker = new StandardRecordBlocker<>(new BlockingByPublisherNameGenerator());
        testBlocker(blocker, dataVgaGames, dataPublisher, matchingRule, gsTestVgaPublisher);

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
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/Pub_VGA_correspondences.csv"), correspondences);
//
//        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTest_pub_vga = new MatchingGoldStandard();
        gsTest_pub_vga.loadFromCSVFile(new File(
                "data/goldstandard/GS_vga_publisher_testing.csv"));


        // evaluate your result
        System.out.println("*\n*\tEvaluating result\n*");
        MatchingEvaluator<Game, Attribute> evaluator = new MatchingEvaluator<Game, Attribute>();
        Performance perfTest = evaluator.evaluateMatching(correspondences,
                gsTest_pub_vga);
        // print the evaluation result
        System.out.println("Publisher <-> VGA");
        System.out.println(String.format(
                "Precision: %.4f", perfTest.getPrecision()));
        System.out.println(String.format(
                "Recall: %.4f", perfTest.getRecall()));
        System.out.println(String.format(
                "F1: %.4f", perfTest.getF1()));

        endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Execution Time: " + totalTime / 1000000000);
    }
}
