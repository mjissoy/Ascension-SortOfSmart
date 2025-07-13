package net.thejadeproject.ascension.physiques;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.registries.AscensionRegistries;

import java.util.HashMap;

public class ModPhysiques {

    public static final DeferredRegister<IPhysique> PHYSIQUES =DeferredRegister.create(AscensionRegistries.Physiques.PHSIQUES_REGISTRY, AscensionCraft.MOD_ID);


    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_SWORD_BODY = PHYSIQUES.register("pure_sword_body",
            ()-> new GenericPhysique("Pure Sword Body",
                    new HashMap<>(){{
                        put("Intent",1.0);
                        put("Body",0.4);
                        put("Essence",0.1);
                    }},
                    new HashMap<>(){{
                        put("Sword",3.0);
                    }})
            );
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_SPEAR_BODY = PHYSIQUES.register("pure_spear_body",
            ()-> new GenericPhysique("Pure Spear Body",
                    new HashMap<>(){{
                        put("Intent",1.0);
                        put("Body",0.4);
                        put("Essence",0.1);
                    }},
                    new HashMap<>(){{
                        put("Spear",3.0);
                    }})
    );
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_BOW_BODY = PHYSIQUES.register("pure_bow_body",
            ()-> new GenericPhysique("Pure Bow Body",
                    new HashMap<>(){{
                        put("Intent",1.0);
                        put("Body",0.4);
                        put("Essence",0.1);
                    }},
                    new HashMap<>(){{
                        put("Bow",3.0);
                    }})
    );
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_BLADE_BODY = PHYSIQUES.register("pure_blade_body",
            ()-> new GenericPhysique("Pure Blade Body",
                    new HashMap<>(){{
                        put("Intent",1.0);
                        put("Body",0.4);
                        put("Essence",0.1);
                    }},
                    new HashMap<>(){{
                        put("Blade",3.0);
                    }})
    );
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_FIST_BODY = PHYSIQUES.register("pure_fist_body",
            ()-> new GenericPhysique("Pure Fist Body",
                    new HashMap<>(){{
                        put("Intent",1.0);
                        put("Body",0.4);
                        put("Essence",0.1);
                    }},
                    new HashMap<>(){{
                        put("Fist",3.0);
                    }})
    );

    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_WATER_BODY = PHYSIQUES.register("pure_water_body",
            ()-> new GenericPhysique("Pure Water Body",
                    new HashMap<>(){{
                        put("Intent",0.4);
                        put("Body",0.1);
                        put("Essence",1.0);
                    }},
                    new HashMap<>(){{
                        put("Water",3.0);
                    }})
    );
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_EARTH_BODY = PHYSIQUES.register("pure_earth_body",
            ()-> new GenericPhysique("Pure Earth Body",
                    new HashMap<>(){{
                        put("Intent",0.4);
                        put("Body",0.1);
                        put("Essence",1.0);
                    }},
                    new HashMap<>(){{
                        put("Earth",3.0);
                    }})
    );
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_FIRE_BODY = PHYSIQUES.register("pure_fire_body",
            ()-> new GenericPhysique("Pure Fire Body",
                    new HashMap<>(){{
                        put("Intent",0.4);
                        put("Body",0.1);
                        put("Essence",1.0);
                    }},
                    new HashMap<>(){{
                        put("Fire",3.0);
                    }})
    );
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_WOOD_BODY = PHYSIQUES.register("pure_wood_body",
            ()-> new GenericPhysique("Pure Wood Body",
                    new HashMap<>(){{
                        put("Intent",0.4);
                        put("Body",0.1);
                        put("Essence",1.0);
                    }},
                    new HashMap<>(){{
                        put("Wood",3.0);
                    }})
    );
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_METAL_BODY = PHYSIQUES.register("pure_metal_body",
            ()-> new GenericPhysique("Pure Metal Body",
                    new HashMap<>(){{
                        put("Intent",0.4);
                        put("Body",0.1);
                        put("Essence",1.0);
                    }},
                    new HashMap<>(){{
                        put("Metal",3.0);
                    }})
    );

    public static final DeferredHolder<IPhysique,GenericPhysique> IRON_BONE_PHYSIQUE = PHYSIQUES.register("iron_bone_physique",
            ()-> new GenericPhysique("Iron Bone Physique",
                    new HashMap<>(){{
                        put("Intent",0.1);
                        put("Body",1.0);
                        put("Essence",0.4);
                    }},
                    new HashMap<>(){{
                        put("Metal",1.2);
                    }})
    );

    public static final DeferredHolder<IPhysique,GenericPhysique> BURNING_SKIN_PHYSIQUE = PHYSIQUES.register("burning_skin_physique",
            ()-> new GenericPhysique("Burning Skin Physique",
                    new HashMap<>(){{
                        put("Intent",0.1);
                        put("Body",1.0);
                        put("Essence",0.4);
                    }},
                    new HashMap<>(){{
                        put("Fire",1.2);
                    }})
    );
    public static final DeferredHolder<IPhysique,GenericPhysique> HUNDRED_POISON_PHYSIQUE = PHYSIQUES.register("hundred_poison_physique",
            ()-> new GenericPhysique("Hundred Poison Physique",
                    new HashMap<>(){{
                        put("Intent",0.1);
                        put("Body",1.0);
                        put("Essence",0.4);
                    }},
                    new HashMap<>(){{
                        put("Poison",1.2);
                    }})
    );

    public static final DeferredHolder<IPhysique,GenericPhysique> SACRED_SAPLING_PHYSIQUE = PHYSIQUES.register("sacred_sapling_physique",
            ()-> new GenericPhysique("Sacred Sapling Physique",
                    new HashMap<>(){{
                        put("Intent",0.1);
                        put("Body",1.0);
                        put("Essence",0.4);
                    }},
                    new HashMap<>(){{
                        put("Wood",1.2);
                    }})
    );

    public static final DeferredHolder<IPhysique,GenericPhysique> SUPPRESSED_YIN_PHYSIQUE = PHYSIQUES.register("suppressed_yin_physique",
            ()-> new GenericPhysique("Suppressed Yin Physique",
                    new HashMap<>(){{
                        put("Intent",0.1);
                        put("Body",1.0);
                        put("Essence",0.4);
                    }},
                    new HashMap<>(){{
                        put("Yin",1.2);
                        put("Ice",1.2);

                    }})
    );
    public static void register(IEventBus modEventBus){
        PHYSIQUES.register(modEventBus);
    }
}
