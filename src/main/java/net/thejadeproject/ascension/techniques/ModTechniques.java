package net.thejadeproject.ascension.techniques;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.PlayerAttributeManager;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.items.technique_manuals.GenericTechniqueManual;
import net.thejadeproject.ascension.physiques.IPhysique;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.techniques.path_techniques.essence.SingleElementTechnique;
import oshi.util.tuples.Pair;

import java.util.HashMap;
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
            ()->new SingleElementTechnique("Pure Fire Technique","ascension:fire",2.0)
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:fire",2.0);
                    }})
                    .setOnMinorRealmChange(event -> {
                        Player player = event.player;
                        PlayerAttributeManager.increaseAttribute(player,5.0,Attributes.MAX_HEALTH);
                        PlayerAttributeManager.increaseAttribute(player,1.0,Attributes.ATTACK_DAMAGE);
                        PlayerAttributeManager.increaseAttribute(player,0.1,Attributes.MOVEMENT_SPEED);
                        PlayerAttributeManager.increaseAttribute(player,0.01,Attributes.JUMP_STRENGTH);


                    })
                    .setOnMajorRealmChange(event ->{
                        Player player = event.player;
                        PlayerAttributeManager.increaseAttribute(player,10.0,Attributes.MAX_HEALTH);
                        PlayerAttributeManager.increaseAttribute(player,2.0,Attributes.ATTACK_DAMAGE);
                        PlayerAttributeManager.increaseAttribute(player,0.5,Attributes.MOVEMENT_SPEED);
                        PlayerAttributeManager.increaseAttribute(player,0.02,Attributes.JUMP_STRENGTH);
                    }));

    public static final TechniqueHolder PURE_WATER_TECHNIQUE = createTechnique("pure_water_technique",
            ()->new SingleElementTechnique("Pure Water Technique","ascension:water",2.0)
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:water",2.0);
                    }}).setOnMinorRealmChange(event -> {
                        Player player = event.player;
                        PlayerAttributeManager.increaseAttribute(player,5.0,Attributes.MAX_HEALTH);
                        PlayerAttributeManager.increaseAttribute(player,1.0,Attributes.ATTACK_DAMAGE);
                        PlayerAttributeManager.increaseAttribute(player,0.1,Attributes.MOVEMENT_SPEED);
                        PlayerAttributeManager.increaseAttribute(player,0.01,Attributes.JUMP_STRENGTH);


                    })
                    .setOnMajorRealmChange(event ->{
                        Player player = event.player;
                        PlayerAttributeManager.increaseAttribute(player,10.0,Attributes.MAX_HEALTH);
                        PlayerAttributeManager.increaseAttribute(player,2.0,Attributes.ATTACK_DAMAGE);
                        PlayerAttributeManager.increaseAttribute(player,0.5,Attributes.MOVEMENT_SPEED);
                        PlayerAttributeManager.increaseAttribute(player,0.02,Attributes.JUMP_STRENGTH);
                    }));

    public static TechniqueHolder createTechnique(String id, Supplier<? extends ITechnique> supplier){
        DeferredHolder<ITechnique,? extends ITechnique> techniqueHolder = TECHNIQUES.register(id,supplier);
        DeferredItem<GenericTechniqueManual> manualHolder = ModItems.ITEMS.register(id,()-> new GenericTechniqueManual(
                new Item.Properties(),
                ResourceLocation.fromNamespaceAndPath(
                        AscensionCraft.MOD_ID,
                        id
                )
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
