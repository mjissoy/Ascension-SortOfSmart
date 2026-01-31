package net.thejadeproject.ascension.progression.skills.passive_skills;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.thejadeproject.ascension.progression.skills.AbstractPassiveSkill;
import net.thejadeproject.ascension.progression.skills.data.IPersistentSkillData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

public class DiamondAdamantPassiveSkill extends AbstractPassiveSkill {

    private static final long COOLDOWN_TICKS = 20 * 60 * 5;
    private static final String SKILL_ID = "ascension:diamond_adamant_passive";

    public DiamondAdamantPassiveSkill(){
        super(Component.translatable("ascension.physique.passive.diamond_adamant"));
        this.path = "ascension:body";
        NeoForge.EVENT_BUS.addListener(this::onLivingDamage);
        NeoForge.EVENT_BUS.addListener(this::onLivingDeathEvent);
    }

    public static class DiamondAdamantData implements IPersistentSkillData {
        private long lastSurvivalTime = 0L;

        @Override
        public CompoundTag writeData() {
            CompoundTag tag = new CompoundTag();
            tag.putLong("LastSurvivalTime", lastSurvivalTime);
            return tag;
        }

        @Override
        public void readData(CompoundTag tag) {
            this.lastSurvivalTime = tag.getLong("LastSurvivalTime");
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf) {
            buf.writeLong(lastSurvivalTime);
        }

        @Override
        public void decode(RegistryFriendlyByteBuf buf) {
            this.lastSurvivalTime = buf.readLong();
        }

        public boolean isOnCooldown(long currentTime) {
            return (currentTime - lastSurvivalTime) < COOLDOWN_TICKS;
        }

        public void setLastSurvivalTime(long time) {
            this.lastSurvivalTime = time;
        }
    }

    private DiamondAdamantData getData(Player player) {
        var skillData = player.getData(ModAttachments.PLAYER_SKILL_DATA);
        var metaData = skillData.getPassiveSkill(SKILL_ID);
        if (metaData != null && metaData.data instanceof DiamondAdamantData data) {
            return data;
        }
        return null;
    }

    public void onLivingDamage(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            if (!player.getData(ModAttachments.PLAYER_SKILL_DATA).hasPassiveSkill(SKILL_ID))
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

            float playerMaxHealth = player.getMaxHealth();
            float incomingDamage = event.getNewDamage();

            if (incomingDamage > playerMaxHealth * 0.4f) {
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
            if (!player.getData(ModAttachments.PLAYER_SKILL_DATA).hasPassiveSkill(SKILL_ID)) return;

            DiamondAdamantData data = getData(player);
            if (data == null) return;

            long currentTime = player.level().getGameTime();

            if (data.isOnCooldown(currentTime)) {
                return;
            }

            int majorRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(this.path).majorRealm;
            double survivalChance = 0.30 + (majorRealm * 0.05);

            if (player.getRandom().nextDouble() < survivalChance) {
                event.setCanceled(true);
                player.setHealth(1.0F);
                player.clearFire();

                data.setLastSurvivalTime(currentTime);
                player.syncData(ModAttachments.PLAYER_SKILL_DATA);
            }
        }
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance() {
        return new DiamondAdamantData();
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance(CompoundTag tag) {
        DiamondAdamantData data = new DiamondAdamantData();
        data.readData(tag);
        return data;
    }

    @Override
    public void onSkillAdded(Player player) {
        super.onSkillAdded(player);
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
    }
}