package net.thejadeproject.ascension.progression.skills.passive_skills.physique_skills.kitsune_skills;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.thejadeproject.ascension.progression.skills.AbstractPassiveSkill;
import net.thejadeproject.ascension.progression.skills.data.IPersistentSkillData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

import java.util.ArrayList;
import java.util.List;

public class KitsuneIllusionBasicPassiveSkill extends AbstractPassiveSkill {

    private static final long COOLDOWN_TICKS = 20 * 15;
    private static final String SKILL_ID = "ascension:kitsune_illusion_basic";

    public KitsuneIllusionBasicPassiveSkill() {
        super(Component.translatable("ascension.physique.passive.kitsune_illusion_basic"));
        this.path = "ascension:essence";
        NeoForge.EVENT_BUS.addListener(this::onLivingHurt);
    }

    public static class IllusionData implements IPersistentSkillData {
        private long lastTriggerTime = 0L;

        @Override
        public CompoundTag writeData() {
            CompoundTag tag = new CompoundTag();
            tag.putLong("LastTrigger", lastTriggerTime);
            return tag;
        }

        @Override
        public void readData(CompoundTag tag) {
            this.lastTriggerTime = tag.getLong("LastTrigger");
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf) {
            buf.writeLong(lastTriggerTime);
        }

        @Override
        public void decode(RegistryFriendlyByteBuf buf) {
            this.lastTriggerTime = buf.readLong();
        }

        public boolean isOnCooldown(long currentTime) {
            return (currentTime - lastTriggerTime) < COOLDOWN_TICKS;
        }

        public void trigger(long currentTime) {
            this.lastTriggerTime = currentTime;
        }
    }

    private IllusionData getData(Player player) {
        var skillData = player.getData(ModAttachments.PLAYER_SKILL_DATA);
        var metaData = skillData.getPassiveSkill(SKILL_ID);
        if (metaData != null && metaData.data instanceof IllusionData data) {
            return data;
        }
        return null;
    }

    public void onLivingHurt(LivingDamageEvent.Pre event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!player.getData(ModAttachments.PLAYER_SKILL_DATA).hasPassiveSkill(SKILL_ID)) return;

        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;

        IllusionData data = getData(player);
        if (data == null) return;

        long currentTime = player.level().getGameTime();

        if (data.isOnCooldown(currentTime)) {
            return;
        }

        int essenceRealm = player.getData(ModAttachments.PLAYER_DATA)
                .getCultivationData().getPathData(this.path).majorRealm;
        double dodgeChance = 0.10 + (essenceRealm * 0.05);

        if (player.getRandom().nextDouble() < dodgeChance) {
            createIllusionClone(player, attacker);
            data.trigger(currentTime);
            player.syncData(ModAttachments.PLAYER_SKILL_DATA);

            event.setNewDamage(0.0F);
            spawnIllusionParticles(player.level(), player.position());
        }
    }

    private void createIllusionClone(Player player, LivingEntity attacker) {
        if (!(player.level() instanceof ServerLevel serverLevel)) return;

        AABB searchArea = player.getBoundingBox().inflate(10.0);
        List<Monster> nearbyMonsters = player.level().getEntitiesOfClass(
                Monster.class,
                searchArea,
                monster -> monster != attacker && monster.isAlive()
        );

        LivingEntity distractionTarget = nearbyMonsters.isEmpty() ? attacker :
                nearbyMonsters.get(player.getRandom().nextInt(nearbyMonsters.size()));

        for (int i = 0; i < 30; i++) {
            double offsetX = (player.getRandom().nextDouble() - 0.5) * 2.0;
            double offsetY = player.getRandom().nextDouble() * 2.0;
            double offsetZ = (player.getRandom().nextDouble() - 0.5) * 2.0;

            serverLevel.sendParticles(ParticleTypes.CLOUD,
                    distractionTarget.getX() + offsetX,
                    distractionTarget.getY() + offsetY,
                    distractionTarget.getZ() + offsetZ,
                    1, 0, 0, 0, 0.05);
        }

        for (int i = 0; i < 20; i++) {
            double angle = player.getRandom().nextDouble() * Math.PI * 2;
            double radius = player.getRandom().nextDouble() * 1.5 + 0.5;
            double x = player.getX() + radius * Math.cos(angle);
            double z = player.getZ() + radius * Math.sin(angle);
            double y = player.getY() + player.getRandom().nextDouble() * 2.0;

            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                    x, y, z, 1, 0, 0.1, 0, 0.02);

            serverLevel.sendParticles(ParticleTypes.ENCHANT,
                    x, y, z, 1, 0, 0.05, 0, 0.05);
        }

        distractionTarget.setGlowingTag(true);
        serverLevel.getServer().execute(() -> {
            if (distractionTarget.isAlive()) {
                distractionTarget.setGlowingTag(false);
            }
        });
    }

    private void spawnIllusionParticles(Level level, Vec3 position) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        for (int i = 0; i < 15; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * 1.0;
            double offsetY = level.random.nextDouble() * 2.0;
            double offsetZ = (level.random.nextDouble() - 0.5) * 1.0;

            serverLevel.sendParticles(ParticleTypes.CLOUD,
                    position.x + offsetX, position.y + offsetY, position.z + offsetZ,
                    1, 0, 0, 0, 0.1);
        }

        for (int i = 0; i < 10; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * 0.5;
            double offsetY = level.random.nextDouble() * 0.5;
            double offsetZ = (level.random.nextDouble() - 0.5) * 0.5;

            serverLevel.sendParticles(ParticleTypes.WITCH,
                    position.x + offsetX, position.y + offsetY, position.z + offsetZ,
                    1, 0, 0.05, 0, 0.05);
        }
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance() {
        return new IllusionData();
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance(CompoundTag tag) {
        IllusionData data = new IllusionData();
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