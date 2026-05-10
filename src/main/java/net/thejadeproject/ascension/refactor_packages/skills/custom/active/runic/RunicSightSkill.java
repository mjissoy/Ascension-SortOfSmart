package net.thejadeproject.ascension.refactor_packages.skills.custom.active.runic;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.textures.ITextureData;
import net.lucent.easygui.gui.textures.TextureData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.gui.elements.info_elements.DescriptionDisplayContainer;
import net.thejadeproject.ascension.refactor_packages.network.client_bound.toast.ShowAscensionToast;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.runic.RunicPathHelper;
import net.thejadeproject.ascension.refactor_packages.runic.RunicPlayerData;
import net.thejadeproject.ascension.refactor_packages.runic.runes.ModRunicRunes;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastResult;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.CastType;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastableSkill;
import net.thejadeproject.ascension.refactor_packages.skills.castable.IPreCastData;

import java.util.ArrayList;
import java.util.List;

public class RunicSightSkill implements ICastableSkill {

    private static final double QI_COST = 4.0D;
    private static final double REACH = 12.0D;
    private static final int COOLDOWN_TICKS = 40;
    private static final float OBSERVATION_GAIN = 0.25F;

    @Override
    public CastResult canCast(Entity caster, IPreCastData preCastData) {
        if (!(caster instanceof ServerPlayer player)) {
            return new CastResult(CastResult.Type.FAILURE);
        }

        if (!player.hasData(ModAttachments.ENTITY_DATA)) {
            return new CastResult(CastResult.Type.FAILURE);
        }

        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);

        if (!RunicPathHelper.hasEnteredRunicPath(entityData)) {
            return new CastResult(CastResult.Type.FAILURE);
        }

        if (!entityData.getQiContainer().hasQi(QI_COST)) {
            return new CastResult(CastResult.Type.FAILURE);
        }

