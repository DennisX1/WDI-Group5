package de.uni_mannheim.WDIGroup5.IdentityResolution.evaluation;

import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

public class SaleEvaluationRule extends EvaluationRule<Game, Attribute> {

	@Override
	public boolean isEqual(Game record1, Game record2, Attribute schemaElement) {
		// the title is correct if all tokens are there, but the order does not
		// matter
		
		//threshold: 2% deviation from the mean value
		return ((record1.getSales().getJapanSales() - record2.getSales().getJapanSales()) <= (record1.getSales().getJapanSales() + record2.getSales().getJapanSales()) / 2 * 0.02);
	}

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.datafusion.EvaluationRule#isEqual(java.lang.Object, java.lang.Object, de.uni_mannheim.informatik.wdi.model.Correspondence)
	 */
	@Override
	public boolean isEqual(Game record1, Game record2,
			Correspondence<Attribute, Matchable> schemaCorrespondence) {
		return isEqual(record1, record2, (Attribute)null);
	}
	
}
