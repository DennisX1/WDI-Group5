package de.uni_mannheim.WDIGroup5.IdentityResolution.solutions;

import de.uni_mannheim.WDIGroup5.IdentityResolution.fusers.TitleFuserShortestString;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.FusibleGameFactory;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Game;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.GameXMLFormatter;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.GameXMLReader;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEngine;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEvaluator;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionStrategy;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

public class Fusion {


    public static void main( String[] args ) throws Exception {


        // Load the Data into FusibleDataSet
        System.out.println("*\n*\tLoading datasets\n*");
        FusibleDataSet<Game, Attribute> ds1 = new FusibleHashedDataSet<>();
        new GameXMLReader().loadFromXML(new File("data/input/GameList.xml"), "/Games/Game", ds1);
        ds1.printDataSetDensityReport();

        FusibleDataSet<Game, Attribute> ds2 = new FusibleHashedDataSet<>();
        new GameXMLReader().loadFromXML(new File("data/input/publisher.xml"), "/Games/Game", ds2);
        ds2.printDataSetDensityReport();

        FusibleDataSet<Game, Attribute> ds3 = new FusibleHashedDataSet<>();
        new GameXMLReader().loadFromXML(new File("data/input/top1000.xml"), "/Games/Game", ds3);
        ds3.printDataSetDensityReport();

        FusibleDataSet<Game, Attribute> ds4 = new FusibleHashedDataSet<>();
        new GameXMLReader().loadFromXML(new File("data/input/vga_games_json.xml"), "/Games/Game", ds4);
        ds4.printDataSetDensityReport();

        // Maintain Provenance
        // Scores (e.g. from rating)
        ds1.setScore(1.0);
        ds2.setScore(2.0);
        ds3.setScore(3.0);


          //Date (e.g. last update)
//        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
//                .appendPattern("yyyy-MM-dd")
//                .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
//                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
//                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
//                .toFormatter(Locale.ENGLISH);
//
//        ds1.setDate(LocalDateTime.parse("2012-01-01", formatter));
//        ds2.setDate(LocalDateTime.parse("2010-01-01", formatter));
//        ds3.setDate(LocalDateTime.parse("2008-01-01", formatter));


        // load correspondences
        System.out.println("*\n*\tLoading correspondences\n*");
        CorrespondenceSet<Game, Attribute> correspondences = new CorrespondenceSet<>();
        System.out.println(ds2.getRecord("publisher_id_804"));
        //correspondences.loadCorrespondences(new File("data/correspondences/GList_Publisher_correspondences.csv"),ds2, ds1);
        //correspondences.loadCorrespondences(new File("data/correspondences/GList_VGA_correspondences.csv"),ds1, ds4);
        //correspondences.loadCorrespondences(new File("data/correspondences/machine_learning_GLIST_T1000_correspondences.csv"),ds3, ds1);
        //correspondences.loadCorrespondences(new File("data/correspondences/Pub_T1000_correspondences.csv"),ds3, ds2);
        //correspondences.loadCorrespondences(new File("data/correspondences/Pub_VGA_correspondences.csv"),ds4, ds2);
        //correspondences.loadCorrespondences(new File("data/correspondences/T1000_VGA_correspondences.csv"),ds3, ds4);


        // write group size distribution
        correspondences.printGroupSizeDistribution();


        // load the gold standard
        System.out.println("*\n*\tEvaluating results\n*");
        DataSet<Game, Attribute> gs = new FusibleHashedDataSet<>();
        new GameXMLReader().loadFromXML(new File("data/goldstandard/gs_fusion.xml"), "/Games/Game", gs);

        for(Game m : gs.get()) {
            System.out.println(String.format("gs: %s", m.getIdentifier()));
        }

        // define the fusion strategy
        DataFusionStrategy<Game, Attribute> strategy = new DataFusionStrategy<>(new FusibleGameFactory());
        // write debug results to file
        strategy.activateDebugReport("data/output/debugResultsDatafusion.csv", -1, gs);

        // add attribute fusers
        // strategy.addAttributeFuser(Game.GAMETITLE, new TitleFuserShortestString(),new TitleEvaluationRule());


        // create the fusion engine
        DataFusionEngine<Game, Attribute> engine = new DataFusionEngine<>(strategy);

        // print consistency report
        engine.printClusterConsistencyReport(correspondences, null);

        // print record groups sorted by consistency
        engine.writeRecordGroupsByConsistency(new File("data/output/recordGroupConsistencies.csv"), correspondences, null);

        // run the fusion
        System.out.println("*\n*\tRunning data fusion\n*");
        FusibleDataSet<Game, Attribute> fusedDataSet = engine.run(correspondences, null);

        // write the result
        new GameXMLFormatter().writeXML(new File("data/output/fused.xml"), fusedDataSet);

        // evaluate
        DataFusionEvaluator<Game, Attribute> evaluator = new DataFusionEvaluator<>(strategy);

        double accuracy = evaluator.evaluate(fusedDataSet, gs, null);

        System.out.println(String.format("Accuracy: %.2f", accuracy));


    }
}