        return new CastResult(CastResult.Type.SUCCESS);
    }

    @Override
    public void initialCast(Entity caster, IPreCastData preCastData) {
        if (!(caster instanceof ServerPlayer player)) return;
        if (!player.hasData(ModAttachments.ENTITY_DATA)) return;

        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);

        if (!entityData.getQiContainer().tryConsumeQi(QI_COST)) {
            return;
        }

        List<ResourceLocation> seenRunes = findRunesInSight(player);

        if (seenRunes.isEmpty()) {
            player.displayClientMessage(Component.literal("No runic traces found."), true);
            return;
        }

        RunicPlayerData runicData = RunicPathHelper.getRunicData(player);
        List<ResourceLocation> newlyObserved = new ArrayList<>();
        List<ResourceLocation> glimpsed = new ArrayList<>();

        for (ResourceLocation runeId : seenRunes) {
            if (!RunicPathHelper.canObserveRune(entityData, runeId)) {
                continue;
            }

            if (runicData.knowsRune(runeId)) {
                continue;
            }

            float oldProgress = runicData.getObservationProgress().getOrDefault(runeId, 0.0F);
            float newProgress = Math.min(1.0F, oldProgress + OBSERVATION_GAIN);

            runicData.addGlimpsedRune(runeId);
            runicData.setObservationProgress(runeId, newProgress);
            glimpsed.add(runeId);

            if (newProgress >= 1.0F) {
                runicData.addObservedRune(runeId);
                newlyObserved.add(runeId);
            }
        }

        RunicPathHelper.saveRunicData(player, runicData);

        if (!newlyObserved.isEmpty()) {
            ResourceLocation first = newlyObserved.getFirst();

            PacketDistributor.sendToPlayer(player, new ShowAscensionToast(
                    "Rune Observed",
                    readableRuneName(first) + " has become clear.",
                    ItemStack.EMPTY,
                    ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/toasts.png")
            ));

            player.displayClientMessage(
                    Component.literal("Observed: " + readableRuneName(first)),
                    true
            );
            return;
        }

        if (!glimpsed.isEmpty()) {
            ResourceLocation first = glimpsed.getFirst();
            float progress = runicData.getObservationProgress().getOrDefault(first, 0.0F);

            player.displayClientMessage(
                    Component.literal("Glimpsed: " + readableRuneName(first) + " " + (int)(progress * 100.0F) + "%"),
                    true
            );
            return;
        }

        player.displayClientMessage(Component.literal("The traces are too deep to read."), true);
    }

    private List<ResourceLocation> findRunesInSight(ServerPlayer player) {
        HitResult hit = player.pick(REACH, 0.0F, false);

        if (hit instanceof BlockHitResult blockHit && hit.getType() != HitResult.Type.MISS) {
            BlockPos pos = blockHit.getBlockPos();
            BlockState state = player.level().getBlockState(pos);

            return getRunesForBlock(state);
        }

        Entity entity = player.pick(REACH, 0.0F, true).getType() == HitResult.Type.ENTITY
                ? null
                : null;

        return List.of();
    }

    private List<ResourceLocation> getRunesForBlock(BlockState state) {
        List<ResourceLocation> runes = new ArrayList<>();

        if (state.is(Blocks.FIRE) || state.is(Blocks.LAVA)) {
            runes.add(ModRunicRunes.FLAME);
        }

        if (state.is(Blocks.WATER)) {
            runes.add(ModRunicRunes.WATER);
        }

        if (state.is(Blocks.STONE) || state.is(Blocks.COBBLESTONE) || state.is(Blocks.DEEPSLATE)) {
            runes.add(ModRunicRunes.EARTH);
            runes.add(ModRunicRunes.HEAVY);
        }

        if (state.is(Blocks.OAK_LOG) || state.is(Blocks.OAK_LEAVES) || state.is(Blocks.GRASS_BLOCK)) {
            runes.add(ModRunicRunes.WOOD);
        }

        if (state.is(Blocks.IRON_BLOCK) || state.is(Blocks.IRON_ORE) || state.is(Blocks.DEEPSLATE_IRON_ORE)) {
            runes.add(ModRunicRunes.METAL);
        }

        if (state.is(Blocks.ICE) || state.is(Blocks.PACKED_ICE) || state.is(Blocks.BLUE_ICE)) {
            runes.add(ModRunicRunes.FROST);
        }

        return runes;
    }

    private String readableRuneName(ResourceLocation runeId) {
        String path = runeId.getPath().replace("_", " ");
        return path.substring(0, 1).toUpperCase() + path.substring(1);
    }

    @Override
    public int getCooldown(CastEndData castEndData) {
        return COOLDOWN_TICKS;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextureData getIcon() {
        return new TextureData(
                ResourceLocation.fromNamespaceAndPath(
                        AscensionCraft.MOD_ID,
                        "textures/spells/icon/placeholder.png"
                ),
                16,
                16
        );
    }

    @Override
    public Component getTitle() {
        return Component.translatable("ascension.skill.runic_sight");
    }

    @Override
    public Component getDescription() {
        return Component.translatable("ascension.skill.runic_sight.description");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public RenderableElement getInformationContainer(UIFrame frame) {
        return new DescriptionDisplayContainer(frame, getTitle(), getDescription());
    }

    @Override public void finalCast(CastEndData reason, Entity caster, ICastData castData) {}
    @Override public boolean continueCasting(int ticksElapsed, Entity caster, ICastData castData) { return false; }
    @Override public void onEquip(IEntityData entityData) {}
    @Override public void onUnEquip(IEntityData entityData, IPreCastData preCastData) {}
    @Override public void selected(IEntityData entityData) {}
    @Override public void unselected(IEntityData entityData) {}

    @Override public IPreCastData freshPreCastData() { return null; }
    @Override public IPreCastData preCastDataFromCompound(CompoundTag tag) { return null; }
    @Override public IPreCastData preCastDataFromNetwork(RegistryFriendlyByteBuf buf) { return null; }

    @Override public ICastData freshCastData() { return null; }
    @Override public ICastData castDataFromCompound(CompoundTag tag) { return null; }
    @Override public ICastData castDataFromNetwork(RegistryFriendlyByteBuf buf) { return null; }

    @Override public IPersistentSkillData freshPersistentInstance() { return null; }
    @Override public IPersistentSkillData persistentInstanceFromCompound(CompoundTag tag) { return null; }
    @Override public IPersistentSkillData persistentInstanceFromNetwork(RegistryFriendlyByteBuf buf) { return null; }

    @OnlyIn(Dist.CLIENT)
    @Override public RenderableElement getCastElement(UIFrame frame) { return null; }

    @Override public void onAdded(IEntityData attachedEntityData) {}
    @Override public void onRemoved(IEntityData attachedEntityData, IPersistentSkillData persistentData) {}
    @Override public void onFormAdded(IEntityData heldEntity, ResourceLocation form, IPhysiqueData physiqueData) {}
    @Override public void onFormRemoved(IEntityData heldEntity, ResourceLocation form, IPhysiqueData physiqueData) {}
    @Override public void finishedCooldown(IEntityData attachedEntityData, String identifier) {}

    @Override public IPersistentSkillData freshPersistentData(IEntityData heldEntity) { return null; }
    @Override public IPersistentSkillData fromCompound(CompoundTag tag, IEntityData heldEntity) { return null; }
    @Override public IPersistentSkillData fromNetwork(RegistryFriendlyByteBuf buf) { return null; }
}