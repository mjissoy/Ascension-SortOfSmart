package net.thejadeproject.ascension.refactor_packages.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.progression.physiques.IPhysique;
import net.thejadeproject.ascension.refactor_packages.attributes.AscensionAttribute;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.stats.Stat;
@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class AscensionRegistries {
    public static class Stats{
        public static final ResourceKey<Registry<Stat>> STAT_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation
                .fromNamespaceAndPath(AscensionCraft.MOD_ID,"stats"));
        public static final Registry<Stat> STATS_REGISTRY = new RegistryBuilder<>(STAT_REGISTRY_KEY)
                .create();
    }
    public static class EntityForms{
        public static final ResourceKey<Registry<IEntityForm>> ENTITY_FORM_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation
                .fromNamespaceAndPath(AscensionCraft.MOD_ID,"entity_forms"));
        public static final Registry<IEntityForm> ENTITY_FORMS_REGISTRY = new RegistryBuilder<>(ENTITY_FORM_REGISTRY_KEY)
                .create();
    }

    @SubscribeEvent // on the mod event bus
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(Stats.STATS_REGISTRY);
        event.register(EntityForms.ENTITY_FORMS_REGISTRY);

    }
}
