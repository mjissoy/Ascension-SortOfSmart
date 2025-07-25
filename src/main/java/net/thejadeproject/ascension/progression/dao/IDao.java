package net.thejadeproject.ascension.progression.dao;

import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Map;

public interface IDao {

    Component getDisplayTitle();
    List<Component> getDescription();


    Double getInteractionValue(IDao dao);

    Map<String,Double> getInteractions();
    Map<String,Double> getRelatedDao();
}
