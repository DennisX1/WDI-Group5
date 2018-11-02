package de.uni_mannheim.WDIGroup5.IdentityResolution.Comparators;

import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Game;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.date.YearSimilarity;

public class GameReleaseDateComparator2Years implements Comparator<Game,Attribute> {

    private YearSimilarity sim = new YearSimilarity(2);
    private ComparatorLogger comparisonLog;

    @Override
    public double compare(
            Game record1,
            Game record2,
            Correspondence<Attribute, Matchable> schemaCorrespondences) {
//NOT YET DONE
        double similarity =  0; //sim.calculate(record1.getReleaseDate(), record2.getReleaseDate());

        if(this.comparisonLog != null){
            this.comparisonLog.setComparatorName(getClass().getName());

            this.comparisonLog.setRecord1Value(record1.getReleaseDate().toString());
            this.comparisonLog.setRecord2Value(record2.getReleaseDate().toString());

            this.comparisonLog.setSimilarity(Double.toString(similarity));
        }
        return similarity;

    }

    @Override
    public ComparatorLogger getComparisonLog() {
        return this.comparisonLog;
    }

    @Override
    public void setComparisonLog(ComparatorLogger comparatorLog) {
        this.comparisonLog = comparatorLog;
    }

}
