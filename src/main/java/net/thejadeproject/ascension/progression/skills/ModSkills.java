package net.thejadeproject.ascension.progression.skills;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.skills.passive_skills.FistAura;
import net.thejadeproject.ascension.progression.skills.passive_skills.IronBonesPassiveSkill;

public class ModSkills {
    public static final DeferredRegister<ISkill> SKILLS = DeferredRegister.create(AscensionRegistries.Skills.SKILL_REGISTRY, AscensionCraft.MOD_ID);


    public static final DeferredHolder<ISkill, IronBonesPassiveSkill> IRON_BONES = SKILLS.register("iron_bones_passive_skill",
            IronBonesPassiveSkill::new);

    public static final DeferredHolder<ISkill, FistAura> FIST_AURA = SKILLS.register("fist_aura_skill",
            FistAura::new);


    public static void register(IEventBus eventBus){
        SKILLS.register(eventBus);
    }
}
