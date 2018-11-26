package de.uni_mannheim.WDIGroup5.IdentityResolution.evaluation;

import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;



public class GenreEvaluationRule extends EvaluationRule<Game, Attribute> {

	SimilarityMeasure<String> sim = new TokenizingJaccardSimilarity();

	@Override
	public boolean isEqual(Game record1, Game record2, Attribute schemaElement) {
		//System.out.println(record1.getGenre());
		//System.out.println(record2.getGenre());
		if(record1.getGenre()==null && record2.getGenre()==null)
			return true;
		else if(record1.getGenre()==null ^ record2.getGenre()==null)
			return true;
		else {
			return sim.calculate(record1.getGenre(), record2.getGenre()) == 1.0;
		}
	}

	@Override
	public boolean isEqual(Game record1, Game record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

		return isEqual(record1, record2, (Attribute)null);
		
	}

	
	
	
}
