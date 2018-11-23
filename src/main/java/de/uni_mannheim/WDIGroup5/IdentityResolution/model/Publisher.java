package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.io.Serializable;

public class Publisher extends AbstractRecord<Attribute> implements Serializable {

    private String publisherName;
    private String headQuarters;
    private int established;
    private String notableGamesPublished;
    private String notes;


    public Publisher(String identifier, String provenance){
        super(identifier, provenance);
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

    public int getEstablished() {
        return established;
    }

    public void setEstablished(int established) {
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




    public static final Attribute PUBLISHERNAME = new Attribute("PublisherName");
    public static final Attribute HEADQUARTERS = new Attribute("Headquarters");
    public static final Attribute ESTABLISHED = new Attribute("Established");
    public static final Attribute NOTABLEGAMESPUBLISHED = new Attribute("NotableGamesPublished");
    public static final Attribute NOTES = new Attribute("Notes");


    @Override
    public boolean hasValue(Attribute attribute) {

        if(attribute==PUBLISHERNAME)
            return getPublisherName()!=null;
        else if(attribute==HEADQUARTERS)
            return getHeadQuarters()!=null;
        else if(attribute==NOTABLEGAMESPUBLISHED)
            return getNotableGamesPublished()!=null;
        else if(attribute==NOTES)
            return getNotes()!=null;
        return false;
    }

}
