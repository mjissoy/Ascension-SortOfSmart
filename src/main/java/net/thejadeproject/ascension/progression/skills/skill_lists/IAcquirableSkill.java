package net.thejadeproject.ascension.progression.skills.skill_lists;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.events.custom.skills.PlayerSkillRemoveEvent;

public interface IAcquirableSkill {

    //only runs when the correct path realm is changed
    default void onRealmChange(RealmChangeEvent event){


        if(event.oldMajorRealm < event.newMajorRealm || (event.oldMajorRealm == event.newMajorRealm && event.oldMinorRealm<event.newMinorRealm)){

            onRealmIncrease(event);
        }else{

            onRealmDecrease(event);
        }
    }
    void onRealmIncrease(RealmChangeEvent event);
    void onRealmDecrease(RealmChangeEvent event);
    default boolean hasSkill(String skill){
        return hasSkill(ResourceLocation.bySeparator(skill,':'));
    }
    boolean hasSkill(ResourceLocation skill);
    /*
        when another AcquirableSkill or another source is trying to remove a skill this is called to ensure
        that we don't remove a skill that was also learned from another source

        checks isPermanent and isFixed

        boolean is there for quick escape
     */
    boolean tryRemoveSkill(PlayerSkillRemoveEvent event,ResourceLocation path);

    @OnlyIn(Dist.CLIENT)
    Component asComponent(ResourceLocation technique,ResourceLocation path,int majorRealm,int minorRealm);
}
