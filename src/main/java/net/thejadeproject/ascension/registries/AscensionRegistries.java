package net.thejadeproject.ascension.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.progression.dao.IDao;
import net.thejadeproject.ascension.progression.physiques.IPhysique;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.progression.techniques.ITechnique;

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
    public static class Techniques{
        public static final ResourceKey<Registry<ITechnique>> TECHNIQUES_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(
                AscensionCraft.MOD_ID,"techniques"
        ));
        public static final Registry<ITechnique> TECHNIQUES_REGISTRY = new RegistryBuilder<>(TECHNIQUES_REGISTRY_KEY)
                .defaultKey(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"empty"))
                .create();
    }
    public static class Dao{
        public static final ResourceKey<Registry<IDao>> DAO_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation
                .fromNamespaceAndPath(AscensionCraft.MOD_ID,"dao"));
        public static final Registry<IDao> DAO_REGISTRY = new RegistryBuilder<>(DAO_REGISTRY_KEY)
                .defaultKey(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"empty"))
                .create();
    }

    @SubscribeEvent // on the mod event bus
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(Physiques.PHSIQUES_REGISTRY);
        event.register(Skills.SKILL_REGISTRY);
        event.register(Techniques.TECHNIQUES_REGISTRY);
        event.register(Dao.DAO_REGISTRY);

    }
}
