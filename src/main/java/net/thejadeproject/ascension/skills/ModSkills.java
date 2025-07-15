package net.thejadeproject.ascension.skills;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.registries.AscensionRegistries;

public class ModSkills {
    public static final DeferredRegister<ISkill> SKILLS = DeferredRegister.create(AscensionRegistries.Skills.SKILL_REGISTRY, AscensionCraft.MOD_ID);







    public static void register(IEventBus eventBus){
        SKILLS.register(eventBus);
    }
}
