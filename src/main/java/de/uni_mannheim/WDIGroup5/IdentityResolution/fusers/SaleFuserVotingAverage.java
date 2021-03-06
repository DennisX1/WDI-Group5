package de.uni_mannheim.WDIGroup5.IdentityResolution.fusers;

import java.awt.List;
import java.time.LocalDateTime;
import java.util.Collection;

import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Game;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Sale;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.FavourSources;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.MostRecent;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.numeric.Average;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class SaleFuserVotingAverage extends AttributeValueFuser<Sale, Game, Attribute> {

    public SaleFuserVotingAverage() {
        super(new FavourSources<Sale, Game, Attribute>());
    }

    @Override
    public boolean hasValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Game.SALES);
    }

    @Override
    public Sale getValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
        return record.getSales();
    }

    @Override
    public void fuse(RecordGroup<Game, Attribute> group, Game fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
    	FusedValue<Sale, Game, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
    	
    	fusedRecord.setSales(fused.getValue()); 
    	
    	SaleFuserAverage averageFuser = new SaleFuserAverage();
    	averageFuser.fuse(group, fusedRecord, schemaCorrespondences, schemaElement);
    	
    	fusedRecord.setAttributeProvenance(Game.SALES, group.getRecordIds());

    }	
	
}
