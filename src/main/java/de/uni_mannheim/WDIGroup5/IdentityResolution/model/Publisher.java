package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.io.Serializable;

public class Publisher extends AbstractRecord<Attribute> implements Serializable {

    private String publisherName;
    private String headQuarters;
    private double established;
    private String notableGamesPublished;
    private String notes;



    @Override
    public boolean hasValue(Attribute attribute) {
        return false;
    }




    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getHeadQuarters() {
        return headQuarters;
    }

    public void setHeadQuarters(String headQuarters) {
        this.headQuarters = headQuarters;
    }

    public double getEstablished() {
        return established;
    }

    public void setEstablished(double established) {
        this.established = established;
    }

    public String getNotableGamesPublished() {
        return notableGamesPublished;
    }

    public void setNotableGamesPublished(String notableGamesPublished) {
        this.notableGamesPublished = notableGamesPublished;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
