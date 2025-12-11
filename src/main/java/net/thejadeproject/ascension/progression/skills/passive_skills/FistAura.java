package net.thejadeproject.ascension.progression.skills.passive_skills;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.thejadeproject.ascension.entity.custom.AscensionSkillEntity;
import net.thejadeproject.ascension.progression.skills.AbstractPassiveSkill;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;
import net.thejadeproject.ascension.util.ModAttachments;
import net.thejadeproject.ascension.util.ModTags;
import org.joml.Vector3f;

public class FistAura extends AbstractPassiveSkill {

    // Orange color for the outline (RGB: 1.0, 0.5, 0.0)
    private static final DustParticleOptions ORANGE_OUTLINE = new DustParticleOptions(new Vector3f(1.0f, 0.5f, 0.0f), 1.0f);
    private static final DustParticleOptions ORANGE_BRIGHT = new DustParticleOptions(new Vector3f(1.0f, 0.7f, 0.2f), 1.5f);

    public FistAura() {
        super(Component.literal("Fist Aura"));

        this.path = "ascension:intent";
        NeoForge.EVENT_BUS.addListener(this::onLivingDamageEvent);
        NeoForge.EVENT_BUS.addListener(this::onPlayerTick);
    }

    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        // Check if player has the skill
        if (!player.getData(ModAttachments.PLAYER_SKILL_DATA).hasPassiveSkill("ascension:fist_aura_skill")) return;

        // Check main hand - if empty or fist intent item
        ItemStack mainHandItem = player.getMainHandItem();
        boolean shouldShowOutline = mainHandItem.isEmpty() ||
                mainHandItem.is(ModTags.Items.daoItemTags.get("ascension:fist_intent"));

        if (!shouldShowOutline) return;

