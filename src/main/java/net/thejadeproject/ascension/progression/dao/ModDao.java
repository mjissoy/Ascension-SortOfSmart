package net.thejadeproject.ascension.progression.dao;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModTags;

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


    public static final DeferredHolder<IDao, GenericDao> CHAOS_DAO = createDao("chaos",
            Component.literal("§5[Chaos]"),
            List.of(
                    Component.literal("Primordial disorder, the state before creation"),
                    Component.literal("The essence of rebellion against established order")
            ),
            Map.of(), // No generative
            Map.of( // Destructive to order-based Daos
                    "ascension:yin", 2.0,
                    "ascension:yang", 2.0,
                    "ascension:order", 1.8
            )
    );

    public static final DeferredHolder<IDao, GenericDao> DEFIANCE_DAO = createDao("defiance",
            Component.literal("§c[Defiance]"),
            List.of(
                    Component.literal("The will to resist all authority"),
                    Component.literal("Strength born from opposition")
            ),
            Map.of(),
            Map.of(
                    "ascension:order", 2.0,
                    "ascension:karma", 1.5
            )
    );

    public static final DeferredHolder<IDao, GenericDao> EARTHSHATTER_DAO = createDao("earthshatter",
            Component.literal("§6[Earthshatter]"),
            List.of(
                    Component.literal("The power to break mountains with a single strike"),
                    Component.literal("Unstoppable force meeting immovable object")
            ),
            Map.of(
                    "ascension:earth", 1.5,
                    "ascension:yang", 1.2
            ),
            Map.of(
                    "ascension:void", 0.5
            )
    );

    public static final DeferredHolder<IDao, GenericDao> TRANSFORMATION_DAO = createDao("transformation",
            Component.literal("§3[Transformation]"),
            List.of(
                    Component.literal("Mastery over form and essence"),
                    Component.literal("The fluidity to become anything")
            ),
            Map.of(
                    "ascension:chaos", 1.2,
                    "ascension:yin", 0.8
            )
    );

    public static final DeferredHolder<IDao, GenericDao> ORDER_DAO = createDao("order",
            Component.literal("§f[Order]").withColor(0xAAAAAA),
            List.of(
                    Component.literal("The principle of structure, law, and harmony"),
                    Component.literal("Natural balance and cosmic hierarchy"),
                    Component.literal("Opposite of chaos and defiance")
            ),
            Map.of(
                    "ascension:yin", 1.2,    // Balance
                    "ascension:yang", 1.2,   // Structure
                    "ascension:earth", 0.8,  // Stability
                    "ascension:metal", 0.5   // Rigidity
            ),
            Map.of(
                    "ascension:chaos", 2.0,      // Order suppresses chaos
                    "ascension:defiance", 1.8,   // Law suppresses rebellion
                    "ascension:void", 0.5        // Order fills emptiness
            ),
            Map.of( // Related Daos
                    "ascension:balance", 1.5,
                    "ascension:law", 1.2
            )
    );

    public static final DeferredHolder<IDao, GenericDao> KARMA_DAO = createDao("karma",
            Component.literal("§d[Karma]").withColor(0x9932CC),
            List.of(
                    Component.literal("Cosmic justice and retribution"),
                    Component.literal("Actions and their consequences"),
                    Component.literal("Heaven's judgment and balance")
            ),
            Map.of(
                    "ascension:order", 1.5,     // Karma maintains order
                    "ascension:yin", 1.0,       // Balance
                    "ascension:reincarnation", 0.8  // Related to rebirth
            ),
            Map.of(
                    "ascension:defiance", 1.8,  // Karma punishes defiance
                    "ascension:chaos", 1.5      // Karma opposes chaos
            )
    );

    public static final DeferredHolder<IDao, GenericDao> LAW_DAO = createDao("law",
            Component.literal("§7[Law]").withColor(0x696969),
            List.of(
                    Component.literal("Codified rules and regulations"),
                    Component.literal("Social structure and governance")
            ),
            Map.of(
                    "ascension:order", 1.2,
                    "ascension:metal", 0.8
            ),
            Map.of(
                    "ascension:chaos", 1.5,
                    "ascension:defiance", 1.5
            )
    );

    public static final DeferredHolder<IDao, GenericDao> BALANCE_DAO = createDao("balance",
            Component.literal("§b[Balance]").withColor(0x87CEEB),
            List.of(
                    Component.literal("Equilibrium between opposing forces"),
                    Component.literal("Harmony and moderation")
            ),
            Map.of(
                    "ascension:yin", 1.0,
                    "ascension:yang", 1.0,
                    "ascension:order", 0.8
            ),
            Map.of(
                    "ascension:chaos", 1.2,
                    "ascension:void", 0.5  // Balance can't exist in emptiness
            )
    );











    public static final DeferredHolder<IDao,GenericDao> FIRE_DAO =createDao("fire",Component.literal("§4[Fire]"),
            new ArrayList<>(),
            Map.of(
                    "ascension:metal", 0.5D,
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
                    "ascension:reincarnation",0.01D
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
    public static final DeferredHolder<IDao,GenericDao> METAL_DAO =createDao("metal",Component.literal("§f[Metal]"),
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

    public static final DeferredHolder<IDao,GenericDao> VOID_DAO =createDao("void",Component.literal("§5[Void]"),
            new ArrayList<>(),
            Map.of(
            ),
            Map.of(
                    "ascension:yang",2.3,
                    "ascension:yin",1.9
            ));

    public static final DeferredHolder<IDao, GenericDao> POISON_DAO = createDao("poison",Component.literal("§5[Poison]"));


    public static final DeferredHolder<IDao,GenericDao> REINCARNATION_DAO = createDao("reincarnation",Component.literal("§3[Reincarnation]"),
            List.of(
                    Component.literal("a vague and fleeting concept")
            ));

    public static final DeferredHolder<IDao, GenericDao> YIN_DAO = createDao("yin",Component.literal("§b[Yin]"));
    public static final DeferredHolder<IDao, GenericDao> YANG_DAO = createDao("yang",Component.literal("§4[Yang]"));
    public static final DeferredHolder<IDao,GenericDao> SWORD_DAO = createDao("sword_intent",Component.literal("§8[Sword Intent]"));
    public static final DeferredHolder<IDao,GenericDao> FIST_DAO = createDao("fist_intent",Component.literal("§8[Fist Intent]"));
    public static final DeferredHolder<IDao,GenericDao> BLADE_DAO = createDao("blade_intent",Component.literal("§8[Blade Intent]"));
    public static final DeferredHolder<IDao,GenericDao> AXE_DAO = createDao("axe_intent",Component.literal("§8[Axe Intent]"));
    public static final DeferredHolder<IDao,GenericDao> SPEAR_DAO = createDao("spear_intent",Component.literal("§8[Spear Intent]"));
    public static final DeferredHolder<IDao,GenericDao> BOW_DAO = createDao("bow_intent",Component.literal("§8[Bow Intent]"));
    public static DeferredHolder<IDao,GenericDao> createDao(String id, Component title){
        return createDao(id,title,new ArrayList<>(),Map.of(),Map.of());
    }
    public static DeferredHolder<IDao,GenericDao> createDao(String id, Component title, List<Component> description){
        return createDao(id,title,description,Map.of(),Map.of());
    }
    public static DeferredHolder<IDao,GenericDao> createDao(String id, Component title,  List<Component> description,Map<String ,Double> generativeDao){
        return createDao(id,title,description,generativeDao,Map.of());
    }
    public static DeferredHolder<IDao,GenericDao> createDao(String id, Component title,  List<Component> description,Map<String ,Double> generativeDao,Map<String ,Double> destructiveDao){
        ModTags.Items.createDaoTag(id);

        return createDao(id,title,description,generativeDao,destructiveDao,Map.of());
    }
    public static DeferredHolder<IDao,GenericDao> createDao(String id, Component title,  List<Component> description,Map<String ,Double> generativeDao,Map<String ,Double> destructiveDao,Map<String,Double> relatedDao){
        ModTags.Items.createDaoTag(id);

        return DAO.register(id,()->new GenericDao(title).setDestructiveDao(destructiveDao).setGenerativeDao(generativeDao).setRelatedDao(relatedDao).setDescription(description));
    }



    public static void register(IEventBus modEventBus){
        DAO.register(modEventBus);
    }
}
