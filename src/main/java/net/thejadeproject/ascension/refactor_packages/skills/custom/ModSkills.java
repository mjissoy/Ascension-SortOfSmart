package net.thejadeproject.ascension.refactor_packages.skills.custom;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysique;
import net.thejadeproject.ascension.refactor_packages.physiques.custom.GenericPhysique;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.GenericCultivationSkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.SwordCultivationSkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.passive.RunicFortificationSkill;

import java.util.Set;

public class ModSkills {
    public static final DeferredRegister<ISkill> SKILLS =DeferredRegister.create(AscensionRegistries.Skills.SKILL_REGISTRY, AscensionCraft.MOD_ID);


    //TODO update to not include any of these details (except path) and the technique defines the data
    public static final DeferredHolder<ISkill,? extends GenericCultivationSkill> BASIC_CULTIVATION_SKILL = SKILLS.register("basic_essence_cultivation_skill",
            ()->new GenericCultivationSkill(2.0,ModPaths.ESSENCE.getId()));

    public static final DeferredHolder<ISkill,? extends GenericCultivationSkill> BASIC_RUNIC_CULTIVATION_SKILL = SKILLS.register("basic_runic_cultivation_skill",
            ()->new GenericCultivationSkill(2.0,ModPaths.RUNIC.getId()));

    public static final DeferredHolder<ISkill,? extends SwordCultivationSkill> SWORD_CULTIVATION_SKILL = SKILLS.register("sword_cultivation_skill",
            SwordCultivationSkill::new);

    // Passives
    public static final DeferredHolder<ISkill, ? extends RunicFortificationSkill> RUNIC_FORTIFICATION = SKILLS.register(
            "runic_fortification",
            () -> new RunicFortificationSkill(Component.translatable("ascension.skill.runic_fortification"))
                    .setShortDescription(Component.translatable("ascension.skill.runic_fortification.short_description"))
                    .setDescription(Component.translatable("ascension.skill.runic_fortification.description"))
    );

    public static void register(IEventBus modEventBus){
        SKILLS.register(modEventBus);
    }
}
