package net.thejadeproject.ascension.refactor_packages.skills.custom;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.GenericCultivationSkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.form_change.EnterSpiritForm;
import net.thejadeproject.ascension.runic_path.skills.active.RunicCultivationSkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.SwordCultivationSkill;
import net.thejadeproject.ascension.runic_path.skills.passive.RunicArmorSkill;
import net.thejadeproject.ascension.runic_path.skills.passive.RunicStrengthSkill;
import net.thejadeproject.ascension.runic_path.skills.passive.RunicVitalitySkill;

public class ModSkills {
    public static final DeferredRegister<ISkill> SKILLS =DeferredRegister.create(AscensionRegistries.Skills.SKILL_REGISTRY, AscensionCraft.MOD_ID);


    //TODO update to not include any of these details (except path) and the technique defines the data
    public static final DeferredHolder<ISkill,? extends GenericCultivationSkill> BASIC_CULTIVATION_SKILL = SKILLS.register("basic_essence_cultivation_skill",
            ()->new GenericCultivationSkill(2.0,ModPaths.ESSENCE.getId()));

    public static final DeferredHolder<ISkill, ? extends RunicCultivationSkill> BASIC_RUNIC_CULTIVATION_SKILL =
            SKILLS.register("basic_runic_cultivation_skill",
                    () -> new RunicCultivationSkill(2.0));

    public static final DeferredHolder<ISkill,? extends SwordCultivationSkill> SWORD_CULTIVATION_SKILL = SKILLS.register("sword_cultivation_skill",
            SwordCultivationSkill::new);

    public static final DeferredHolder<ISkill,? extends EnterSpiritForm> ENTER_SPIRIT_FORM = SKILLS.register("enter_spirit_form",
            EnterSpiritForm::new);

    // Passives
    public static final DeferredHolder<ISkill, ? extends RunicArmorSkill> RUNIC_ARMOR = SKILLS.register(
            "runic_fortification",
            () -> new RunicArmorSkill(Component.translatable("ascension.skill.runic_fortification"))
                    .setShortDescription(Component.translatable("ascension.skill.runic_fortification.short_description"))
                    .setDescription(Component.translatable("ascension.skill.runic_fortification.description"))
    );

    public static final DeferredHolder<ISkill, ? extends RunicStrengthSkill> RUNIC_STRENGTH = SKILLS.register(
            "runic_strength",
            () -> new RunicStrengthSkill(Component.translatable("ascension.skill.runic_strength"))
                    .setShortDescription(Component.translatable("ascension.skill.runic_strength.short_description"))
                    .setDescription(Component.translatable("ascension.skill.runic_strength.description"))
    );

    public static final DeferredHolder<ISkill, ? extends RunicVitalitySkill> RUNIC_VITALITY = SKILLS.register(
            "runic_vitality",
            () -> new RunicVitalitySkill(Component.translatable("ascension.skill.runic_vitality"))
                    .setShortDescription(Component.translatable("ascension.skill.runic_vitality.short_description"))
                    .setDescription(Component.translatable("ascension.skill.runic_vitality.description"))
    );

    public static void register(IEventBus modEventBus){
        SKILLS.register(modEventBus);
    }
}
