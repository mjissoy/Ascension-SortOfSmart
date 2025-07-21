package net.thejadeproject.ascension.progression.dao;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.registries.AscensionRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO auto generate TagKey from this
/*TODO
    add some sort of Dao mastery. give it its own progression system and realm.
    have a dao progenitor? only 1 per dao and
    as long as someone is a dao progenitor others cannot progress the final stage stuck at 99.9%
 */
public class ModDao {
    public static final DeferredRegister<IDao> DAO = DeferredRegister.create(AscensionRegistries.Dao.DAO_REGISTRY, AscensionCraft.MOD_ID);

    public static final DeferredHolder<IDao,GenericDao> FIRE_DAO =createDao("fire",Component.literal("§4[Fire]"),
            new ArrayList<>(),
            Map.of(
                    "ascension:metal",0.5D,
                    "ascension:earth",2D
            ),
            Map.of(
                    "ascension:yang",1.8
            ));
    public static final DeferredHolder<IDao, GenericDao> PHOENIX_FIRE = createDao("phoenix_fire",Component.literal("[Phoenix Fire]").withColor(-37120),
            List.of(
                    Component.literal("flames born from a phoenix, possess a hint of reincarnation")
            ),
            Map.of(),
            Map.of(
                    "ascension:fire",1.0D,
                    "ascension:reincarnation",1.01D
            ));
    public static final DeferredHolder<IDao,GenericDao> WATER_DAO =createDao("water",Component.literal("§9[Water]"),
            new ArrayList<>(),
            Map.of(
                "ascension:fire",0.5D,
                "ascension:wood",2D

            ),
            Map.of(
                    "ascension:yin",1.2
            ));
    public static final DeferredHolder<IDao,GenericDao> WOOD_DAO =createDao("wood",Component.literal("§2[Wood]"),
            new ArrayList<>(),
            Map.of(
                    "ascension:earth",0.5D,
                    "ascension:fire",2D
            ));
    public static final DeferredHolder<IDao,GenericDao> EARTH_DAO =createDao("earth",Component.literal("§e[Earth]"),
            new ArrayList<>(),
            Map.of(
                    "ascension:water",0.5D,
                    "ascension:metal",2D
            ));
    public static final DeferredHolder<IDao,GenericDao> METAL_DAO =createDao("metal",Component.literal("§f[Metal]]"),
            new ArrayList<>(),
            Map.of(
                    "ascension:wood",0.5D,
                    "ascension:water",2D
            ));


    public static final DeferredHolder<IDao,GenericDao> ICE_DAO =createDao("ice",Component.literal("§b[Ice]"),
            new ArrayList<>(),
            Map.of(
            ),
            Map.of(
                    "ascension:yin",1.8,
                    "ascension:water",1.4
            ));

    public static final DeferredHolder<IDao, GenericDao> Poison_DAO = createDao("poison",Component.literal("§5[Poison]"));


    public static final DeferredHolder<IDao,GenericDao> REINCARNATION_DAO = createDao("reincarnation",Component.literal("§3[Reincarnation]"),
            List.of(
                    Component.literal("a vague and fleeting concept")
            ));

    public static final DeferredHolder<IDao, GenericDao> YIN_DAO = createDao("yin",Component.literal("§b[Yin]"));
    public static final DeferredHolder<IDao, GenericDao> YANG_DAO = createDao("yang",Component.literal("§4[Yang]"));


    public static final DeferredHolder<IDao,GenericDao> SWORD_DAO = createDao("sword_intent",Component.literal("§8[Sword Intent]"));
    public static final DeferredHolder<IDao,GenericDao> FIST_DAO = createDao("fist_intent",Component.literal("§8[Fist Intent]"));
    public static final DeferredHolder<IDao,GenericDao> BLADE_DAO = createDao("blade_intent",Component.literal("§8[Blade Intent]"));
    public static final DeferredHolder<IDao,GenericDao> BOW_DAO = createDao("bow_intent",Component.literal("§8[Bow Intent]"));
    public static final DeferredHolder<IDao,GenericDao> SPEAR_DAO = createDao("spear_intent",Component.literal("§8[Spear Intent]"));

    public static DeferredHolder<IDao,GenericDao> createDao(String id, Component title){
        return createDao(id,title,new ArrayList<>(),Map.of(),Map.of());
    }
    public static DeferredHolder<IDao,GenericDao> createDao(String id, Component title, List<MutableComponent> description){
        return createDao(id,title,description,Map.of(),Map.of());
    }
    public static DeferredHolder<IDao,GenericDao> createDao(String id, Component title,  List<MutableComponent> description,Map<String ,Double> interactions){
        return createDao(id,title,description,interactions,Map.of());
    }
    public static DeferredHolder<IDao,GenericDao> createDao(String id, Component title,  List<MutableComponent> description,Map<String ,Double> interactions,Map<String ,Double> relatedDao){
        return DAO.register(id,()->new GenericDao(title).setInteractions(interactions).setRelatedDao(relatedDao).setDescription(description));
    }

    public static void register(IEventBus modEventBus){
        DAO.register(modEventBus);
    }
}
