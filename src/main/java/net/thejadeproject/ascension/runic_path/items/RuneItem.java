package net.thejadeproject.ascension.runic_path.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.forms.forms.ModForms;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.skills.custom.ModSkills;
import net.thejadeproject.ascension.runic_path.Rune;
import net.thejadeproject.ascension.runic_path.Runes;
import net.thejadeproject.ascension.runic_path.RunicPathHelper;
import net.thejadeproject.ascension.runic_path.RunicRuneData;
import net.thejadeproject.ascension.runic_path.network.RealmRuneSelection;
import net.thejadeproject.ascension.runic_path.network.SyncRunes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RuneItem extends Item {

    private final ResourceLocation runeId;

    public RuneItem(Properties properties, ResourceLocation runeId) {
        super(properties);
        this.runeId = runeId;
    }

    public ResourceLocation getRuneId() {
        return runeId;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide()) {
            return InteractionResultHolder.success(stack);
        }

        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);
        if (entityData == null) {
            player.displayClientMessage(Component.literal("No entity data found."), true);
            return InteractionResultHolder.fail(stack);
        }

        if (player.isShiftKeyDown()) {
            RunicPathHelper.clearRunes(entityData);

            if (player instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(
                        serverPlayer,
                        new SyncRunes(java.util.List.of(), java.util.List.of())
                );
            }

            player.displayClientMessage(
                    Component.literal("Cleared Runic Runes"),
                    true
            );

            return InteractionResultHolder.success(stack);
        }

        if (!entityData.hasPath(ModPaths.RUNIC.getId())) {
            player.displayClientMessage(Component.literal("You must walk the Runic Path to comprehend runes."), true);
            return InteractionResultHolder.fail(stack);
        }

        Rune rune = Runes.get(runeId);
        if (rune == null) {
            player.displayClientMessage(Component.literal("This rune is invalid."), true);
            return InteractionResultHolder.fail(stack);
        }

        RunicRuneData runeData = RunicPathHelper.getRuneData(entityData);

        boolean added = runeData.unlockRune(runeId);
        if (!added) {
            player.displayClientMessage(
                    Component.literal("You already comprehend ").append(rune.getDisplayName()).append(Component.literal(".")),
                    true
            );
            return InteractionResultHolder.fail(stack);
        }


        RunicPathHelper.autoSelectRuneIfPossible(entityData, runeData, runeId);
        RunicPathHelper.saveRuneData(entityData, runeData);
        RunicPathHelper.refreshAllRuneSkills(entityData);

        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(
                    serverPlayer,
                    new SyncRunes(
                            new java.util.ArrayList<>(runeData.getUnlockedRunes()),
                            toRealmSelections(runeData)
                    )            );
        }

        player.displayClientMessage(
                Component.literal("You have comprehended ").append(rune.getDisplayName()).append(Component.literal(".")),
                true
        );

        stack.shrink(1);
        return InteractionResultHolder.consume(stack);
    }

    private static List<RealmRuneSelection> toRealmSelections(RunicRuneData runeData) {
        java.util.List<RealmRuneSelection> result = new ArrayList<>();

        for (Map.Entry<Integer, List<ResourceLocation>> entry : runeData.getSelectedRunes().entrySet()) {
            result.add(new RealmRuneSelection(
                    entry.getKey(),
                    new ArrayList<>(entry.getValue())
            ));
        }

        return result;
    }


}