        // Create orange outline around the hand
        spawnHandOutline(player);
    }

    public void onLivingDamageEvent(LivingDamageEvent.Pre event) {
        if (event.getSource().getEntity() != null) {
            if (!(event.getSource().getEntity() instanceof Player player)) return;
            if (!player.getData(ModAttachments.PLAYER_SKILL_DATA).hasPassiveSkill("ascension:fist_aura_skill")) return;

            // Check if weapon was used
            if (event.getSource().isDirect() &&
                    event.getSource().getWeaponItem() != ItemStack.EMPTY &&
                    !event.getSource().getWeaponItem().is(ModTags.Items.daoItemTags.get("ascension:fist_intent"))
            ) return;
            else {
                if (event.getSource().getDirectEntity() instanceof AscensionSkillEntity ascensionSkillEntity) {
                    if (!ascensionSkillEntity.getDaoTags().contains("ascension:fist_intent")) return;
                }
            }

            // Player has the skill so apply the bonus damage
            // Get Cultivation stage
            int majorRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(getSkillPath()).majorRealm;
            int minorRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(getSkillPath()).minorRealm;

            double damage = 2 * (1 + majorRealm * 10) * (1 + minorRealm);

            event.setNewDamage((float) (event.getNewDamage() + damage));

            // Spawn impact particles on successful hit
            if (player.level() instanceof ServerLevel serverLevel) {
                spawnPunchImpact(serverLevel, event.getEntity().position());
            }
        }
    }

    private void spawnHandOutline(Player player) {
        if (!(player.level() instanceof ServerLevel serverLevel)) return;

        // Outline for main hand
        spawnSingleHandOutline(serverLevel, player, InteractionHand.MAIN_HAND, player.tickCount);

        // Check off-hand
        ItemStack offHandItem = player.getOffhandItem();
        boolean offHandShouldShowOutline = offHandItem.isEmpty() ||
                offHandItem.is(ModTags.Items.daoItemTags.get("ascension:fist_intent"));

        if (offHandShouldShowOutline) {
            spawnSingleHandOutline(serverLevel, player, InteractionHand.OFF_HAND, player.tickCount + 10);
        }
    }

    private void spawnSingleHandOutline(ServerLevel serverLevel, Player player, InteractionHand hand, int tick) {
        // Get hand position and orientation
        HandOutlineData outlineData = calculateHandOutline(player, hand);
        Vec3 center = outlineData.center;
        Vec3 forward = outlineData.forward;
        Vec3 up = outlineData.up;
        Vec3 right = outlineData.right;

        double time = tick * 0.05;

        // Draw an orange outline around the hand
        // 1. Outline around the knuckles (semi-circle)
        int knucklePoints = 6;
        for (int i = 0; i < knucklePoints; i++) {
            double angle = Math.PI * (i / (double)(knucklePoints - 1)); // 0 to PI
            double radius = 0.06; // Small radius for knuckle curve

            // Position along a semi-circle in the forward-up plane
            double x = center.x + forward.x * radius * Math.cos(angle) + up.x * radius * Math.sin(angle);
            double y = center.y + forward.y * radius * Math.cos(angle) + up.y * radius * Math.sin(angle);
            double z = center.z + forward.z * radius * Math.cos(angle) + up.z * radius * Math.sin(angle);

            serverLevel.sendParticles(ORANGE_OUTLINE, x, y, z, 1, 0, 0, 0, 0);
        }

        // 2. Lines along the fingers
        // Create 4 lines representing fingers (thumb, index, middle, ring/pinky)
        for (int finger = 0; finger < 4; finger++) {
            double fingerOffset = (finger - 1.5) * 0.04; // Spread fingers
            Vec3 fingerStart = center.add(right.scale(fingerOffset));
            Vec3 fingerEnd = fingerStart.add(forward.scale(0.12)); // Finger length

            // Draw line segments for the finger
            int segments = 3;
            for (int seg = 0; seg <= segments; seg++) {
                double t = seg / (double)segments;
                Vec3 point = fingerStart.lerp(fingerEnd, t);

                serverLevel.sendParticles(ORANGE_OUTLINE,
                        point.x, point.y, point.z, 1, 0, 0, 0, 0);
            }
        }

        // 3. Outline around the back of the hand (wrist area)
        int wristPoints = 4;
        for (int i = 0; i < wristPoints; i++) {
            double angle = 2 * Math.PI * i / wristPoints;
            double radius = 0.04;

            // Circle in the right-up plane (perpendicular to forward)
            double x = center.x + right.x * radius * Math.cos(angle) + up.x * radius * Math.sin(angle);
            double y = center.y + right.y * radius * Math.cos(angle) + up.y * radius * Math.sin(angle);
            double z = center.z + right.z * radius * Math.cos(angle) + up.z * radius * Math.sin(angle);

            // Move slightly back toward wrist
            x -= forward.x * 0.03;
            y -= forward.y * 0.03;
            z -= forward.z * 0.03;

            serverLevel.sendParticles(ORANGE_OUTLINE, x, y, z, 1, 0, 0, 0, 0);
        }

        // 4. Pulsing effect on the center of the hand
        if (tick % 10 == 0) {
            double pulseSize = 0.02 + 0.01 * Math.sin(time * 2);

            // Center pulse
            serverLevel.sendParticles(ORANGE_BRIGHT,
                    center.x, center.y, center.z, 1, 0, 0, 0, 0);

            // Small radial pulses
            for (int i = 0; i < 4; i++) {
                double angle = Math.PI * 2 * i / 4;
                double x = center.x + right.x * pulseSize * Math.cos(angle) + up.x * pulseSize * Math.sin(angle);
                double y = center.y + right.y * pulseSize * Math.cos(angle) + up.y * pulseSize * Math.sin(angle);
                double z = center.z + right.z * pulseSize * Math.cos(angle) + up.z * pulseSize * Math.sin(angle);

                serverLevel.sendParticles(ORANGE_BRIGHT, x, y, z, 1, 0, 0, 0, 0);
            }
        }
    }

    private HandOutlineData calculateHandOutline(Player player, InteractionHand hand) {
        // Get player position and look direction
        Vec3 playerPos = player.position();
        Vec3 look = player.getLookAngle().normalize();

        // Calculate hand position - much simpler approach
        double sideOffset = 0.25;
        double forwardOffset = 0.35;
        double verticalOffset = player.getEyeHeight() * 0.65 - 0.1; // Chest level

        // Adjust for handedness
        if (hand == InteractionHand.MAIN_HAND) {
            sideOffset *= (player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1);
        } else {
            sideOffset *= (player.getMainArm() == HumanoidArm.RIGHT ? -1 : 1);
        }

        // Calculate orientation vectors
        Vec3 up = new Vec3(0, 1, 0);
        Vec3 right = look.cross(up).normalize();
        if (right.length() < 0.1) {
            // If look is straight up/down, use different up
            up = new Vec3(0, 0, 1);
            right = look.cross(up).normalize();
        }
        up = right.cross(look).normalize();

        // Calculate hand center position
        Vec3 center = playerPos
                .add(0, verticalOffset, 0)
                .add(right.scale(sideOffset))
                .add(look.scale(forwardOffset));

        return new HandOutlineData(center, look, up, right);
    }

    private static class HandOutlineData {
        public final Vec3 center;
        public final Vec3 forward; // Direction hand is facing (punch direction)
        public final Vec3 up;      // Up direction for the hand
        public final Vec3 right;   // Right direction for the hand

        public HandOutlineData(Vec3 center, Vec3 forward, Vec3 up, Vec3 right) {
            this.center = center;
            this.forward = forward;
            this.up = up;
            this.right = right;
        }
    }

    private void spawnPunchImpact(ServerLevel serverLevel, Vec3 position) {
        // Create an orange burst on impact
        for (int i = 0; i < 8; i++) {
            double angle = serverLevel.random.nextDouble() * Math.PI * 2;
            double radius = serverLevel.random.nextDouble() * 0.15;
            double x = position.x + radius * Math.cos(angle);
            double z = position.z + radius * Math.sin(angle);
            double y = position.y + serverLevel.random.nextDouble() * 0.3;

            double dx = (x - position.x) * 0.15;
            double dz = (z - position.z) * 0.15;
            double dy = serverLevel.random.nextDouble() * 0.05;

            serverLevel.sendParticles(ORANGE_BRIGHT,
                    x, y, z,
                    1, dx, dy, dz, 0.03);
        }

        // Line effect showing punch direction
        for (int i = 0; i < 3; i++) {
            double offsetX = (serverLevel.random.nextDouble() - 0.5) * 0.1;
            double offsetZ = (serverLevel.random.nextDouble() - 0.5) * 0.1;

            serverLevel.sendParticles(ORANGE_OUTLINE,
                    position.x + offsetX, position.y + 0.1, position.z + offsetZ,
                    1, 0, 0.05, 0, 0);
        }
    }

    @Override
    public void onSkillAdded(Player player) {
        super.onSkillAdded(player);
    }

    @Override
    public ISkillData decode(RegistryFriendlyByteBuf buf) {
        return null;
    }
}