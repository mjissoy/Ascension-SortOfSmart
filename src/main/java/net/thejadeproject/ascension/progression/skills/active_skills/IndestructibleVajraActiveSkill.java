package net.thejadeproject.ascension.progression.skills.active_skills;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.data.CastType;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;
import net.thejadeproject.ascension.util.ModAttachments;

import java.util.*;

public class IndestructibleVajraActiveSkill extends AbstractActiveSkill {

    private static final Map<UUID, VajraData> activeVajraPlayers = new HashMap<>();

    private static class VajraData {
        float storedDamage = 0;
        int remainingTicks = 0;
        List<UUID> damageSources = new ArrayList<>();
    }

    public IndestructibleVajraActiveSkill() {
        super(Component.translatable("ascension.physique.active.indestructible_vajra"));
        this.path = "ascension:body";
        this.qiCost = 200.0;

        NeoForge.EVENT_BUS.addListener(this::onLivingDamageEvent);
    }

    @Override
    public boolean isPrimarySkill() {
        return true;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public int getCooldown() {
        return 20 * 120;
    }

    @Override
    public int maxCastingTicks() {
        return 0;
    }

    @Override
    public ISkillData getSkillData() {
        return null;
    }

    @Override
    public ISkillData decode(RegistryFriendlyByteBuf buf) {
        return null;
    }

    @Override
    public void cast(int castingTicksElapsed, Level level, Player player) {
        if (level.isClientSide()) return;

        UUID playerId = player.getUUID();
        VajraData data = new VajraData();

        //Duration 5 Sec + 1 sec per major realm
        int majorRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(this.path).majorRealm;
        data.remainingTicks = 20 * (5 + majorRealm);

        activeVajraPlayers.put(playerId, data);

        player.invulnerableTime = data.remainingTicks;

        //Maybe remove all neg effects on skill use and during
        // player.removeAllEffects();

        // Visual effect: spawn particles, play sound (future)
        // level.playSound(null, player.blockPosition(), SoundEvents.ANVIL_PLACE, ...);
    }

    @Override
    public void onPreCast() {

    }

    public void onLivingDamageEvent(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            UUID playerId = player.getUUID();
            VajraData data = activeVajraPlayers.get(playerId);

            if (data != null) {
                //Store Data for later reflection
                data.storedDamage += event.getOriginalDamage();

                //Record DMG Source
                Entity source = event.getSource().getEntity();
                if (source != null && !data.damageSources.contains(source.getUUID())) {
                    data.damageSources.add(source.getUUID());
                }

                event.setNewDamage(0);

                data.remainingTicks--;

                if (data.remainingTicks <= 0) {
                    releaseShockwave(player, data);
                    activeVajraPlayers.remove(playerId);
                }
            }
        }
    }

    private void releaseShockwave(Player player, VajraData data) {
        Level level = player.level();
        if (level.isClientSide()) return;

        //Shockwave radius base 5 blocks + 1 per major realm
        int majorRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(this.path).majorRealm;
        float radius = 5.0f + majorRealm;

        AABB aabb = player.getBoundingBox().inflate(radius);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb, livingEntity -> livingEntity != player && livingEntity.isAlive());

        //Calc dmg stored: stored dmg * multiplier
        float shockwaveDamage = data.storedDamage * 0.5f;

        for (LivingEntity entity : entities) {
            //dmg falls off with distance
            double distance = entity.distanceTo(player);
            if (distance <= radius) {
                float distanceMultiplier = 1.0f - (float)(distance / radius);
                float finalDamage = shockwaveDamage * distanceMultiplier;

                //Apply damage
                entity.hurt(player.damageSources().magic(), finalDamage);

                //Knock away from player
                double dx = entity.getX() - player.getX();
                double dz = entity.getZ() - player.getZ();
                double distance2D = Math.sqrt(dx * dx + dz * dz);
                if (distance2D > 0) {
                    double strength = (finalDamage / 5.0) * distanceMultiplier;
                    entity.setDeltaMovement(
                            (dx / distance2D) * strength,
                            0.3 * strength,
                            (dz / distance2D) * strength
                    );
                }
            }
        }

        // Visual/sound effect for shockwave (future for sick effects obv)
        // level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE, ...);
        // Spawn explosion particles

        data.storedDamage = 0;
        data.damageSources.clear();
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        activeVajraPlayers.remove(player.getUUID());
    }

    public static void clearPlayerVajra(UUID playerId) {
        activeVajraPlayers.remove(playerId);
    }
}
