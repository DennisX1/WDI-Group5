package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.management.PlatformLoggingMXBean;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class Game extends AbstractRecord<Attribute> implements Serializable {

    //protected String id;
    private String gameTitle;
    private String genre;
    private String platform;
    private LocalDateTime releaseDate;
    private Publisher publisher;
    private Sale sales;

    /*
     * This is the main class, we also use the invocation of the GameXMLReader to load and parse the data
     * GameXMLReader invocates the other readers to create a full game entity of all datasets
     * Afterwards we start comparing specific attributes!
     */

    public Game(String identifier, String provenance) {
        super(identifier,provenance);
    }


    @Override
    public String getIdentifier() {
        return super.id;
    }

    @Override
    public String getProvenance() {
        return null;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public Publisher getPublisher() {
        return this.publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
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

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Sale getSales() {
        return sales;
    }

    public void setSales(Sale sales) {
        this.sales = sales;
    }


    private Map<Attribute, Collection<String>> provenance = new HashMap<>();
    private Collection<String> recordProvenance;

    public void setRecordProvenance(Collection<String> provenance) {
        recordProvenance = provenance;
    }

    public Collection<String> getRecordProvenance() {
        return recordProvenance;
    }

    public void setAttributeProvenance(Attribute attribute,
                                       Collection<String> provenance) {
        this.provenance.put(attribute, provenance);
    }

    public Collection<String> getAttributeProvenance(String attribute) {
        return provenance.get(attribute);
    }

    public String getMergedAttributeProvenance(Attribute attribute) {
        Collection<String> prov = provenance.get(attribute);

        if (prov != null) {
            return StringUtils.join(prov, "+");
        } else {
            return "";
        }
    }

    public static final Attribute GAMETITLE = new Attribute("GameTitle");
    public static final Attribute GENRE = new Attribute("Genre");
    public static final Attribute PLATFORM = new Attribute("Platform");
    public static final Attribute RELEASEDATE = new Attribute("ReleaseDate");
    public static final Attribute PUBLISHER = new Attribute("Publisher");
    public static final Attribute SALE = new Attribute("Sale");


    @Override
    public boolean hasValue(Attribute attribute) {
        if (attribute == GAMETITLE)
            return getGameTitle() != null && !getGameTitle().isEmpty();
        else if (attribute == GENRE)
            return getGenre() != null && !getGenre().isEmpty();
        else if (attribute == RELEASEDATE)
            return getReleaseDate() != null;
        else if (attribute == PLATFORM)
            return getPlatform() != null && getPlatform().isEmpty();
        else if (attribute == PUBLISHER)
            return getPublisher() != null && !getPublisher().getPublisherName().isEmpty();
        else if (attribute == SALE)
            return getSales() != null && getSales().getJapanSales() != 0;
        else
            return false;
    }




    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Game) {
            return this.getIdentifier().equals(((Game) obj).getIdentifier());
        } else
            return false;
    }


}
