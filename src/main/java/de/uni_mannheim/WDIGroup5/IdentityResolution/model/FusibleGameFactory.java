package de.uni_mannheim.WDIGroup5.IdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FusibleGameFactory implements FusibleFactory <Game,Attribute> {

    @Override
    public Game createInstanceForFusion(RecordGroup <Game, Attribute> cluster) {

        List<String> ids = new LinkedList<>();

        for (Game g : cluster.getRecords()) {
            ids.add(g.getIdentifier());
        }

        Collections.sort(ids);

        String mergedId = StringUtils.join(ids, '+');

        return new Game(mergedId, "fused");
    }
}
