package de.uni_mannheim.WDIGroup5.IdentityResolution.solutions;

import de.uni_mannheim.WDIGroup5.IdentityResolution.evaluation.*;
import de.uni_mannheim.WDIGroup5.IdentityResolution.fusers.*;
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
        
        /*
        System.out.println(ds2.getRecord("publisher_id_804"));
        System.out.println(ds2.getRandomRecord());
        System.out.println(ds2.getRandomRecord().getIdentifier());
        System.out.println(ds2.getRandomRecord().getGameTitle());
        System.out.println(ds2.getRandomRecord().getGenre());
        System.out.println(ds2.getRandomRecord().getPublisher());
        System.out.println(ds2.getRandomRecord().getPublisher().getPublisherName());
        */



        FusibleDataSet<Game, Attribute> ds3 = new FusibleHashedDataSet<>();
        new GameXMLReader().loadFromXML(new File("data/input/top1000.xml"), "/Games/Game", ds3);
        ds3.printDataSetDensityReport();

        FusibleDataSet<Game, Attribute> ds4 = new FusibleHashedDataSet<>();
        new GameXMLReader().loadFromXML(new File("data/input/vga_games_json.xml"), "/Games/Game", ds4);
        ds4.printDataSetDensityReport();

        // Maintain Provenance
        // Scores (e.g. from rating)
        //ds1.setScore(2.0);
        //ds2.setScore(2.0);
        ds3.setScore(1.0);
        //ds4.setScore(2.0);


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
        CorrespondenceSet<Game, Attribute> correspondences_game = new CorrespondenceSet<>();
        correspondences_game.loadCorrespondences(new File("data/correspondences/machine_learning_GLIST_T1000_correspondences.csv"),ds3, ds1);
        correspondences_game.loadCorrespondences(new File("data/correspondences/machine_learning_T1000_VGA_correspondences.csv"),ds3, ds4);
        correspondences_game.loadCorrespondences(new File("data/correspondences/machine_learning_GLIST_VGA_correspondences.csv"),ds4, ds1);
        //correspondences_game.loadCorrespondences(new File("data/correspondences/Pub_T1000_correspondences.csv"),ds3);
        //correspondences_game.loadCorrespondences(new File("data/correspondences/Pub_T1000_correspondences.csv"),ds3, ds2);
        
        // write group size distribution
        correspondences_game.printGroupSizeDistribution();

        // load the gold standard
        System.out.println("*\n*\tEvaluating results\n*");
        DataSet<Game, Attribute> gs = new FusibleHashedDataSet<>();
        new GameXMLReader().loadFromXML(new File("data/goldstandard/gs_fusion.xml"), "/Games/Game", gs);

        for(Game m : gs.get()) {
            System.out.println(String.format("gs: %s", m.getIdentifier()));
        }

        // define the fusion strategy
        DataFusionStrategy<Game, Attribute> strategy_game = new DataFusionStrategy<>(new FusibleGameFactory());
        // write debug results to file
        strategy_game.activateDebugReport("data/output/debugResultsDatafusion.csv", -1, gs);

        // add attribute fusers
        strategy_game.addAttributeFuser(Game.GAMETITLE, new TitleFuserVoting(),new GameTitleEvaluationRule());
        strategy_game.addAttributeFuser(Game.GENRE, new GenreFuserVoting(),new GenreEvaluationRule());
        strategy_game.addAttributeFuser(Game.RELEASEDATE, new ReleaseDateFuserFavourSource(),new ReleaseDateEvaluationRule());
        strategy_game.addAttributeFuser(Game.PLATFORM, new PlatformFuserLongestString(),new PlatformEvaluationRule());
        strategy_game.addAttributeFuser(Game.SALES, new SaleFuserVotingAverage(),new SaleEvaluationRule());
        strategy_game.addAttributeFuser(Game.PUBLISHER, new PublisherFuserFavorSource(),new PublisherEvaluationRule());

        
        // create the fusion engine
        DataFusionEngine<Game, Attribute> engine_game = new DataFusionEngine<>(strategy_game);

        
        // print consistency report
        engine_game.printClusterConsistencyReport(correspondences_game, null);

        // print record groups sorted by consistency
        engine_game.writeRecordGroupsByConsistency(new File("data/output/recordGroupConsistencies_Game.csv"), correspondences_game, null);

        
        // run the fusion
        System.out.println("*\n*\tRunning data fusion\n*");
        FusibleDataSet<Game, Attribute> fusedDataSet_game = engine_game.run(correspondences_game, null);

        /*
        System.out.println(fusedDataSet.getRandomRecord());
        System.out.println(fusedDataSet.getRandomRecord().getGameTitle());
        System.out.println(fusedDataSet.getRandomRecord().getPublisher().getPublisherName());
        System.out.println(fusedDataSet.getRandomRecord().getGenre());
        System.out.println(fusedDataSet.getRandomRecord().getReleaseDate());
        System.out.println(fusedDataSet.getRandomRecord().getSales().getJapanSales());
        System.out.println(fusedDataSet.getRandomRecord().getPlatform());
        */
      
        // write the result
        new GameXMLFormatter().writeXML(new File("data/output/fused_game.xml"), fusedDataSet_game);

        
        // evaluate
        DataFusionEvaluator<Game, Attribute> evaluator_game = new DataFusionEvaluator<>(strategy_game);

        
        double accuracy_game = evaluator_game.evaluate(fusedDataSet_game, gs, null);

        System.out.println(String.format("Accuracy: %.2f", accuracy_game));

        /*
        
        FusibleDataSet<Game, Attribute> ds_fused = new FusibleHashedDataSet<>();
        new GameXMLReader().loadFromXML(new File("data/output/fused_game.xml"), "/Games/Game", ds_fused);
        
        FusibleDataSet<Game, Attribute> ds_fused_jpn = new FusibleHashedDataSet<>();
        FusibleDataSet<Game, Attribute> ds_fused_gamelist = new FusibleHashedDataSet<>();
        FusibleDataSet<Game, Attribute> ds_fused_vga = new FusibleHashedDataSet<>();

        for(Game record : ds_fused.get()) {
        	if(record.getIdentifier().contains("+")) {
        		record.setIdentifier(record.getIdentifier().split("\\+")[0]);
        	}
        	else {
                System.out.println(record.getIdentifier());
        	}
        	if(record.getIdentifier().contains("jpntop")) {
        		ds_fused_jpn.add(record);
        	}
        	else if(record.getIdentifier().contains("Game_List")) {
        		ds_fused_gamelist.add(record);
        	}
        	else if(record.getIdentifier().contains("vga")) {
        		ds_fused_vga.add(record);
        	}
        	else {
                System.out.println(record.getIdentifier());
        	}
        }

      
        CorrespondenceSet<Game, Attribute> correspondences_publisher = new CorrespondenceSet<>();
        
        if(ds_fused_jpn.size() != 0) {
            System.out.println(ds_fused_jpn.size());
            System.out.println(ds_fused_jpn.getRandomRecord());
            System.out.println(ds_fused_jpn.getRandomRecord().getGameTitle());
            System.out.println(ds_fused_jpn.getRandomRecord().getIdentifier());
            //System.out.println(ds_fused_jpn.getNumberOfValues(ds_fused_jpn.getRandomRecord()));
            //ds_fused_jpn.printDataSetDensityReport();
            correspondences_publisher.loadCorrespondences(new File("data/correspondences/Pub_T1000_correspondences.csv"),ds_fused_jpn, ds2);
        }
        
        if(ds_fused_gamelist.size() != 0) {
            System.out.println(ds_fused_gamelist.size());
            //ds_fused_gamelist.printDataSetDensityReport();
            correspondences_publisher.loadCorrespondences(new File("data/correspondences/GList_Publisher_correspondences.csv"),ds2, ds_fused_gamelist);
        }
        
        if(ds_fused_vga.size() != 0) {
            System.out.println(ds_fused_vga.size());
            //ds_fused_vga.printDataSetDensityReport();
            correspondences_publisher.loadCorrespondences(new File("data/correspondences/Pub_VGA_correspondences.csv"),ds_fused_vga, ds2);
        }   
        
        correspondences_publisher.printGroupSizeDistribution();

        DataFusionStrategy<Game, Attribute> strategy_publisher = new DataFusionStrategy<>(new FusibleGameFactory());
        strategy_publisher.addAttributeFuser(Game.GAMETITLE, new TitleFuserVoting(),new GameTitleEvaluationRule());
        strategy_publisher.addAttributeFuser(Game.GENRE, new GenreFuserVoting(),new GenreEvaluationRule());
        strategy_publisher.addAttributeFuser(Game.RELEASEDATE, new ReleaseDateFuserFavourSource(),new ReleaseDateEvaluationRule());
        strategy_publisher.addAttributeFuser(Game.PLATFORM, new PlatformFuserVoting(),new PlatformEvaluationRule());
        strategy_publisher.addAttributeFuser(Game.SALES, new SaleFuserVotingAverage(),new SaleEvaluationRule());
        strategy_publisher.addAttributeFuser(Game.PUBLISHER, new PublisherFuserFavorSource(),new PublisherEvaluationRule());
        
        DataFusionEngine<Game, Attribute> engine_publisher = new DataFusionEngine<>(strategy_publisher);
        engine_publisher.printClusterConsistencyReport(correspondences_publisher, null);
        engine_publisher.writeRecordGroupsByConsistency(new File("data/output/recordGroupConsistencies_Publisher.csv"), correspondences_publisher, null);
   
        FusibleDataSet<Game, Attribute> fusedDataSet_publisher = engine_publisher.run(correspondences_publisher, null);
        new GameXMLFormatter().writeXML(new File("data/output/fused_with_publisher.xml"), fusedDataSet_publisher);
        
        DataFusionEvaluator<Game, Attribute> evaluator_publisher = new DataFusionEvaluator<>(strategy_publisher);
        double accuracy_publisher = evaluator_publisher.evaluate(fusedDataSet_publisher, gs, null);

        System.out.println(String.format("Accuracy: %.2f", accuracy_publisher));
      
        //System.out.println(ds_fused.size());
        //System.out.println(ds_fused_jpn.size());
        //System.out.println(ds_fused_gamelist.size());
        //System.out.println(ds_fused_vga.size());
        */
        
    }
}
