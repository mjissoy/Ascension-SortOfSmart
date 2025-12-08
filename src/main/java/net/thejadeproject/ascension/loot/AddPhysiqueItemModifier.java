package net.thejadeproject.ascension.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.thejadeproject.ascension.events.ModDataComponents;
import net.thejadeproject.ascension.items.ModItems;
import org.jetbrains.annotations.NotNull;

public class AddPhysiqueItemModifier extends LootModifier {
    public static final MapCodec<AddPhysiqueItemModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            codecStart(inst)
                    .and(ResourceLocation.CODEC.fieldOf("physiqueId").forGetter(m -> m.physiqueId))
                    .apply(inst, AddPhysiqueItemModifier::new)
    );

    private final ResourceLocation physiqueId;

    public AddPhysiqueItemModifier(LootItemCondition[] conditionsIn, ResourceLocation physiqueId) {
        super(conditionsIn);
        this.physiqueId = physiqueId;
    }

    // Constructor that takes String for convenience
    public AddPhysiqueItemModifier(LootItemCondition[] conditionsIn, String physiqueId) {
        this(conditionsIn, ResourceLocation.parse(physiqueId));
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ItemStack stack = new ItemStack(ModItems.PHYSIQUE_SLIP.get());
        stack.set(ModDataComponents.PHYSIQUE_ID.get(), physiqueId.toString());
        generatedLoot.add(stack);
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}