package net.thejadeproject.ascension.progression.skills;

import net.lucent.easygui.util.textures.TextureData;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.progression.skills.passive_skills.SwordIntent;
import net.thejadeproject.ascension.progression.techniques.ModTechniques;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.skills.passive_skills.FistAura;
import net.thejadeproject.ascension.progression.skills.passive_skills.IronBonesPassiveSkill;

public class ModSkills {
    public static final DeferredRegister<ISkill> SKILLS = DeferredRegister.create(AscensionRegistries.Skills.SKILL_REGISTRY, AscensionCraft.MOD_ID);


    public static final DeferredHolder<ISkill,  ? extends AbstractPassiveSkill> IRON_BONES = SKILLS.register("iron_bones_passive_skill",
           ()->new IronBonesPassiveSkill()
                   .setSkillIcon(new TextureData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/spells/iron_body.png"),32,32)
           ));

    public static final DeferredHolder<ISkill, ? extends AbstractPassiveSkill > FIST_AURA = SKILLS.register("fist_aura_skill",
            ()->new FistAura()
                    .setSkillIcon(new TextureData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/spells/fist_aura.png"),32,32)
                    ));
    public static final DeferredHolder<ISkill,  ? extends AbstractPassiveSkill > SWORD_INTENT = SKILLS.register("sword_intent_skill",
            ()->new SwordIntent()
                    .setSkillIcon(new TextureData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/spells/fist_aura.png"),32,32)
                    ));


    public static void register(IEventBus eventBus){
        SKILLS.register(eventBus);
    }
}
