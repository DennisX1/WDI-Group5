package de.uni_mannheim.WDIGroup5.IdentityResolution.blockers;

import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Game;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.BlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class BlockingByReleaseYearGenerator extends RecordBlockingKeyGenerator<Game, Attribute>  {
	
	@SuppressWarnings("deprecation")
	//SimpleDateFormat df = new SimpleDateFormat("yyyy");
	//year = df.format(date);
	@Override
	public void generateBlockingKeys(Game record, Processable<Correspondence<Attribute, Matchable>> correspondences,
			DataIterator<Pair<String, Game>> resultCollector) {
		resultCollector.next(new Pair<>(Integer.toString(record.getReleaseDate().getYear()), record));
	}
	
}
