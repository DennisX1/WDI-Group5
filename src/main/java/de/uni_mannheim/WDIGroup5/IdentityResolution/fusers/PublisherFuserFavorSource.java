package de.uni_mannheim.WDIGroup5.IdentityResolution.fusers;

import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Game;
import de.uni_mannheim.WDIGroup5.IdentityResolution.model.Publisher;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.FavourSources;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.string.ShortestString;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class PublisherFuserFavorSource extends AttributeValueFuser<Publisher, Game, Attribute> {

	public PublisherFuserFavorSource() {
        super(new FavourSources<Publisher, Game, Attribute>());
	}

	  @Override
	    public void fuse(RecordGroup<Game, Attribute> group, Game fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {

	        // get the fused value
	        FusedValue<Publisher, Game, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);

	        // set the value for the fused record
	        fusedRecord.setPublisher(fused.getValue());

	        // add provenance info
	        fusedRecord.setAttributeProvenance(Game.PUBLISHER, fused.getOriginalIds());
	    }

	    @Override
	    public boolean hasValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
	        return record.hasValue(Game.PUBLISHER);
	    }

	    @Override
	    public Publisher getValue(Game record, Correspondence<Attribute, Matchable> correspondence) {
	        return record.getPublisher();
	    }
	}

