package de.uni_mannheim.WDIGroup5.IdentityResolution.solutions;

import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.*;
import de.uni_mannheim.WDIGroup5.IdentityResolution.comparators.GameTitleComparatorEqual;
import de.uni_mannheim.WDIGroup5.IdentityResolution.blockers.*;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Game;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.GameXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.Blocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.MatchingRule;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.*;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class IR_machine_learning {

    /*
     * Logging Options:
     * 		default: 	level INFO	- console
     * 		trace:		level TRACE     - console
     * 		infoFile:	level INFO	- console/file
     * 		traceFile:	level TRACE	- console/file
     *
     * To set the log level to trace and write the log to winter.log and console,
     * activate the "traceFile" logger as follows:
     *     private static final Logger logger = WinterLogManager.activateLogger("traceFile");
     *
     */

    private static final Logger logger = WinterLogManager.activateLogger("default");
	private static long startTime;
	private static long endTime;

    public static void main(String[] args) throws Exception {
        //loading data
        System.out.println("*\n*\tLoading datasets\n*");
        HashedDataSet<Game, Attribute> dataPublisher = new HashedDataSet<>();
        new GameXMLReader().loadFromXML(new File("data/input/publisher.xml"), "/Games/Game", dataPublisher);
        HashedDataSet<Game, Attribute> dataTop1000JapanSales = new HashedDataSet<>();
        new GameXMLReader().loadFromXML(new File("data/input/top1000.xml"), "/Games/Game", dataTop1000JapanSales);
        HashedDataSet<Game, Attribute> dataVgaGames = new HashedDataSet<>();
        new GameXMLReader().loadFromXML(new File("data/input/vga_games_json.xml"), "/Games/Game", dataVgaGames);
        HashedDataSet<Game, Attribute> dataGameList = new HashedDataSet<>();
        new GameXMLReader().loadFromXML(new File("data/input/GameList.xml"), "/Games/Game", dataVgaGames);
        System.out.println("*\n*\tFinished loading the datasets\n*");

		startTime = System.nanoTime();
		System.out.println("*\n*\tStart Counting Time\n*");
		
        // create a matching rule
        String options[] = new String[]{"-S"};
        String modelType = "SimpleLogistic"; // use a logistic regression
        WekaMatchingRule<Game, Attribute> matchingRule = new WekaMatchingRule<>(0.7, modelType, options);
        matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000);

        // add comparators
        matchingRule.addComparator(new GameTitleComparatorEqual());
        matchingRule.addComparator(new GameTitleComparatorJaccard());
        matchingRule.addComparator(new GameTitleComparatorLevenshtein());
        matchingRule.addComparator(new GameGenreComparatorEqual());
        matchingRule.addComparator(new GameGenreComparatorJaccard());
        matchingRule.addComparator(new GameGenreComparatorLevenshtein());
        matchingRule.addComparator(new GamePlatformComparatorEqual());
        matchingRule.addComparator(new GamePlatformComparatorJaccard());
        matchingRule.addComparator(new GamePlatformComparatorLevenshtein());
        matchingRule.addComparator(new GameReleaseDateComparator2Years());
        matchingRule.addComparator(new SalesJapanSalesComparatorAbsolutDiff());
        matchingRule.addComparator(new SalesPriceComparatorAbsolutDiff());
        matchingRule.addComparator(new SalesFirstWeekSalesComparatorAbsolutDiff());
        matchingRule.addComparator(new PublisherNameComparatorEqual());
        matchingRule.addComparator(new PublisherNameComparatorJaccard());
        matchingRule.addComparator(new PublisherNameComparatorLevenshtein());


        // load the training set
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTrainTopGamelist = new MatchingGoldStandard();
        gsTrainTopGamelist.loadFromCSVFile(new File("data/goldstandard/gold_standard_top1000_DS1_train.csv"));

        MatchingGoldStandard gsTrainTopPublisher = new MatchingGoldStandard();
        gsTrainTopPublisher.loadFromCSVFile(new File("data/goldstandard/gold_standard_top1000_publisher_train.csv"));

        MatchingGoldStandard gsTrainTopVga = new MatchingGoldStandard();
        gsTrainTopVga.loadFromCSVFile(new File("data/goldstandard/gold_standard_top1000_vga_train.csv"));

        MatchingGoldStandard gsTrainGamelistVga = new MatchingGoldStandard();
        gsTrainGamelistVga.loadFromCSVFile(new File("data/goldstandard/GS_gamelist_vga_training.csv"));

        MatchingGoldStandard gsTrainPublisherGamelist = new MatchingGoldStandard();
        gsTrainPublisherGamelist.loadFromCSVFile(new File("data/goldstandard/GS_publisher_gamelist_training.csv"));

        MatchingGoldStandard gsTrainVgaPublisher = new MatchingGoldStandard();
        gsTrainVgaPublisher.loadFromCSVFile(new File("data/goldstandard/GS_vga_publisher_training.csv"));

        // train the matching rule's model
        System.out.println("*\n*\tLearning matching rule\n*");
        RuleLearner<Game, Attribute> learner = new RuleLearner<>();
        learner.learnMatchingRule(dataVgaGames, dataGameList, null, matchingRule, gsTrainTopGamelist);
        System.out.println(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));

        // create a blocker (blocking strategy)
        System.out.println("*\n*\tStandard Blocker: by title\n*");
        Blocker<Game, Attribute, Game, Attribute> blocker = new StandardRecordBlocker<>(new BlockingByGameTitleGenerator());

        testBlocker(blocker, dataTop1000JapanSales, dataGameList, matchingRule, gsTrainTopGamelist);

        testBlocker(blocker, dataTop1000JapanSales, dataVgaGames, matchingRule, gsTrainTopVga);

        testBlocker(blocker, dataGameList, dataVgaGames, matchingRule, gsTrainGamelistVga);

        System.out.println("*\n*\tStandard Blocker: by platform\n*");
        blocker = new StandardRecordBlocker<>(new BlockingByPlatformGenerator());

        testBlocker(blocker, dataTop1000JapanSales, dataGameList, matchingRule, gsTrainTopGamelist);

        testBlocker(blocker, dataTop1000JapanSales, dataVgaGames, matchingRule, gsTrainTopVga);

        testBlocker(blocker, dataGameList, dataVgaGames, matchingRule, gsTrainGamelistVga);


        System.out.println("*\n*\tStandard Blocker: by year\n*");
        blocker = new StandardRecordBlocker<>(new BlockingByReleaseYearGenerator());

        testBlocker(blocker, dataTop1000JapanSales, dataGameList, matchingRule, gsTrainTopGamelist);

        testBlocker(blocker, dataTop1000JapanSales, dataVgaGames, matchingRule, gsTrainTopVga);

        testBlocker(blocker, dataGameList, dataVgaGames, matchingRule, gsTrainGamelistVga);


        System.out.println("*\n*\tStandard Blocker: by publisher\n*");
        blocker = new StandardRecordBlocker<>(new BlockingByPublisherNameGenerator());

        testBlocker(blocker, dataTop1000JapanSales, dataGameList, matchingRule, gsTrainTopGamelist);

        testBlocker(blocker, dataTop1000JapanSales, dataVgaGames, matchingRule, gsTrainTopVga);

        testBlocker(blocker, dataGameList, dataVgaGames, matchingRule, gsTrainGamelistVga);

        testBlocker(blocker, dataTop1000JapanSales, dataPublisher, matchingRule, gsTrainTopPublisher);

        testBlocker(blocker, dataPublisher, dataGameList, matchingRule, gsTrainPublisherGamelist);

        testBlocker(blocker, dataVgaGames, dataPublisher, matchingRule, gsTrainVgaPublisher);


    }


    protected static void testBlocker(Blocker<Game, Attribute, Game, Attribute> blocker, DataSet<Game, Attribute> ds1, DataSet<Game, Attribute> ds2, MatchingRule<Game, Attribute> rule, MatchingGoldStandard gsTest) {
        // Initialize Matching Engine
        MatchingEngine<Game, Attribute> engine = new MatchingEngine<>();

        System.out.println("*\n*\tRunning identity resolution\n*");
        // Execute the matching
        Processable<Correspondence<Game, Attribute>> correspondences = engine.runIdentityResolution(ds1, ds2, null, rule, blocker);


        // Optional?
        // write the correspondences to the output file
        // new CSVCorrespondenceFormatter().writeCSV(new File("data/output/academy_awards_2_actors_correspondences.csv"), correspondences);

        // Totally sure not optinonal!

//        // load the gold standard (test set)
//        System.out.println("*\n*\tLoading gold standard\n*");
//        MatchingGoldStandard gsTest = new MatchingGoldStandard();
//        gsTest.loadFromCSVFile(new File(
//                "data/goldstandard/gs_academy_awards_2_actors_test.csv"));


        // evaluate your result
        System.out.println("*\n*\tEvaluating result\n*");
        MatchingEvaluator<Game, Attribute> evaluator = new MatchingEvaluator<Game, Attribute>();
        Performance perfTest = evaluator.evaluateMatching(correspondences,
                gsTest);

        // print the evaluation result
        System.out.println("Academy Awards <-> Actors");
        System.out.println(String.format(
                "Precision: %.4f", perfTest.getPrecision()));
        System.out.println(String.format(
                "Recall: %.4f", perfTest.getRecall()));
        System.out.println(String.format(
                "F1: %.4f", perfTest.getF1()));

		endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		System.out.println("Execution Time: " + totalTime/1000000000);
		
    }
}
