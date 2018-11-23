package de.uni_mannheim.WDIGroup5.IdentityResolution.evaluation;

import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;



public class ReleaseDateEvaluationRule extends EvaluationRule <Game, Attribute>{

	@Override
	public boolean isEqual(Game record1, Game record2, Attribute schemaElement) {

		if(record1.getReleaseDate()==null && record2.getReleaseDate()==null)
			return true;
		else if(record1.getReleaseDate()==null ^ record2.getReleaseDate()==null)
			return false;
		else
			//Returns only true, if Year, Month and Day is equal!
			return (
					(record1.getReleaseDate().getYear() == record2.getReleaseDate().getYear()) 
				   && (record1.getReleaseDate().getMonth() == record2.getReleaseDate().getMonth())
				   && (record1.getReleaseDate().getDayOfMonth() == record2.getReleaseDate().getDayOfMonth())			
			);
			
		
	}

	@Override
	public boolean isEqual(Game record1, Game record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}
