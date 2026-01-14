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
import net.thejadeproject.ascension.loot.AddPhysiqueRandomPurityModifier;

import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, AscensionCraft.MOD_ID);
    }
    @Override
    protected void start() {


        //Dragon Vein Phys
        add("dragon_vein_from_ender_dragon", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:entities/ender_dragon")).build(),
                        LootItemRandomChanceCondition.randomChance(0.09f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "dragon_vein_physique"),
                20
        ));

        //Heavenly Demon Phys
        add("heavenly_demon_from_wither", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:entities/wither")).build(),
                        LootItemRandomChanceCondition.randomChance(0.07f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "heavenly_demon_physique"),
                15
        ));

        //Soul Anchor Phys
        add("soul_anchor_from_ancient_city", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:chests/ancient_city")).build(),
                        LootItemRandomChanceCondition.randomChance(0.12f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "soul_anchor_physique"),
                15
        ));

        //Eternal Reincarnation Physique
        add("eternal_reincarnation_range", new AddPhysiqueRandomPurityModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:chests/trial_chambers/vault")).build(),
                        LootItemRandomChanceCondition.randomChance(0.08f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "eternal_reincarnation_physique"),
                5,
                25
        ));

        //HundredPoison Phys
        add("hundred_poison_from_witch", new AddPhysiqueRandomPurityModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:entities/witch")).build(),
                        LootItemRandomChanceCondition.randomChance(0.21f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "hundred_poison_physique"),
                7,
                33
        ));
        add("hundred_poison_from_cave_spider", new AddPhysiqueRandomPurityModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:entities/cave_spider")).build(),
                        LootItemRandomChanceCondition.randomChance(0.15f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "hundred_poison_physique"),
                2,
                13
        ));
        add("hundred_poison_from_pufferfish", new AddPhysiqueRandomPurityModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:entities/pufferfish")).build(),
                        LootItemRandomChanceCondition.randomChance(0.15f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "hundred_poison_physique"),
                3,
                10
        ));

        // Stone Skin Physique (Human Tier) - Common defensive
        add("stone_skin_from_mineshaft", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:chests/abandoned_mineshaft")).build(),
                        LootItemRandomChanceCondition.randomChance(0.08f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "stone_skin_physique"),
                10
        ));

// Iron Marrow Physique (Human Tier) - Internal strength
        add("iron_marrow_from_iron_golem", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:entities/iron_golem")).build(),
                        LootItemRandomChanceCondition.randomChance(0.12f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "iron_marrow_physique"),
                12
        ));

// Jade Bone Physique (Earth Tier) - Defensive with luck
        add("jade_bone_from_jungle_temple", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:chests/jungle_temple")).build(),
                        LootItemRandomChanceCondition.randomChance(0.07f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "jade_bone_physique"),
                18
        ));

// Crystal Meridian Physique (Earth Tier) - Essence channeling
        add("crystal_meridian_from_end_city", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:chests/end_city_treasure")).build(),
                        LootItemRandomChanceCondition.randomChance(0.09f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "crystal_meridian_physique"),
                22
        ));

// Blood Jade Physique (Earth Tier) - Life and vitality
        add("blood_jade_from_nether_fortress", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:chests/nether_bridge")).build(),
                        LootItemRandomChanceCondition.randomChance(0.11f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "blood_jade_physique"),
                20
        ));

// Heavenly Lightning Physique (Heaven Tier) - Divine lightning
        add("heavenly_lightning_from_charged_creeper", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:entities/creeper")).build(),
                        LootItemRandomChanceCondition.randomChance(0.05f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "heavenly_lightning_physique"),
                24
        ));

// Phoenix Nirvana Physique (Heaven Tier) - Rebirth
        add("phoenix_nirvana_from_nether_fortress", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:chests/nether_bridge")).build(),
                        LootItemRandomChanceCondition.randomChance(0.06f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "phoenix_nirvana_physique"),
                13
        ));

