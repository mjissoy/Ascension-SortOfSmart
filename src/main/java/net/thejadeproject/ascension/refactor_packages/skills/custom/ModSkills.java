package net.thejadeproject.ascension.refactor_packages.skills.custom;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.GenericCultivationSkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.SwordCultivationSkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.five_element.FiveElementCirculation;
import net.thejadeproject.ascension.refactor_packages.skills.custom.form_change.EnterSpiritForm;

import java.util.Set;

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

    public static void register(IEventBus modEventBus){
        SKILLS.register(modEventBus);
    }
}
