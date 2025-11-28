package net.thejadeproject.ascension.progression.skills;

import net.lucent.easygui.util.textures.TextureData;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.progression.skills.active_skills.fire_dao.BasicFireBall;
import net.thejadeproject.ascension.progression.skills.active_skills.fire_dao.DelayedFireLaunch;
import net.thejadeproject.ascension.progression.skills.passive_skills.FlightPassiveSkill;
import net.thejadeproject.ascension.progression.skills.passive_skills.SwordIntent;
import net.thejadeproject.ascension.progression.techniques.ModTechniques;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.skills.passive_skills.FistAura;
import net.thejadeproject.ascension.progression.skills.passive_skills.IronBonesPassiveSkill;

import java.util.List;

public class ModSkills {
    public static final DeferredRegister<ISkill> SKILLS = DeferredRegister.create(AscensionRegistries.Skills.SKILL_REGISTRY, AscensionCraft.MOD_ID);


    public static final DeferredHolder<ISkill,  ? extends AbstractPassiveSkill> IRON_BONES = SKILLS.register("iron_bones_passive_skill",
           ()->new IronBonesPassiveSkill()
                   .setSkillIcon(new TextureData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/spells/iron_body.png"),32,32)
           ));

    public static final DeferredHolder<ISkill,  ? extends AbstractPassiveSkill> FLIGHT = SKILLS.register("flight_passive_skill",
            ()->new FlightPassiveSkill()
                    .setSkillIcon(new TextureData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/spells/iron_body.png"),32,32)
                    ));

    public static final DeferredHolder<ISkill, ? extends AbstractPassiveSkill > FIST_AURA = SKILLS.register("fist_aura_skill",
            ()->new FistAura()
                    .setSkillIcon(new TextureData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/spells/fist_aura.png"),32,32)
                    ).setSkillDescription(List.of(
                            Component.literal("Enhance your").append(" §8Fist ").append("with pure intent."),
                            Component.literal("Increasing damage of any").append(" §8Fist ").append("intent attacks")
                    )));
    public static final DeferredHolder<ISkill,  ? extends AbstractPassiveSkill > SWORD_INTENT = SKILLS.register("sword_intent_skill",
            SwordIntent::new);
    public static final DeferredHolder<ISkill,  ? extends AbstractActiveSkill > BASIC_FIRE_BALL = SKILLS.register("basic_fire_ball",
            ()->new BasicFireBall(1,5,"Basic Fire Ball",10,60)
                    .setSkillIcon(new TextureData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/spells/fire_ball.png"),32,32)
                    ));
    public static final DeferredHolder<ISkill,  ? extends AbstractActiveSkill > LARGE_FIRE_BALL = SKILLS.register("large_fire_ball",
            ()->new BasicFireBall(1,10,"Large Fire Ball",50,60)
                    .setSkillIcon(new TextureData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/spells/fire_ball.png"),32,32)
                    ));
    public static final DeferredHolder<ISkill,  ? extends AbstractActiveSkill > DELAYED_FIRE_LAUNCH = SKILLS.register("delayed_fire_launch",
            ()->new DelayedFireLaunch("Delayed Fire Launch")
                    .setSkillIcon(new TextureData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/spells/fire_ball.png"),32,32)
                    ));
    public static void register(IEventBus eventBus){
        SKILLS.register(eventBus);
    }
}
