package de.uni_mannheim.WDIGroup5.IdentityResolution.fusers;

import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Game;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.Voting;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class GenreFuserVoting extends AttributeValueFuser<String, Game, Attribute> {

    public GenreFuserVoting() {
        super(new Voting<String, Game, Attribute>());
    }

    @Override
    public boolean hasValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Game.GENRE);
    }

    @Override
    public String getValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
        return record.getGenre();
    }

    @Override
    public void fuse(RecordGroup<Game, Attribute> group, Game fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
        FusedValue<String, Game, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
        fusedRecord.setGenre(fused.getValue());
        fusedRecord.setAttributeProvenance(Game.GENRE, fused.getOriginalIds());
    }
}
