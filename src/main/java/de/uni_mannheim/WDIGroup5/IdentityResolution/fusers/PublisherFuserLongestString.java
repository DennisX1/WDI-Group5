package de.uni_mannheim.WDIGroup5.IdentityResolution.fusers;

import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Game;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Publisher;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.string.LongestString;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.string.ShortestString;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class PublisherFuserLongestString extends AttributeValueFuser<String, Game, Attribute> {

	public PublisherFuserLongestString() {
        super(new LongestString<Game, Attribute>());
	}

	  @Override
	    public void fuse(RecordGroup<Game, Attribute> group, Game fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {

	        // get the fused value
	        FusedValue<String, Game, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);

	        // set the value for the fused record
	        fusedRecord.setPublisher(new Publisher(fusedRecord.getIdentifier(), fusedRecord.getProvenance()));
	        fusedRecord.getPublisher().setPublisherName(fused.getValue());
	        // add provenance info
	        fusedRecord.setAttributeProvenance(Game.PUBLISHER, fused.getOriginalIds());
	    }

	    @Override
	    public boolean hasValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
	        return record.hasValue(Game.PUBLISHER);
	    }

	    @Override
	    public String getValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
	        return record.getPublisher().getPublisherName();
	    }
	}



