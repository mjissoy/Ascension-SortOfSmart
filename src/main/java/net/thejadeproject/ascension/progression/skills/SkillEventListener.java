package net.thejadeproject.ascension.progression.skills;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.events.custom.PhysiqueChangeEvent;
import net.thejadeproject.ascension.events.custom.TechniqueChangeEvent;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.registries.AscensionRegistries;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class SkillEventListener{

    @SubscribeEvent
    public static void onRealmChangeEvent(RealmChangeEvent event){
        for(ISkill skill : AscensionRegistries.Skills.SKILL_REGISTRY.stream().toList()){
            if(skill instanceof AbstractPassiveSkill passiveSkill){
                passiveSkill.onRealmChange(event);
            }
        }
    }

    @SubscribeEvent
    public static void onTechniqueChangeEvent(TechniqueChangeEvent event){
        for(ISkill skill : AscensionRegistries.Skills.SKILL_REGISTRY.stream().toList()){
            if(skill instanceof AbstractPassiveSkill passiveSkill){
                passiveSkill.onTechniqueChange(event);
            }
        }
    }
    @SubscribeEvent
    public static void onPhysiqueChangeEvent(PhysiqueChangeEvent event){
        for(ISkill skill : AscensionRegistries.Skills.SKILL_REGISTRY.stream().toList()){
            if(skill instanceof AbstractPassiveSkill passiveSkill){
                passiveSkill.onPhysiqueChange(event);
            }
        }
    }
}
