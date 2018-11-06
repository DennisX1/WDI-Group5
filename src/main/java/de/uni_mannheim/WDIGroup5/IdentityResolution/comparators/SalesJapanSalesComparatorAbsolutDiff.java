package de.uni_mannheim.WDIGroup5.IdentityResolution.comparators;

import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Sale;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.AbsoluteDifferenceSimilarity;

public class SalesJapanSalesComparatorAbsolutDiff implements Comparator<Sale, Attribute> {

    private AbsoluteDifferenceSimilarity sim = new AbsoluteDifferenceSimilarity(100000);
    private ComparatorLogger comparisonLog;

    @Override
    public double compare(Sale record1, Sale record2, Correspondence<Attribute, Matchable> schemaCorrespondences) {

        double s1 = record1.getJapanSales();
        double s2 = record2.getJapanSales();

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
    public ComparatorLogger getComparisonLog() {
        return this.comparisonLog;
    }

    @Override
    public void setComparisonLog(ComparatorLogger comparatorLog) {
        this.comparisonLog = comparatorLog;
    }
}
