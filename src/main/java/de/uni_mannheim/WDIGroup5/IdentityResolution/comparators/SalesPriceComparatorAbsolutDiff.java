package de.uni_mannheim.WDIGroup5.IdentityResolution.comparators;

import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Game;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.AbsoluteDifferenceSimilarity;

public class SalesPriceComparatorAbsolutDiff implements Comparator<Game,Attribute> {


    private AbsoluteDifferenceSimilarity sim = new AbsoluteDifferenceSimilarity(100000);
    private ComparatorLogger comparisonLog;


    @Override
    public double compare(Game record1, Game record2, Correspondence<Attribute, Matchable> correspondence) {
        double s1 = record1.getSales().getPrice();
        double s2 = record2.getSales().getPrice();

        double similarity = sim.calculate(s1, s2);

        if (this.comparisonLog != null) {
            this.comparisonLog.setComparatorName(getClass().getName());

            this.comparisonLog.setRecord1Value(Double.toString(s1));
            this.comparisonLog.setRecord2Value(Double.toString(s2));

            this.comparisonLog.setSimilarity(Double.toString(similarity));
        }

        return similarity;
    }

    @Override
    public Attribute getFirstSchemaElement(Game record) {
        return null;
    }

    @Override
    public Attribute getSecondSchemaElement(Game record) {
        return null;
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
