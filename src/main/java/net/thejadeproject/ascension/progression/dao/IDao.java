package net.thejadeproject.ascension.progression.dao;

import net.minecraft.network.chat.Component;
import net.thejadeproject.ascension.registries.AscensionRegistries;

import java.util.List;
import java.util.Map;

public interface IDao {

    Component getDisplayTitle();
    List<Component> getDescription();



    //============= GETTING VALUES ===============
    //note this is the raw INTERACTION value. each one is used slightly differently
    // eg when using destructive value also * dao multiplier/event dao multiplier

    default Double getDestructiveValue(IDao dao) {
        return getDestructiveValue(AscensionRegistries.Dao.DAO_REGISTRY.getKey(dao).toString());
    }
    default Double getDestructiveValue(String dao){
        return getDestructiveDao().get(dao);
    }

    default Double getGenerativeValue(IDao dao) {
        return getGenerativeValue(AscensionRegistries.Dao.DAO_REGISTRY.getKey(dao).toString());
    }
    default Double getGenerativeValue(String dao) {
        return getGenerativeDao().get(dao);
    }

    default Double getRelatedValue(IDao dao) {
        return getRelatedValue(AscensionRegistries.Dao.DAO_REGISTRY.getKey(dao).toString());
    }
    default Double getRelatedValue(String dao){
        return getRelatedDao().get(dao);
    }

    Map<String,Double> getGenerativeDao(); //Dao that GENERATE this dao (enhance it)
    Map<String,Double> getDestructiveDao(); //Dao that DESTROY this dao (weaken it)


    Map<String,Double> getRelatedDao();
}
