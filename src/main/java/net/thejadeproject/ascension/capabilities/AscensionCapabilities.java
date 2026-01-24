package net.thejadeproject.ascension.capabilities;

import net.lucent.formation_arrays.FormationArrays;
import net.lucent.formation_arrays.api.capability.IFormationHolder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.jetbrains.annotations.Nullable;

public class AscensionCapabilities {


    public static final ItemCapability<IPlayerFilter, @Nullable Void> PLAYER_FILTER_CAPABILITY = ItemCapability.createVoid(
            ResourceLocation.fromNamespaceAndPath(FormationArrays.MOD_ID,"player_filter"), IPlayerFilter.class);


}
