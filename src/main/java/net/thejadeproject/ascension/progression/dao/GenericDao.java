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
    public Map<String ,Double> interactions = Map.of();
    public Map<String ,Double> relatedDao = Map.of();
    public List<Component> description = new ArrayList<>();
    public GenericDao(Component title){
        this.title = title;
    }

    public GenericDao setInteractions(Map<String ,Double> interactions){
        if(interactions == null){
            this.interactions = Map.of();
            return this;
        }
        this.interactions = interactions;
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
                Component.literal("Interactions:").withStyle(ChatFormatting.BOLD)
        );
        for (Map.Entry<String,Double> interaction: getInteractions().entrySet()){

            extraInfo.add(
                    AscensionRegistries.Dao.DAO_REGISTRY.get(ResourceLocation.bySeparator(interaction.getKey(),':')).getDisplayTitle().copy().append( ": "+interaction.getValue())
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
    public Double getInteractionValue(IDao dao) {
        Double value = 0.0;
        String daoId = AscensionRegistries.Dao.DAO_REGISTRY.getKey(dao).toString();
        if(getInteractions().containsKey(daoId)) value = getInteractions().get(daoId);
        for(Map.Entry<String ,Double> daoRelation : getRelatedDao().entrySet()){
            double tempValue = 1.0;
            IDao relatedDao = AscensionRegistries.Dao.DAO_REGISTRY.get(ResourceLocation.bySeparator(daoRelation.getKey(),':'));
            if(daoRelation.getKey().equals(daoId)){
                tempValue = tempValue*daoRelation.getValue();
            }else{
                tempValue =  tempValue*relatedDao.getInteractionValue(dao)*daoRelation.getValue();
            }
            value = value == 0 ? tempValue : value*tempValue;

        }
        return value;
    }

    @Override
    public Map<String , Double> getInteractions() {
        return interactions;
    }

    @Override
    public Map<String , Double> getRelatedDao() {
        return relatedDao;
    }


}
