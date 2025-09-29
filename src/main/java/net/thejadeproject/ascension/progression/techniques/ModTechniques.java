package net.thejadeproject.ascension.progression.techniques;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.PlayerAttributeManager;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.items.technique_manuals.GenericTechniqueManual;
import net.thejadeproject.ascension.progression.skills.skill_lists.AcquirableSkillData;
import net.thejadeproject.ascension.progression.skills.skill_lists.SkillList;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.GenericCalculatedStabilityHandler;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.LnStabilityHandler;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.techniques.path_techniques.body.SingleAttributeTechnique;
import net.thejadeproject.ascension.progression.techniques.path_techniques.essence.SingleElementTechnique;
import net.thejadeproject.ascension.progression.techniques.path_techniques.intent.SingleIntentTechnique;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class ModTechniques {

    public static class TechniqueHolder{

        public DeferredHolder<ITechnique,? extends ITechnique> technique;
        public DeferredItem<GenericTechniqueManual> manual;

        public TechniqueHolder(DeferredHolder<ITechnique,? extends ITechnique> technique,DeferredItem<GenericTechniqueManual> manual){
            this.technique = technique;
            this.manual = manual;
        }

    }


    public static final DeferredRegister<ITechnique> TECHNIQUES =DeferredRegister.create(AscensionRegistries.Techniques.TECHNIQUES_REGISTRY, AscensionCraft.MOD_ID);

    public static final TechniqueHolder PURE_FIRE_TECHNIQUE = createTechnique("pure_fire_technique",
            ()->new SingleElementTechnique("Pure Fire Technique","ascension:fire",2.0,new LnStabilityHandler())
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:fire",2.0);
                    }})
                    .setOnMinorRealmChange(event -> {
                        Player player = event.player;
                        PlayerAttributeManager.increaseAttribute(player,5.0,Attributes.MAX_HEALTH);
                        PlayerAttributeManager.increaseAttribute(player,1.0,Attributes.ATTACK_DAMAGE);
                        PlayerAttributeManager.increaseAttribute(player,0.1,Attributes.MOVEMENT_SPEED);
                        PlayerAttributeManager.increaseAttribute(player,0.01,Attributes.JUMP_STRENGTH);
                        PlayerAttributeManager.increaseAttribute(player,0.01,Attributes.STEP_HEIGHT);


                    })
                    .setOnMajorRealmChange(event ->{
                        Player player = event.player;
                        PlayerAttributeManager.increaseAttribute(player,10.0,Attributes.MAX_HEALTH);
                        PlayerAttributeManager.increaseAttribute(player,2.0,Attributes.ATTACK_DAMAGE);
                        PlayerAttributeManager.increaseAttribute(player,0.5,Attributes.MOVEMENT_SPEED);
                        PlayerAttributeManager.increaseAttribute(player,0.02,Attributes.JUMP_STRENGTH);
                        PlayerAttributeManager.increaseAttribute(player,0.02,Attributes.STEP_HEIGHT);
                    })
                    .setDescription(List.of(
                            Component.literal("breathe in pure ").append("§4fire ").append("and purify ones essence")
                    )));

    public static final TechniqueHolder PURE_WATER_TECHNIQUE = createTechnique("pure_water_technique",
            ()->new SingleElementTechnique("Pure Water Technique","ascension:water",2.0,new LnStabilityHandler())
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:water",2.0);
                    }}).setOnMinorRealmChange(event -> {
                        Player player = event.player;
                        PlayerAttributeManager.increaseAttribute(player,5.0,Attributes.MAX_HEALTH);
                        PlayerAttributeManager.increaseAttribute(player,1.0,Attributes.ATTACK_DAMAGE);
                        PlayerAttributeManager.increaseAttribute(player,0.1,Attributes.MOVEMENT_SPEED);
                        PlayerAttributeManager.increaseAttribute(player,0.01,Attributes.JUMP_STRENGTH);
                        PlayerAttributeManager.increaseAttribute(player,0.01,Attributes.STEP_HEIGHT);
                        PlayerAttributeManager.increaseAttribute(player,0.01,Attributes.WATER_MOVEMENT_EFFICIENCY);


                    })
                    .setOnMajorRealmChange(event ->{
                        Player player = event.player;
                        PlayerAttributeManager.increaseAttribute(player,10.0,Attributes.MAX_HEALTH);
                        PlayerAttributeManager.increaseAttribute(player,2.0,Attributes.ATTACK_DAMAGE);
                        PlayerAttributeManager.increaseAttribute(player,0.5,Attributes.MOVEMENT_SPEED);
                        PlayerAttributeManager.increaseAttribute(player,0.02,Attributes.JUMP_STRENGTH);
                        PlayerAttributeManager.increaseAttribute(player,0.02,Attributes.STEP_HEIGHT);
                        PlayerAttributeManager.increaseAttribute(player,0.02,Attributes.WATER_MOVEMENT_EFFICIENCY);

                    }));
    public static final TechniqueHolder PURE_SWORD_INTENT = createTechnique("pure_sword_intent",
            ()->new SingleIntentTechnique("Pure Sword Technique",8.0,"ascension:sword_intent",new LnStabilityHandler())
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:sword_intent",2.0);
                    }}).setOnMinorRealmChange(event -> {
                        Player player = event.player;
                        PlayerAttributeManager.increaseAttribute(player,5.0,Attributes.MAX_HEALTH);
                        PlayerAttributeManager.increaseAttribute(player,1.0,Attributes.ATTACK_DAMAGE);
                        PlayerAttributeManager.increaseAttribute(player,0.1,Attributes.MOVEMENT_SPEED);
                        PlayerAttributeManager.increaseAttribute(player,0.01,Attributes.JUMP_STRENGTH);
                        PlayerAttributeManager.increaseAttribute(player,0.01,Attributes.STEP_HEIGHT);


                    })
                    .setOnMajorRealmChange(event ->{
                        Player player = event.player;
                        PlayerAttributeManager.increaseAttribute(player,10.0,Attributes.MAX_HEALTH);
                        PlayerAttributeManager.increaseAttribute(player,2.0,Attributes.ATTACK_DAMAGE);
                        PlayerAttributeManager.increaseAttribute(player,0.5,Attributes.MOVEMENT_SPEED);
                        PlayerAttributeManager.increaseAttribute(player,0.02,Attributes.JUMP_STRENGTH);
                        PlayerAttributeManager.increaseAttribute(player,0.02,Attributes.STEP_HEIGHT);
                    })
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:intent",0,3,"ascension:sword_intent_skill",true)
                    )));
    public static final TechniqueHolder PURE_FIST_INTENT = createTechnique("pure_fist_intent",
            ()->new SingleIntentTechnique("Pure Fist Technique",8.0,"ascension:fist_intent",new LnStabilityHandler())
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:fist_intent",2.0);
                    }}).setOnMinorRealmChange(event -> {
                        Player player = event.player;
                        PlayerAttributeManager.increaseAttribute(player,5.0,Attributes.MAX_HEALTH);
                        PlayerAttributeManager.increaseAttribute(player,1.0,Attributes.ATTACK_DAMAGE);
                        PlayerAttributeManager.increaseAttribute(player,0.1,Attributes.MOVEMENT_SPEED);
                        PlayerAttributeManager.increaseAttribute(player,0.01,Attributes.JUMP_STRENGTH);
                        PlayerAttributeManager.increaseAttribute(player,0.01,Attributes.STEP_HEIGHT);


                    })
                    .setOnMajorRealmChange(event ->{
                        Player player = event.player;
                        PlayerAttributeManager.increaseAttribute(player,10.0,Attributes.MAX_HEALTH);
                        PlayerAttributeManager.increaseAttribute(player,2.0,Attributes.ATTACK_DAMAGE);
                        PlayerAttributeManager.increaseAttribute(player,0.5,Attributes.MOVEMENT_SPEED);
                        PlayerAttributeManager.increaseAttribute(player,0.02,Attributes.JUMP_STRENGTH);
                        PlayerAttributeManager.increaseAttribute(player,0.02,Attributes.STEP_HEIGHT);
                    }).setDescription(List.of(
                            Component.literal("what heavens? under my").append(" §8Fist ").append("all is equal"),
                            Component.literal("and all is weak")
                    )));

    public static final TechniqueHolder DIVINE_PHOENIX_TECHNIQUE = createTechnique("divine_phoenix_technique",
            ()->new SingleAttributeTechnique("Divine Phoenix Technique",8.0,"ascension:phoenix_fire",new LnStabilityHandler())
                    .setEfficiencyAttributes(new HashMap<>(){{

                        put("ascension:phoenix_fire",2.0);
                    }}).setOnMinorRealmChange(event -> {
                        Player player = event.player;
                        PlayerAttributeManager.increaseAttribute(player,5.0,Attributes.MAX_HEALTH);
                        PlayerAttributeManager.increaseAttribute(player,1.0,Attributes.ATTACK_DAMAGE);
                        PlayerAttributeManager.increaseAttribute(player,0.1,Attributes.MOVEMENT_SPEED);
                        PlayerAttributeManager.increaseAttribute(player,0.01,Attributes.JUMP_STRENGTH);
                        PlayerAttributeManager.increaseAttribute(player,0.01,Attributes.STEP_HEIGHT);


                    })
                    .setOnMajorRealmChange(event ->{
                        Player player = event.player;
                        PlayerAttributeManager.increaseAttribute(player,10.0,Attributes.MAX_HEALTH);
                        PlayerAttributeManager.increaseAttribute(player,2.0,Attributes.ATTACK_DAMAGE);
                        PlayerAttributeManager.increaseAttribute(player,0.5,Attributes.MOVEMENT_SPEED);
                        PlayerAttributeManager.increaseAttribute(player,0.02,Attributes.JUMP_STRENGTH);
                        PlayerAttributeManager.increaseAttribute(player,0.02,Attributes.STEP_HEIGHT);
                    }));

    public static final TechniqueHolder VOID_SWALLOWING_TECHNIQUE = createTechnique("void_swallowing_technique",
            ()->new SingleAttributeTechnique("Void Swallowing Technique",8.0,"ascension:void",new LnStabilityHandler())
                    .setEfficiencyAttributes(new HashMap<>(){{

                        put("ascension:void",2.0);
                    }}).setOnMinorRealmChange(event -> {
                        Player player = event.player;
                        PlayerAttributeManager.increaseAttribute(player,10.0,Attributes.MAX_HEALTH);
                        PlayerAttributeManager.increaseAttribute(player,2.0,Attributes.ATTACK_DAMAGE);
                        PlayerAttributeManager.increaseAttribute(player,0.3,Attributes.MOVEMENT_SPEED);
                        PlayerAttributeManager.increaseAttribute(player,0.03,Attributes.JUMP_STRENGTH);
                        PlayerAttributeManager.increaseAttribute(player,0.02,Attributes.STEP_HEIGHT);


                    })
                    .setOnMajorRealmChange(event ->{
                        Player player = event.player;
                        PlayerAttributeManager.increaseAttribute(player,20.0,Attributes.MAX_HEALTH);
                        PlayerAttributeManager.increaseAttribute(player,4.0,Attributes.ATTACK_DAMAGE);
                        PlayerAttributeManager.increaseAttribute(player,0.7,Attributes.MOVEMENT_SPEED);
                        PlayerAttributeManager.increaseAttribute(player,0.06,Attributes.JUMP_STRENGTH);
                        PlayerAttributeManager.increaseAttribute(player,0.04,Attributes.STEP_HEIGHT);
                    }));
    public static TechniqueHolder createTechnique(String id, Supplier<? extends ITechnique> supplier){
        DeferredHolder<ITechnique,? extends ITechnique> techniqueHolder = TECHNIQUES.register(id,supplier);
        DeferredItem<GenericTechniqueManual> manualHolder = ModItems.ITEMS.register(id,()-> new GenericTechniqueManual(
                new Item.Properties(),
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, id)
                ));
        return new TechniqueHolder(techniqueHolder,manualHolder);
    }
    public static TechniqueHolder createTechnique(String id, Supplier<? extends ITechnique> supplier, Supplier<? extends GenericTechniqueManual> itemSupplier){
        DeferredHolder<ITechnique,? extends ITechnique> techniqueHolder = TECHNIQUES.register(id,supplier);
        DeferredItem<GenericTechniqueManual> manualHolder = ModItems.ITEMS.register(id,itemSupplier);
        return new TechniqueHolder(techniqueHolder,manualHolder);
    }

    public static void register(IEventBus eventBus){
        TECHNIQUES.register(eventBus);
    }
}
