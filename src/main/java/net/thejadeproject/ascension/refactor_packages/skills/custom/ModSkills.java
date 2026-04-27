package net.thejadeproject.ascension.refactor_packages.skills.custom;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.attack.fire.FireSpray;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.GenericCultivationSkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.ScholarlySoulCultivationSkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.SwordCultivationSkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.elemental.*;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.five_element.FiveElementCirculation;
import net.thejadeproject.ascension.refactor_packages.skills.custom.form_change.EnterSpiritForm;


public class ModSkills {
    public static final DeferredRegister<ISkill> SKILLS =DeferredRegister.create(AscensionRegistries.Skills.SKILL_REGISTRY, AscensionCraft.MOD_ID);


    //TODO update to not include any of these details (except path) and the technique defines the data
    public static final DeferredHolder<ISkill,? extends GenericCultivationSkill> BASIC_CULTIVATION_SKILL = SKILLS.register("basic_essence_cultivation_skill",
            ()->new GenericCultivationSkill(2.0,ModPaths.ESSENCE.getId()));

    public static final DeferredHolder<ISkill,? extends SwordCultivationSkill> SWORD_CULTIVATION_SKILL = SKILLS.register("sword_cultivation_skill",
            SwordCultivationSkill::new);
    public static final DeferredHolder<ISkill,? extends EnterSpiritForm> ENTER_SPIRIT_FORM = SKILLS.register("enter_spirit_form",
            EnterSpiritForm::new);

    public static final DeferredHolder<ISkill,? extends FiveElementCirculation> FIVE_ELEMENT_CIRCULATION = SKILLS.register("five_element_circulation",
            FiveElementCirculation::new);
    public static final DeferredHolder<ISkill,? extends FireSpray> FIRE_SPRAY = SKILLS.register("fire_spray",
            FireSpray::new);

    public static final DeferredHolder<ISkill, ? extends ScholarlySoulCultivationSkill> SCHOLARLY_SOUL_CULTIVATION_SKILL =
            SKILLS.register("scholarly_soul_cultivation_skill", ScholarlySoulCultivationSkill::new);

    // Basic Element/Essence Cultivation Skills
    public static final DeferredHolder<ISkill, ? extends FireEssenceCultivationSkill> FIRE_ESSENCE_CULTIVATION_SKILL =
            SKILLS.register("fire_essence_cultivation_skill", FireEssenceCultivationSkill::new);
    public static final DeferredHolder<ISkill, ? extends WaterEssenceCultivationSkill> WATER_ESSENCE_CULTIVATION_SKILL =
            SKILLS.register("water_essence_cultivation_skill", WaterEssenceCultivationSkill::new);
    public static final DeferredHolder<ISkill, ? extends WoodEssenceCultivationSkill> WOOD_ESSENCE_CULTIVATION_SKILL =
            SKILLS.register("wood_essence_cultivation_skill", WoodEssenceCultivationSkill::new);
    public static final DeferredHolder<ISkill, ? extends EarthEssenceCultivationSkill> EARTH_ESSENCE_CULTIVATION_SKILL =
            SKILLS.register("earth_essence_cultivation_skill", EarthEssenceCultivationSkill::new);
    public static final DeferredHolder<ISkill, ? extends MetalEssenceCultivationSkill> METAL_ESSENCE_CULTIVATION_SKILL =
            SKILLS.register("metal_essence_cultivation_skill", MetalEssenceCultivationSkill::new);

    public static void register(IEventBus modEventBus){
        SKILLS.register(modEventBus);
    }
}
