package net.thejadeproject.ascension.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.loot.AddItemModifier;
import net.thejadeproject.ascension.loot.AddPhysiqueItemModifier;
import net.thejadeproject.ascension.progression.techniques.ModTechniques;

import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, AscensionCraft.MOD_ID);
    }
    @Override
    protected void start() {


        add("heavenborn_end_city", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:chests/end_city_treasure")).build(),
                        LootItemRandomChanceCondition.randomChance(0.2f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "heavenborn_stone_monkey_physique"),
                10 // 10% purity
        ));

        add("pure_sword_bastion", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:chests/bastion_treasure")).build(),
                        LootItemRandomChanceCondition.randomChance(0.3f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "pure_sword_body"),
                25 // 25% purity
        ));

        add("pure_fire_fortress_broken", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:chests/nether_bridge")).build(),
                        LootItemRandomChanceCondition.randomChance(0.3f).build() // High chance for broken physique
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "pure_fire_body"),
                25 // 25% purity
        ));












        this.add("jade_bamboo_of_serenity_from_bamboo", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("blocks/bamboo")).build(),
                LootItemRandomChanceCondition.randomChance(0.08F).build()}, ModItems.JADE_BAMBOO_OF_SERENITY.get()));

        //Living Core Drop Regular Mobs
        this.add("living_core_from_strider", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/strider")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.LIVING_CORE.get()));
        this.add("living_core_from_piglin", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/piglin")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.LIVING_CORE.get()));
        this.add("living_core_from_hoglin", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/hoglin")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.LIVING_CORE.get()));
        this.add("living_core_from_zoglin", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/zoglin")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.LIVING_CORE.get()));
        this.add("living_core_from_vex", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/vex")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.LIVING_CORE.get()));
        this.add("living_core_from_allay", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/allay")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.LIVING_CORE.get()));
        this.add("living_core_from_golem", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/iron_golem")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.LIVING_CORE.get()));

        //Living Core Drop Boss Undead
        this.add("living_core_from_ender_dragon", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/ender_dragon")).build(),
                LootItemRandomChanceCondition.randomChance(0.15F).build()}, ModItems.LIVING_CORE.get()));






        //Undead Core Drop Regular Undead Mobs
        this.add("undead_core_from_zombie", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/zombie")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.UNDEAD_CORE.get()));
        this.add("undead_core_from_skeleton", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/skeleton")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.UNDEAD_CORE.get()));
        this.add("undead_core_from_wither_skeleton", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/wither_skeleton")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.UNDEAD_CORE.get()));
        this.add("undead_core_from_stray", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/stray")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.UNDEAD_CORE.get()));
        this.add("undead_core_from_husk", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/husk")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.UNDEAD_CORE.get()));
        this.add("undead_core_from_drowned", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/drowned")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.UNDEAD_CORE.get()));
        this.add("undead_core_from_zombie_villager", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/zombie_villager")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.UNDEAD_CORE.get()));
        this.add("undead_core_from_zombified_piglin", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/zombified_piglin")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.UNDEAD_CORE.get()));
        this.add("undead_core_from_zombie_horse", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/zombie_horse")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.UNDEAD_CORE.get()));
        this.add("undead_core_from_phantom", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/phantom")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.UNDEAD_CORE.get()));
        this.add("undead_core_from_zoglin", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/zoglin")).build(),
                LootItemRandomChanceCondition.randomChance(0.06F).build()}, ModItems.UNDEAD_CORE.get()));
        //Undead Core Drop Boss Undead
        this.add("undead_core_from_wither", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/wither")).build(),
                LootItemRandomChanceCondition.randomChance(0.15F).build()}, ModItems.UNDEAD_CORE.get()));

    }
}
