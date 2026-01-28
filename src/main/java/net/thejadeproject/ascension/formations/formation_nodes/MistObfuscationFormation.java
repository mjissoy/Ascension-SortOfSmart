package net.thejadeproject.ascension.formations.formation_nodes;

import net.lucent.formation_arrays.api.cores.IFormationCore;
import net.lucent.formation_arrays.api.formations.IFormation;
import net.lucent.formation_arrays.formations.node.FormationNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.TriState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MistObfuscationFormation extends FormationNode {
    public final double RADIUS;
    public final int QI_DRAIN;
    private BlockPos currentPos;
    private final List<Player> obfuscatedPlayers = new ArrayList<>();

    public MistObfuscationFormation(IFormation formation, UUID uuid, double radius, int qiDrain) {
        super(formation);
        setFormationId(uuid);
        this.RADIUS = radius;
        this.QI_DRAIN = qiDrain;

        if (FMLLoader.getDist() == Dist.CLIENT) {
            NeoForge.EVENT_BUS.addListener(this::onRenderNameTag);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void onRenderNameTag(RenderNameTagEvent event) {
        if (!activeLastTick()) return;
        if (currentPos == null) return;

        //Only affect players
        if (!(event.getEntity() instanceof Player targetPlayer)) return;

        LocalPlayer viewer = Minecraft.getInstance().player;
        if (viewer == null) return;

        Vec3 formationCenter = currentPos.getCenter();

        //Check if targeted player is inside formation

        boolean targetInside = targetPlayer.position().distanceToSqr(formationCenter) <= RADIUS * RADIUS;

        //Check if viewer is inside the formation
        boolean viewerInside = viewer.position().distanceToSqr(formationCenter) <= RADIUS * RADIUS;

        //Logic Behind Formation: Hide nametag if target is inside but viewer is outsider
        //Rule:
        // - Outside Viewerss can't see inside players names
        // - Insider Viewers can see anyone (both inside and outside)
        if (targetInside && !viewerInside) {
            event.setCanRender(TriState.FALSE);
        }
    }

    @Override
    public void tick(Level level, BlockPos pos, IFormationCore blockEntity, List<ItemStack> jadeSlips) {
        this.currentPos = pos;

        //Track which players are being Obfuscated for possible extension to logic
        if (!level.isClientSide()) {
            updateObfuscatedPlayers(level, pos);
        }
    }

    private void updateObfuscatedPlayers(Level level, BlockPos pos) {
        obfuscatedPlayers.clear();
        if (!activeLastTick()) return;

        Vec3 center = pos.getCenter();

        List<Player> playersInRange = level.getEntitiesOfClass(
                Player.class,
                new AABB(pos).inflate(RADIUS),
                player -> player.position().distanceToSqr(center) <= RADIUS * RADIUS
        );

        obfuscatedPlayers.addAll(playersInRange);
    }

    @Override
    public int getEnergyCost() {
        return QI_DRAIN + (obfuscatedPlayers.size() * 2);
    }

    @Override
    public void deactivate(Level level, BlockPos pos, IFormationCore core) {
        super.deactivate(level, pos, core);
        obfuscatedPlayers.clear();
    }
}
