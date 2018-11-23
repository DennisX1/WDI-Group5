package de.uni_mannheim.WDIGroup5.IdentityResolution.fusers;

import java.time.LocalDateTime;

import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Game;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Sale;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.MostRecent;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.numeric.Average;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class SaleFuserAverage extends AttributeValueFuser<Double, Game, Attribute> {

    public SaleFuserAverage() {
        super(new Average<Game, Attribute>());
    }

    @Override
    public boolean hasValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Game.SALE);
    }

    @Override
    public Double getValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
        return record.getSales().getJapanSales();
    }

    @Override
    public void fuse(RecordGroup<Game, Attribute> group, Game fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
        FusedValue<Double, Game, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
        //fusedRecord.setSales(new Sale(fusedRecord.getIdentifier(), fusedRecord.getProvenance()));
        fusedRecord.getSales().setJapanSales(fused.getValue());
        fusedRecord.setAttributeProvenance(Game.SALE, fused.getOriginalIds());
    }	
	
}
