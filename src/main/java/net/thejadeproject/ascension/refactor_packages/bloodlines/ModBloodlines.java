package net.thejadeproject.ascension.refactor_packages.bloodlines;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.bloodlines.custom.GenericBloodline;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttributes;

public class ModBloodlines {

    public static final DeferredRegister<IBloodline> BLOODLINES =
            DeferredRegister.create(AscensionRegistries.Bloodlines.BLOODLINE_REGISTRY, AscensionCraft.MOD_ID);

    // ── Modifier keys ────────────────────────────────────────────────────────
    private static final ResourceLocation PHOENIX_KEY = rl("phoenix_bloodline");

    // ── Registrations ────────────────────────────────────────────────────────

    /**
     * Mortal Bloodline nothing special just regular
     */
    public static final DeferredHolder<IBloodline, GenericBloodline> MORTAL_BLOODLINE =
            BLOODLINES.register("mortal_bloodline", () ->
                    new GenericBloodline(Component.translatable("ascension.bloodline.mortal_bloodline"))
                            .setShortDescription(Component.translatable("ascension.bloodline.mortal_bloodline.short"))
                            .setDescription(Component.translatable("ascension.bloodline.mortal_bloodline.desc"))
            );


    /*
    Phoenix Bloodline This is just an example for now. What do you guys think?
     */
    public static final DeferredHolder<IBloodline, GenericBloodline> PHOENIX_BLOODLINE =
            BLOODLINES.register("phoenix_bloodline", () ->
                            new GenericBloodline(Component.translatable("ascension.bloodline.phoenix_bloodline"))
                                    .setShortDescription(Component.translatable("ascension.bloodline.phoenix_bloodline.short"))
                                    .setDescription(Component.translatable("ascension.bloodline.phoenix_bloodline.desc"))
                                    .addFlatAttribute(ModAttributes.MAX_QI,          100, PHOENIX_KEY)
                                    .addFlatAttribute(ModAttributes.QI_REGEN_RATE,   0.5, PHOENIX_KEY)
                                    .addFlatAttribute(Attributes.MOVEMENT_SPEED,     0.04, PHOENIX_KEY)
                                    .addFlatAttribute(Attributes.MAX_HEALTH,         20,  PHOENIX_KEY)
                    // .addInherentSkill(ModSkills.PHOENIX_REBIRTH_PASSIVE.getId())
            );


    // ── Registration ─────────────────────────────────────────────────────────

    public static void register(IEventBus modEventBus) {
        BLOODLINES.register(modEventBus);
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, path);
    }
}
