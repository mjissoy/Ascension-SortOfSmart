package net.thejadeproject.ascension.progression.skills.passive_skills.physique_skills.kitsune_skills;


import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.thejadeproject.ascension.progression.skills.AbstractPassiveSkill;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;
import net.thejadeproject.ascension.util.ModAttachments;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TailMultiplierPassiveSkill extends AbstractPassiveSkill {

    private static final Map<UUID, Integer> lastTailCount = new HashMap<>();

    public TailMultiplierPassiveSkill() {
        super(Component.translatable("ascension.physique.passive.tail_multiplier"));
        this.path = "ascension:illusion";
        NeoForge.EVENT_BUS.addListener(this::onLivingIncomingDamage);
        NeoForge.EVENT_BUS.addListener(this::onLivingHeal);
    }

    public void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!player.getData(ModAttachments.PLAYER_SKILL_DATA).hasPassiveSkill("ascension:tail_multiplier_passive"))
                return;

            // Get tail count from data component (you'll need to implement this)
            int tailCount = getTailCount(player);
            if (tailCount <= 0) return;

            // Damage reduction based on tails: 2% per tail (max 18% at 9 tails)
            float damageReduction = event.getOriginalAmount() * (tailCount * 0.02f);
            float newDamage = event.getOriginalAmount() - damageReduction;

            event.setAmount(newDamage);

            // Visual effect when damage is reduced
            if (damageReduction > 0.5f && player.level() instanceof ServerLevel serverLevel) {
                spawnTailProtectionParticles(serverLevel, player);
            }
        }
    }

    public void onLivingHeal(LivingHealEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!player.getData(ModAttachments.PLAYER_SKILL_DATA).hasPassiveSkill("ascension:tail_multiplier_passive"))
                return;

            int tailCount = getTailCount(player);
            if (tailCount <= 0) return;

            // Healing bonus: 5% per tail (max 45% at 9 tails)
            float healMultiplier = 1.0f + (tailCount * 0.05f);
            float newHealAmount = event.getAmount() * healMultiplier;

            event.setAmount(newHealAmount);

            // Visual effect when healing is boosted
            if (tailCount >= 3 && player.level() instanceof ServerLevel serverLevel) {
                spawnTailHealingParticles(serverLevel, player, tailCount);
            }
        }
    }

    private int getTailCount(Player player) {
        // TODO: Implement this method to get tail count from your data component
        // Example implementation:
        // return player.getData(ModDataComponents.TAIL_COUNT);

        // For now, return a placeholder based on realm
        int illusionRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(this.path).majorRealm;

        // Calculate tails: 1 tail per major realm starting from realm 4, max 9
        if (illusionRealm >= 4) {
            return Math.min(9, illusionRealm - 3); // Realm 4 = 1 tail, Realm 12 = 9 tails
        }
        return 0;
    }

    private void spawnTailProtectionParticles(ServerLevel serverLevel, Player player) {
        int tailCount = getTailCount(player);

        // Spawn particles around player based on tail count
        for (int tail = 0; tail < tailCount; tail++) {
            double angle = (2 * Math.PI * tail) / tailCount;
            double radius = 1.0 + (tail * 0.1); // Slightly different radius per tail

            for (int i = 0; i < 5; i++) {
                double particleAngle = angle + (player.level().random.nextDouble() - 0.5) * 0.5;
                double x = player.getX() + radius * Math.cos(particleAngle);
                double z = player.getZ() + radius * Math.sin(particleAngle);
                double y = player.getY() + 0.5 + player.level().random.nextDouble() * 1.5;

                // Protective fox magic particles
                serverLevel.sendParticles(ParticleTypes.ENCHANT,
                        x, y, z,
                        1, 0, 0.05, 0, 0.05);

                // Occasional sparkle
                if (player.level().random.nextInt(3) == 0) {
                    serverLevel.sendParticles(ParticleTypes.GLOW,
                            x, y, z,
                            1, 0.02, 0.02, 0.02, 0.01);
                }
            }
        }
    }

    private void spawnTailHealingParticles(ServerLevel serverLevel, Player player, int tailCount) {
        // Healing particles flow into player from tail positions
        for (int tail = 0; tail < tailCount; tail++) {
            double angle = (2 * Math.PI * tail) / tailCount;
            double radius = 1.5;

            // Tail position (source of healing energy)
            double tailX = player.getX() + radius * Math.cos(angle);
            double tailZ = player.getZ() + radius * Math.sin(angle);
            double tailY = player.getY() + 0.5;

            // Create particle stream from tail to player
            for (int i = 0; i < 3; i++) {
                double progress = player.level().random.nextDouble();
                double particleX = tailX + (player.getX() - tailX) * progress;
                double particleY = tailY + (player.getY() + 1.0 - tailY) * progress;
                double particleZ = tailZ + (player.getZ() - tailZ) * progress;

                // Healing energy particles
                serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                        particleX, particleY, particleZ,
                        1, 0, 0, 0, 0.05);

                // Fox magic trail
                if (i % 2 == 0) {
                    serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                            particleX, particleY, particleZ,
                            1, 0.01, 0.01, 0.01, 0.02);
                }
            }
        }

        // Central healing glow
        for (int i = 0; i < 10; i++) {
            double offsetX = (player.level().random.nextDouble() - 0.5) * 0.8;
            double offsetY = player.level().random.nextDouble() * 1.5;
            double offsetZ = (player.level().random.nextDouble() - 0.5) * 0.8;

            serverLevel.sendParticles(ParticleTypes.GLOW,
                    player.getX() + offsetX, player.getY() + offsetY, player.getZ() + offsetZ,
                    1, 0, 0, 0, 0.1);
        }
    }

    @Override
    public void onSkillAdded(Player player) {
        super.onSkillAdded(player);
        // Initialize with current tail count
        lastTailCount.put(player.getUUID(), getTailCount(player));
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        lastTailCount.remove(player.getUUID());
    }

    @Override
    public ISkillData decode(RegistryFriendlyByteBuf buf) {
        return null;
    }

    public static void clearPlayerData(UUID playerId) {
        lastTailCount.remove(playerId);
    }
}