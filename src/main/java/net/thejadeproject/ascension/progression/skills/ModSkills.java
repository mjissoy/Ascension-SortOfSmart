package net.thejadeproject.ascension.progression.skills;

import net.lucent.easygui.util.textures.TextureData;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.progression.skills.active_skills.IndestructibleVajraActiveSkill;
import net.thejadeproject.ascension.progression.skills.active_skills.SpiritualSenseActiveSkill;
import net.thejadeproject.ascension.progression.skills.active_skills.fire_dao.BasicFireBall;
import net.thejadeproject.ascension.progression.skills.active_skills.fire_dao.DelayedFireLaunch;
import net.thejadeproject.ascension.progression.skills.active_skills.metal_dao.OreSightActiveSkill;
import net.thejadeproject.ascension.progression.skills.active_skills.wood_dao.RootwardensCallActiveSkill;
import net.thejadeproject.ascension.progression.skills.passive_skills.*;
import net.thejadeproject.ascension.progression.techniques.ModTechniques;
import net.thejadeproject.ascension.registries.AscensionRegistries;

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



    public static final DeferredHolder<ISkill, ? extends AbstractPassiveSkill> STONEHIDE_PASSIVE = SKILLS.register("stonehide_passive",
            () -> new StonehidePassiveSkill()
                    .setSkillIcon(new TextureData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/spells/stonehide.png"), 32, 32)
                    ).setSkillDescription(List.of(
                            Component.translatable("ascension.physique.passive.stonehide.desc1"),
                            Component.translatable("ascension.physique.passive.stonehide.desc2"),
                            Component.translatable("ascension.physique.passive.stonehide.desc3"),
                            Component.translatable("ascension.physique.passive.stonehide.desc4"),
                            Component.translatable("ascension.physique.passive.stonehide.desc5"),
                            Component.translatable("ascension.physique.passive.stonehide.desc6")
                    ))
    );
    public static final DeferredHolder<ISkill, ? extends AbstractPassiveSkill> DIAMOND_ADAMANT_PASSIVE = SKILLS.register("diamond_adamant_passive",
            () -> new DiamondAdamantPassiveSkill()
                    .setSkillIcon(new TextureData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/spells/diamond_body.png"), 32, 32)
                    ).setSkillDescription(List.of(
                            Component.translatable("ascension.physique.passive.diamond_adamant.desc1"),
                            Component.translatable("ascension.physique.passive.diamond_adamant.desc2"),
                            Component.translatable("ascension.physique.passive.diamond_adamant.desc3"),
                            Component.translatable("ascension.physique.passive.diamond_adamant.desc4"),
                            Component.translatable("ascension.physique.passive.diamond_adamant.desc5"),
                            Component.translatable("ascension.physique.passive.diamond_adamant.desc6")
                    ))
    );
    public static final DeferredHolder<ISkill, ? extends  AbstractActiveSkill> INDESTRUCTIBLE_VAJRA_ACTIVE = SKILLS.register("indestructible_vajra_active",
            () -> new IndestructibleVajraActiveSkill()
                    .setSkillIcon(new TextureData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/spells/vajra_form.png"), 32, 32)
                    ).setSkillDescription(List.of(
                            Component.translatable("ascension.physique.active.indestructible_vajra.desc1"),
                            Component.translatable("ascension.physique.active.indestructible_vajra.desc2"),
                            Component.translatable("ascension.physique.active.indestructible_vajra.desc3"),
                            Component.translatable("ascension.physique.active.indestructible_vajra.desc4"),
                            Component.translatable("ascension.physique.active.indestructible_vajra.desc5"),
                            Component.translatable("ascension.physique.active.indestructible_vajra.desc6"),
                            Component.translatable("ascension.physique.active.indestructible_vajra.desc7"),
                            Component.translatable("ascension.physique.active.indestructible_vajra.desc8")
                    ))
    );

    public static final DeferredHolder<ISkill, ? extends AbstractActiveSkill> ORE_SIGHT_ACTIVE = SKILLS.register("ore_sight_active",
            () -> new OreSightActiveSkill()
                    .setSkillIcon(new TextureData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/spells/ore_sight.png"), 32, 32))
                    .setSkillDescription(List.of(
                            Component.translatable("ascension.skill.active.ore_sight.desc1"),
                            Component.translatable("ascension.skill.active.ore_sight.desc2"),
                            Component.translatable("ascension.skill.active.ore_sight.desc3"),
                            Component.translatable("ascension.skill.active.ore_sight.desc4"),
                            Component.translatable("ascension.skill.active.ore_sight.desc5")
                    ))
    );

    public static final DeferredHolder<ISkill, ? extends AbstractActiveSkill> ROOTWARDENS_CALL = SKILLS.register("rootwardens_call",
            () -> new RootwardensCallActiveSkill()
                    .setSkillIcon(new TextureData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/spells/rootwardens_call.png"), 32, 32))
                    .setSkillDescription(List.of(
                            Component.translatable("ascension.skill.active.rootwardens_call.desc1"),
                            Component.translatable("ascension.skill.active.rootwardens_call.desc2"),
                            Component.translatable("ascension.skill.active.rootwardens_call.desc3"),
                            Component.translatable("ascension.skill.active.rootwardens_call.desc4"),
                            Component.translatable("ascension.skill.active.rootwardens_call.desc5")
                    ))
    );

    public static final DeferredHolder<ISkill, ? extends AbstractActiveSkill> SPIRITUAL_SENSE = SKILLS.register("spiritual_sense",
            () -> new SpiritualSenseActiveSkill()
                    .setSkillIcon(new TextureData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/spells/spiritual_sense.png"), 32, 32))
                    .setSkillDescription(List.of(
                            Component.translatable("ascension.skill.active.spiritual_sense.desc1"),
                            Component.translatable("ascension.skill.active.spiritual_sense.desc2"),
                            Component.translatable("ascension.skill.active.spiritual_sense.desc3"),
                            Component.translatable("ascension.skill.active.spiritual_sense.desc4"),
                            Component.translatable("ascension.skill.active.spiritual_sense.desc5")
                    ))
    );





    public static void register(IEventBus eventBus){
        SKILLS.register(eventBus);
    }
}
