package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.Matchable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Game implements Matchable {

protected String id;
protected String provenance;
private String gameTitle;
private String genre;
private String platform;
private Date releaseDate;
private List <Publisher> publisherList;
private List <Sale> salesList;

/*
 * This is the main class, we also use the invocation of the GameXMLReader to load and parse the data
 * GameXMLReader invocates the other readers to create a full game entity of all datasets
 * Afterwards we start comparing specific attributes!
 */





    public Game (String identifier, String provenance){
        this.id = id;
        this.provenance = provenance;

    }





    @Override
    public String getIdentifier() {
        return this.id;
    }

    @Override
    public String getProvenance() {
        return null;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public List<Publisher> getPublisherList() {
        return publisherList;
    }

    public void setPublisherList(List<Publisher> publisherList) {
        this.publisherList = publisherList;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Sale> getSalesList() {
        return salesList;
    }

    public void setSalesList(List<Sale> salesList) {
        this.salesList = salesList;
    }




}
