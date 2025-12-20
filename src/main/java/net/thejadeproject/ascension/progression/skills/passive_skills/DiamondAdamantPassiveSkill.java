package net.thejadeproject.ascension.progression.skills.passive_skills;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.thejadeproject.ascension.progression.skills.AbstractPassiveSkill;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DiamondAdamantPassiveSkill extends AbstractPassiveSkill {

    private static final Map<UUID, Long> lastSurvivalTime = new HashMap<>();
    private static final long COOLDOWN_TICKS = 20 * 60 * 5;

    public DiamondAdamantPassiveSkill(){
        super(Component.translatable("ascension.physique.passive.diamond_adamant"));
        this.path = "ascension:body";
        NeoForge.EVENT_BUS.addListener(this::onLivingDamage);
        NeoForge.EVENT_BUS.addListener(this::onLivingDeathEvent);
    }

    public void onLivingDamage(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            if (!player.getData(ModAttachments.PLAYER_SKILL_DATA).hasPassiveSkill("ascension:diamond_adamant_passive"))
                return;

            if (event.getSource().getDirectEntity() instanceof AbstractArrow arrow && arrow.isCritArrow()) {
                event.setNewDamage(event.getNewDamage() * 0.5f);
                return;
            }

            String msgId = event.getSource().getMsgId();
            if (msgId.contains("critical") || msgId.contains("crit")) {
                event.setNewDamage(event.getNewDamage() * 0.5f);
                return;
            }

            // Alternative: Check if damage is insanely high (could be critical)
            // This is a fallback method
            float playerMaxHealth = player.getMaxHealth();
            float incomingDamage = event.getNewDamage();

            // If damage > 40% of max health it might be a critical
            if (incomingDamage > playerMaxHealth * 0.4f) {
                // might be a critical reduce by 30% to be safe
                event.setNewDamage(incomingDamage * 0.7f);
            }
            if (event.getSource().getMsgId().contains("headshot") ||
                    event.getSource().getMsgId().contains("backstab")) {
                event.setNewDamage(incomingDamage * 0.5f);
            }
        }
    }

    public void onLivingDeathEvent(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!player.getData(ModAttachments.PLAYER_SKILL_DATA).hasPassiveSkill("ascension:diamond_adamant_passive")) return;

            UUID playerId = player.getUUID();
            long currentTime = player.level().getGameTime();
            Long lastTrigger = lastSurvivalTime.get(playerId);

            if (lastTrigger != null && (currentTime - lastTrigger) < COOLDOWN_TICKS) {
                return;
            }

            int majorRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(this.path).majorRealm;
            double survivalChance = 0.30 + (majorRealm * 0.05);

            if (player.getRandom().nextDouble() < survivalChance) {
                event.setCanceled(true);
                player.setHealth(1.0F);
                player.clearFire();

                // Add visual/sound effects (Future)
                // player.level().playSound(null, player.blockPosition(), SoundEvents.ANVIL_LAND, ...);

                lastSurvivalTime.put(playerId, currentTime);
            }
        }
    }

    @Override
    public void onSkillAdded(Player player) {
        super.onSkillAdded(player);
        lastSurvivalTime.putIfAbsent(player.getUUID(), 0L);
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        lastSurvivalTime.remove(player.getUUID());
    }

    @Override
    public ISkillData decode(RegistryFriendlyByteBuf buf) {
        return null;
    }

    public static void clearPlayerCooldown(UUID playerId) {
        lastSurvivalTime.remove(playerId);
    }
}
