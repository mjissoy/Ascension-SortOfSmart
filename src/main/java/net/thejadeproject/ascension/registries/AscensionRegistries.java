package net.thejadeproject.ascension.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.physiques.IPhysique;
import net.thejadeproject.ascension.skills.ISkill;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID,bus = EventBusSubscriber.Bus.MOD)
public class AscensionRegistries {

    public static class Physiques{

        public static final ResourceKey<Registry<IPhysique>> PHYSIQUE_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation
                .fromNamespaceAndPath(AscensionCraft.MOD_ID,"physiques"));
        public static final Registry<IPhysique> PHSIQUES_REGISTRY = new RegistryBuilder<>(PHYSIQUE_REGISTRY_KEY)
                .defaultKey(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"empty"))
                .create();
    }
    public static class Skills{
        public static final ResourceKey<Registry<ISkill>> SKILL_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(
                AscensionCraft.MOD_ID,"skills"
        ));
        public static final Registry<ISkill> SKILL_REGISTRY = new RegistryBuilder<>(SKILL_REGISTRY_KEY)
                .defaultKey(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"empty"))
                .create();
    }
    @SubscribeEvent // on the mod event bus
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(Physiques.PHSIQUES_REGISTRY);
        event.register(Skills.SKILL_REGISTRY);

    }
}
