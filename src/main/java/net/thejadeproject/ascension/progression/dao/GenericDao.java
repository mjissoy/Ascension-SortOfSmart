package net.thejadeproject.ascension.progression.dao;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.registries.AscensionRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/*
a dao inherits the interactions of related Dao at a multiplier rate
 */
public class GenericDao implements IDao{
    public Component title;

    public Map<String ,Double> destructiveDao = Map.of();

    public Map<String ,Double> generativeDao = Map.of();


    public Map<String ,Double> relatedDao = Map.of();
    public List<Component> description = new ArrayList<>();
    public GenericDao(Component title){
        this.title = title;
    }


    public GenericDao setGenerativeDao(Map<String ,Double> generativeDao){
        if(generativeDao == null){
            this.generativeDao = Map.of();
            return this;
        }
        this.generativeDao = generativeDao;
        return this;
    }
    public GenericDao setDestructiveDao(Map<String ,Double> destructiveDao){
        if(destructiveDao == null){
            this.destructiveDao = Map.of();
            return this;
        }
        this.destructiveDao = destructiveDao;
        return this;
    }


    public GenericDao setRelatedDao(Map<String ,Double> relatedDao){
        if(relatedDao == null){
            this.relatedDao = Map.of();
            return this;
        }
        this.relatedDao = relatedDao;
        return this;
    }
    public GenericDao setDescription(List<Component> description){
        if(description == null){
            this.description = new ArrayList<>();
            return this;
        }
        this.description = description;
        return this;
    }
    @Override
    public Component getDisplayTitle() {
        return title;
    }

    @Override
    public List<Component> getDescription() {
        //TODO add interaction and other stuff
        List<Component> extraInfo = new ArrayList<>(){{
            add(getDisplayTitle());
        }};
        extraInfo.addAll(new ArrayList<>(description));
        extraInfo.add(
                Component.literal("Generative Dao:").withStyle(ChatFormatting.BOLD)
        );
        for (Map.Entry<String,Double> generativeDao: getGenerativeDao().entrySet()){

            extraInfo.add(
                    AscensionRegistries.Dao.DAO_REGISTRY.get(ResourceLocation.bySeparator(generativeDao.getKey(),':')).getDisplayTitle().copy().append( ": "+generativeDao.getValue())
            );
        }
        extraInfo.add(
                Component.literal("Destructive Dao:").withStyle(ChatFormatting.BOLD)
        );
        for (Map.Entry<String,Double> destructiveDao: getDestructiveDao().entrySet()){

            extraInfo.add(
                    AscensionRegistries.Dao.DAO_REGISTRY.get(ResourceLocation.bySeparator(destructiveDao.getKey(),':')).getDisplayTitle().copy().append( ": "+destructiveDao.getValue())
            );
        }

        extraInfo.add(
                Component.literal("Related Dao:").withStyle(ChatFormatting.BOLD)
        );
        for (Map.Entry<String,Double> relatedDao: getRelatedDao().entrySet()){
            extraInfo.add(
                    AscensionRegistries.Dao.DAO_REGISTRY.get(ResourceLocation.bySeparator(relatedDao.getKey(),':')).getDisplayTitle().copy().append( ": "+relatedDao.getValue())
            );
        }


        return extraInfo;
    }



    @Override
    public Map<String, Double> getGenerativeDao() {
        return generativeDao;
    }

    @Override
    public Map<String, Double> getDestructiveDao() {
        return destructiveDao;
    }


    @Override
    public Map<String , Double> getRelatedDao() {
        return relatedDao;
    }


}
