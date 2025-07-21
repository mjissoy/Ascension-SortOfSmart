package net.thejadeproject.ascension.progression.dao;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;
import java.util.Map;

public interface IDao {

    Component getDisplayTitle();
    List<MutableComponent> getDescription();


    Double getInteractionValue(IDao dao);

    Map<String,Double> getInteractions();
    Map<String,Double> getRelatedDao();
}