// Primordial Chaos Physique (Ascension Tier) - Chaos origin
        add("primordial_chaos_from_end_city", new AddPhysiqueRandomPurityModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:chests/end_city_treasure")).build(),
                        LootItemRandomChanceCondition.randomChance(0.08f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "primordial_chaos_physique"),
                1,
                21
        ));

// Sword Saint Physique (Ascension Tier) - Ultimate sword
        add("sword_saint_from_bastion_treasure", new AddPhysiqueRandomPurityModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:chests/bastion_treasure")).build(),
                        LootItemRandomChanceCondition.randomChance(0.08f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "sword_saint_physique"),
                1,
                23
        ));

// Universe Devourer Physique (Ascension Tier) - Ultimate devouring
        add("universe_devourer_from_end_city", new AddPhysiqueRandomPurityModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:chests/end_city_treasure")).build(),
                        LootItemRandomChanceCondition.randomChance(0.08f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "universe_devourer_physique"),
                2,
                27
        ));

// Nine-Tailed Kitsune Physique (Ascension Tier) - Divine fox spirit
        add("nine_tailed_kitsune_from_fox", new AddPhysiqueRandomPurityModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:entities/fox")).build(),
                        LootItemRandomChanceCondition.randomChance(0.09f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "nine_tailed_kitsune_physique"),
                1,
                26
        ));

// Empty Vessel (Human Tier) - Blank slate
        add("empty_vessel_from_village", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:chests/village/village_plains_house")).build(),
                        LootItemRandomChanceCondition.randomChance(0.25f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "empty_vessel"),
                5
        ));

// Pure Sword Body (Earth Tier) - Sword specialization
        add("pure_sword_body_from_pillager", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:entities/pillager")).build(),
                        LootItemRandomChanceCondition.randomChance(0.18f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "pure_sword_body"),
                15
        ));

// Pure Spear Body (Earth Tier) - Spear specialization
        add("pure_spear_body_from_drowned", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:entities/drowned")).build(),
                        LootItemRandomChanceCondition.randomChance(0.16f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "pure_spear_body"),
                15
        ));

// Pure Axe Body (Earth Tier) - Axe specialization
        add("pure_axe_body_from_vindicator", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:entities/vindicator")).build(),
                        LootItemRandomChanceCondition.randomChance(0.17f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "pure_axe_body"),
                15
        ));

// Pure Blade Body (Earth Tier) - Blade specialization
        add("pure_blade_body_from_pillager", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:entities/pillager")).build(),
                        LootItemRandomChanceCondition.randomChance(0.16f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "pure_blade_body"),
                15
        ));

// Pure Fist Body (Earth Tier) - Unarmed specialization
        add("pure_fist_body_from_cave_spider", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:entities/cave_spider")).build(),
                        LootItemRandomChanceCondition.randomChance(0.19f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "pure_fist_body"),
                15
        ));

// Iron Bone Physique (Human Tier) - Durable bone structure
        add("iron_bone_from_iron_golem", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:entities/iron_golem")).build(),
                        LootItemRandomChanceCondition.randomChance(0.14f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "iron_bone_physique"),
                10
        ));

// Burning Skin Physique (Human Tier) - Fire-resistant skin
        add("burning_skin_from_blaze", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:entities/blaze")).build(),
                        LootItemRandomChanceCondition.randomChance(0.16f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "burning_skin_physique"),
                10
        ));

// Sacred Sapling Physique (Earth Tier) - Nature affinity
        add("sacred_sapling_from_jungle_temple", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:chests/jungle_temple")).build(),
                        LootItemRandomChanceCondition.randomChance(0.09f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "sacred_sapling_physique"),
                18
        ));

// Suppressed Yin Physique (Earth Tier) - Yin-aligned
        add("suppressed_yin_from_stray", new AddPhysiqueItemModifier(
                new LootItemCondition[] {
                        LootTableIdCondition.builder(ResourceLocation.parse("minecraft:entities/stray")).build(),
                        LootItemRandomChanceCondition.randomChance(0.17f).build()
                },
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "suppressed_yin_physique"),
                18
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
