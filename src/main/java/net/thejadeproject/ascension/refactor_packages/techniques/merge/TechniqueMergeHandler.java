package net.thejadeproject.ascension.refactor_packages.techniques.merge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechniqueData;
import net.thejadeproject.ascension.refactor_packages.techniques.ModTechniques;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.BodyElementTechnique;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.technique_data.BodyTechniqueData;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.CombinedBodyElementTechnique;

import java.util.*;

public class TechniqueMergeHandler {

    // Maps a set of technique IDs → the merged result technique ID.
    private static final Map<Set<ResourceLocation>, ResourceLocation> MERGE_TABLE = new HashMap<>();

    static {
        // 2-element merges (generative cycle pairs)
        MERGE_TABLE.put(Set.of(
            ModTechniques.LIVER_WOOD_TECHNIQUE.getId(),
            ModTechniques.HEART_FIRE_TECHNIQUE.getId()
        ), ModTechniques.WOOD_FIRE_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.HEART_FIRE_TECHNIQUE.getId(),
            ModTechniques.SPLEEN_EARTH_TECHNIQUE.getId()
        ), ModTechniques.FIRE_EARTH_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.SPLEEN_EARTH_TECHNIQUE.getId(),
            ModTechniques.LUNG_METAL_TECHNIQUE.getId()
        ), ModTechniques.EARTH_METAL_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.LUNG_METAL_TECHNIQUE.getId(),
            ModTechniques.KIDNEY_WATER_TECHNIQUE.getId()
        ), ModTechniques.METAL_WATER_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.KIDNEY_WATER_TECHNIQUE.getId(),
            ModTechniques.LIVER_WOOD_TECHNIQUE.getId()
        ), ModTechniques.WATER_WOOD_BODY_TECHNIQUE.getId());

        // 3-element merges: 3 singles (all at once)
        MERGE_TABLE.put(Set.of(
            ModTechniques.LIVER_WOOD_TECHNIQUE.getId(),
            ModTechniques.HEART_FIRE_TECHNIQUE.getId(),
            ModTechniques.SPLEEN_EARTH_TECHNIQUE.getId()
        ), ModTechniques.WOOD_FIRE_EARTH_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.HEART_FIRE_TECHNIQUE.getId(),
            ModTechniques.SPLEEN_EARTH_TECHNIQUE.getId(),
            ModTechniques.LUNG_METAL_TECHNIQUE.getId()
        ), ModTechniques.FIRE_EARTH_METAL_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.SPLEEN_EARTH_TECHNIQUE.getId(),
            ModTechniques.LUNG_METAL_TECHNIQUE.getId(),
            ModTechniques.KIDNEY_WATER_TECHNIQUE.getId()
        ), ModTechniques.EARTH_METAL_WATER_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.LUNG_METAL_TECHNIQUE.getId(),
            ModTechniques.KIDNEY_WATER_TECHNIQUE.getId(),
            ModTechniques.LIVER_WOOD_TECHNIQUE.getId()
        ), ModTechniques.METAL_WATER_WOOD_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.KIDNEY_WATER_TECHNIQUE.getId(),
            ModTechniques.LIVER_WOOD_TECHNIQUE.getId(),
            ModTechniques.HEART_FIRE_TECHNIQUE.getId()
        ), ModTechniques.WATER_WOOD_FIRE_BODY_TECHNIQUE.getId());

        // 3-element merges: 2-element combined + 1 new adjacent single
        MERGE_TABLE.put(Set.of(
            ModTechniques.WOOD_FIRE_BODY_TECHNIQUE.getId(),
            ModTechniques.SPLEEN_EARTH_TECHNIQUE.getId()
        ), ModTechniques.WOOD_FIRE_EARTH_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.FIRE_EARTH_BODY_TECHNIQUE.getId(),
            ModTechniques.LUNG_METAL_TECHNIQUE.getId()
        ), ModTechniques.FIRE_EARTH_METAL_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.EARTH_METAL_BODY_TECHNIQUE.getId(),
            ModTechniques.KIDNEY_WATER_TECHNIQUE.getId()
        ), ModTechniques.EARTH_METAL_WATER_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.METAL_WATER_BODY_TECHNIQUE.getId(),
            ModTechniques.LIVER_WOOD_TECHNIQUE.getId()
        ), ModTechniques.METAL_WATER_WOOD_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.WATER_WOOD_BODY_TECHNIQUE.getId(),
            ModTechniques.HEART_FIRE_TECHNIQUE.getId()
        ), ModTechniques.WATER_WOOD_FIRE_BODY_TECHNIQUE.getId());

        // 4-element merges: 4 singles (all at once)
        MERGE_TABLE.put(Set.of(
            ModTechniques.LIVER_WOOD_TECHNIQUE.getId(),
            ModTechniques.HEART_FIRE_TECHNIQUE.getId(),
            ModTechniques.SPLEEN_EARTH_TECHNIQUE.getId(),
            ModTechniques.LUNG_METAL_TECHNIQUE.getId()
        ), ModTechniques.WOOD_FIRE_EARTH_METAL_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.HEART_FIRE_TECHNIQUE.getId(),
            ModTechniques.SPLEEN_EARTH_TECHNIQUE.getId(),
            ModTechniques.LUNG_METAL_TECHNIQUE.getId(),
            ModTechniques.KIDNEY_WATER_TECHNIQUE.getId()
        ), ModTechniques.FIRE_EARTH_METAL_WATER_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.SPLEEN_EARTH_TECHNIQUE.getId(),
            ModTechniques.LUNG_METAL_TECHNIQUE.getId(),
            ModTechniques.KIDNEY_WATER_TECHNIQUE.getId(),
            ModTechniques.LIVER_WOOD_TECHNIQUE.getId()
        ), ModTechniques.EARTH_METAL_WATER_WOOD_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.LUNG_METAL_TECHNIQUE.getId(),
            ModTechniques.KIDNEY_WATER_TECHNIQUE.getId(),
            ModTechniques.LIVER_WOOD_TECHNIQUE.getId(),
            ModTechniques.HEART_FIRE_TECHNIQUE.getId()
        ), ModTechniques.METAL_WATER_WOOD_FIRE_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.KIDNEY_WATER_TECHNIQUE.getId(),
            ModTechniques.LIVER_WOOD_TECHNIQUE.getId(),
            ModTechniques.HEART_FIRE_TECHNIQUE.getId(),
            ModTechniques.SPLEEN_EARTH_TECHNIQUE.getId()
        ), ModTechniques.WATER_WOOD_FIRE_EARTH_BODY_TECHNIQUE.getId());

        // 4-element merges: 3-element combined + 1 new adjacent single
        MERGE_TABLE.put(Set.of(
            ModTechniques.WOOD_FIRE_EARTH_BODY_TECHNIQUE.getId(),
            ModTechniques.LUNG_METAL_TECHNIQUE.getId()
        ), ModTechniques.WOOD_FIRE_EARTH_METAL_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.FIRE_EARTH_METAL_BODY_TECHNIQUE.getId(),
            ModTechniques.KIDNEY_WATER_TECHNIQUE.getId()
        ), ModTechniques.FIRE_EARTH_METAL_WATER_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.EARTH_METAL_WATER_BODY_TECHNIQUE.getId(),
            ModTechniques.LIVER_WOOD_TECHNIQUE.getId()
        ), ModTechniques.EARTH_METAL_WATER_WOOD_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.METAL_WATER_WOOD_BODY_TECHNIQUE.getId(),
            ModTechniques.HEART_FIRE_TECHNIQUE.getId()
        ), ModTechniques.METAL_WATER_WOOD_FIRE_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.WATER_WOOD_FIRE_BODY_TECHNIQUE.getId(),
            ModTechniques.SPLEEN_EARTH_TECHNIQUE.getId()
        ), ModTechniques.WATER_WOOD_FIRE_EARTH_BODY_TECHNIQUE.getId());

        // 5-element merges: 4-element combined + 1 new adjacent single
        MERGE_TABLE.put(Set.of(
            ModTechniques.WOOD_FIRE_EARTH_METAL_BODY_TECHNIQUE.getId(),
            ModTechniques.KIDNEY_WATER_TECHNIQUE.getId()
        ), ModTechniques.FIVE_ELEMENT_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.FIRE_EARTH_METAL_WATER_BODY_TECHNIQUE.getId(),
            ModTechniques.LIVER_WOOD_TECHNIQUE.getId()
        ), ModTechniques.FIVE_ELEMENT_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.EARTH_METAL_WATER_WOOD_BODY_TECHNIQUE.getId(),
            ModTechniques.HEART_FIRE_TECHNIQUE.getId()
        ), ModTechniques.FIVE_ELEMENT_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.METAL_WATER_WOOD_FIRE_BODY_TECHNIQUE.getId(),
            ModTechniques.SPLEEN_EARTH_TECHNIQUE.getId()
        ), ModTechniques.FIVE_ELEMENT_BODY_TECHNIQUE.getId());

        MERGE_TABLE.put(Set.of(
            ModTechniques.WATER_WOOD_FIRE_EARTH_BODY_TECHNIQUE.getId(),
            ModTechniques.LUNG_METAL_TECHNIQUE.getId()
        ), ModTechniques.FIVE_ELEMENT_BODY_TECHNIQUE.getId());

        // 5-element merge: all 5 singles at once
        MERGE_TABLE.put(Set.of(
            ModTechniques.HEART_FIRE_TECHNIQUE.getId(),
            ModTechniques.KIDNEY_WATER_TECHNIQUE.getId(),
            ModTechniques.LIVER_WOOD_TECHNIQUE.getId(),
            ModTechniques.SPLEEN_EARTH_TECHNIQUE.getId(),
            ModTechniques.LUNG_METAL_TECHNIQUE.getId()
        ), ModTechniques.FIVE_ELEMENT_BODY_TECHNIQUE.getId());
    }

    private static double baseSuccessChance(int techniqueCount) {
        return switch (techniqueCount) {
            case 2 -> 0.90;
            case 3 -> 0.70;
            case 4 -> 0.50;
            case 5 -> 0.20;
            default -> 0.0;
        };
    }

    /**
     * Returns all sets of body techniques the player is currently eligible to merge.
     */
    public static List<Set<ResourceLocation>> findEligibleMerges(IEntityData entityData) {
        PathData bodyPath = entityData.getPathData(ModPaths.BODY.getId());
        if (bodyPath == null) return List.of();

        long worldTime = entityData.getAttachedEntity().level().getGameTime();

        Set<ResourceLocation> candidates = new LinkedHashSet<>();
        candidates.addAll(bodyPath.getTechniqueHistory());
        if (bodyPath.getLastUsedTechnique() != null) candidates.add(bodyPath.getLastUsedTechnique());

        List<ResourceLocation> eligible = new ArrayList<>();
        for (ResourceLocation techId : candidates) {
            var tech = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(techId);
            if (!(tech instanceof BodyElementTechnique) && !(tech instanceof CombinedBodyElementTechnique)) continue;
            ITechniqueData data = bodyPath.getTechniqueData(techId);
            if (!(data instanceof BodyTechniqueData bodyData)) continue;
            if (bodyData.getTotalCultivationTicks() < BodyTechniqueData.PROFICIENCY_THRESHOLD) continue;
            if (bodyData.isMergeCoolingDown(worldTime)) continue;
            eligible.add(techId);
        }

        List<Set<ResourceLocation>> results = new ArrayList<>();
        for (Set<ResourceLocation> combo : MERGE_TABLE.keySet()) {
            if (eligible.containsAll(combo)) results.add(combo);
        }
        return results;
    }

    /**
     * Attempts the merge. On success assigns the merged technique.
     * On failure damages the player to 1 HP and records the cooldown on all involved techniques.
     *
     * @param player        the player attempting the merge
     * @param techniqueIds  the exact set of technique IDs to merge (must be a MERGE_TABLE key)
     * @param itemBonus     additional success chance from held items (0.0–1.0), unused until items are added
     */
    public static void attemptMerge(ServerPlayer player, Set<ResourceLocation> techniqueIds, double itemBonus) {
        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);
        ResourceLocation mergedTechniqueId = MERGE_TABLE.get(techniqueIds);
        if (mergedTechniqueId == null) return;

        double successChance = Math.min(1.0, baseSuccessChance(techniqueIds.size()) + itemBonus);
        boolean success = Math.random() < successChance;

        if (success) {
            entityData.setTechnique(mergedTechniqueId);
        } else {
            player.setHealth(1.0f);

            long worldTime = player.level().getGameTime();
            PathData bodyPath = entityData.getPathData(ModPaths.BODY.getId());
            for (ResourceLocation techId : techniqueIds) {
                ITechniqueData data = bodyPath.getTechniqueData(techId);
                if (data instanceof BodyTechniqueData bodyData) {
                    bodyData.recordFailedMerge(worldTime);
                }
            }
        }
    }

    public static ResourceLocation getMergeResult(Set<ResourceLocation> techniqueIds) {
        return MERGE_TABLE.get(techniqueIds);
    }
}
