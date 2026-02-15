package net.thejadeproject.ascension.refactor_packages.player_data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;

import java.util.HashMap;

public class PlayerData {

    private ResourceLocation activeForm;

    private final HashMap<ResourceLocation,IEntityFormData> formData = new HashMap<>();

    /*
        despite each form being able to store data for them
        there can only be 1 of each at a time
     */
    private ResourceLocation physique;
    private ResourceLocation bloodline;

    private ResourceLocation physiqueForm; //the form holding the data of the physique
    private ResourceLocation bloodlineForm;//almost always body (but might have some rare circumstances where it is not)


    private final HashMap<ResourceLocation,ResourceLocation> techniques = new HashMap<>(); //Path -> current technique



    //checks all locations
    public boolean hasSkill(ResourceLocation skill){
        for (IEntityFormData entityFormData : formData.values()){
            if(entityFormData.getHeldSkills().hasSkill(skill)) return true;
        }
        return false;
    }


    public static PlayerData getPlayerData(Player player){
        return null;//TODO
    }
}
