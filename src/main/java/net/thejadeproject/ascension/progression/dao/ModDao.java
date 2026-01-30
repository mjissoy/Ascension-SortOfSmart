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


    // Primordial Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> PRIMORDIAL_DAO = createDao("primordial",
            Component.literal("§5[Primordial]").withColor(0x800080),
            List.of(
                    Component.literal("The first essence before all things"),
                    Component.literal("The origin of chaos and creation")
            ),
            Map.of(
                    "ascension:chaos", 1.5,
                    "ascension:creation", 1.2
            ),
            Map.of(
                    "ascension:order", 2.0,
                    "ascension:void", 1.5
            )
    );

    // Creation Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> CREATION_DAO = createDao("creation",
            Component.literal("§a[Creation]").withColor(0x32CD32),
            List.of(
                    Component.literal("The force of bringing things into being"),
                    Component.literal("Opposite of destruction, source of life")
            ),
            Map.of(
                    "ascension:life", 1.8,
                    "ascension:light", 1.5,
                    "ascension:wood", 1.3
            ),
            Map.of(
                    "ascension:destruction", 2.0,
                    "ascension:void", 1.8,
                    "ascension:death", 1.5
            )
    );

    // Destruction Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> DESTRUCTION_DAO = createDao("destruction",
            Component.literal("§4[Destruction]").withColor(0x8B0000),
            List.of(
                    Component.literal("The force of ending and unmaking"),
                    Component.literal("Necessary counterpart to creation")
            ),
            Map.of(
                    "ascension:void", 1.5,
                    "ascension:chaos", 1.3
            ),
            Map.of(
                    "ascension:creation", 2.0,
                    "ascension:life", 1.8,
                    "ascension:order", 1.5
            )
    );

    // Devouring Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> DEVOURING_DAO = createDao("devouring",
            Component.literal("§8[Devouring]").withColor(0x000000),
            List.of(
                    Component.literal("The act of consuming all that exists"),
                    Component.literal("Ultimate form of destruction through consumption")
            ),
            Map.of(
                    "ascension:destruction", 1.8,
                    "ascension:void", 1.8,
                    "ascension:chaos", 1.5
            ),
            Map.of(
                    "ascension:creation", 2.2,
                    "ascension:life", 2.0,
                    "ascension:order", 1.8
            )
    );

    // Heaven Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> HEAVEN_DAO = createDao("heaven",
            Component.literal("§b[Heaven]").withColor(0x87CEEB),
            List.of(
                    Component.literal("Divine realm and celestial power"),
                    Component.literal("Source of divine authority and order")
            ),
            Map.of(
                    "ascension:light", 1.8,
                    "ascension:order", 1.5,
                    "ascension:yang", 1.2
            ),
            Map.of(
                    "ascension:chaos", 2.0,
                    "ascension:demonic", 1.8,
                    "ascension:defiance", 1.5
            )
    );

    // Dragon Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> DRAGON_DAO = createDao("dragon",
            Component.literal("§6[Dragon]").withColor(0xFF8C00),
            List.of(
                    Component.literal("Primordial power of ancient dragons"),
                    Component.literal("Mastery over elements and primordial forces")
            ),
            Map.of(
                    "ascension:fire", 1.5,
                    "ascension:water", 1.5,
                    "ascension:earth", 1.5,
                    "ascension:metal", 1.3,
                    "ascension:primordial", 1.2
            ),
            Map.of(
                    "ascension:order", 1.8,  // Dragons are chaotic by nature
                    "ascension:law", 2.0     // Dragons defy mortal laws
            )
    );

    // Space Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> SPACE_DAO = createDao("space",
            Component.literal("§3[Space]").withColor(0x008B8B),
            List.of(
                    Component.literal("Manipulation of spatial dimensions"),
                    Component.literal("Control over distance and location")
            ),
            Map.of(
                    "ascension:void", 1.8,
                    "ascension:light", 1.2
            ),
            Map.of(
                    "ascension:earth", 1.5,  // Earth grounds space
                    "ascension:metal", 1.3   // Metal contains space
            )
    );

    // Darkness Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> DARKNESS_DAO = createDao("darkness",
            Component.literal("§8[Darkness]").withColor(0x000000),
            List.of(
                    Component.literal("The absence of light, shadow essence"),
                    Component.literal("Power of concealment and stealth")
            ),
            Map.of(
                    "ascension:shadow", 1.8,
                    "ascension:yin", 1.5,
                    "ascension:void", 1.2
            ),
            Map.of(
                    "ascension:light", 2.0,
                    "ascension:sun", 2.0,
                    "ascension:yang", 1.8
            )
    );

    // Sun Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> SUN_DAO = createDao("sun",
            Component.literal("§e[Sun]").withColor(0xFFD700),
            List.of(
                    Component.literal("Solar power and light essence"),
                    Component.literal("Source of life and warmth")
            ),
            Map.of(
                    "ascension:fire", 1.8,
                    "ascension:light", 2.0,
                    "ascension:yang", 1.8,
                    "ascension:life", 1.5
            ),
            Map.of(
                    "ascension:moon", 2.0,
                    "ascension:darkness", 2.0,
                    "ascension:yin", 1.8,
                    "ascension:ice", 1.5
            )
    );

    // Light Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> LIGHT_DAO = createDao("light",
            Component.literal("§f[Light]").withColor(0xFFFFFF),
            List.of(
                    Component.literal("Pure illumination and clarity"),
                    Component.literal("Dispels darkness and reveals truth")
            ),
            Map.of(
                    "ascension:sun", 1.5,
                    "ascension:yang", 1.2,
                    "ascension:order", 1.0
            ),
            Map.of(
                    "ascension:darkness", 2.0,
                    "ascension:shadow", 2.0,
                    "ascension:yin", 1.8
            )
    );

    // Moon Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> MOON_DAO = createDao("moon",
            Component.literal("§b[Moon]").withColor(0x87CEFA),
            List.of(
                    Component.literal("Lunar power and reflection"),
                    Component.literal("Control over tides and dreams")
            ),
            Map.of(
                    "ascension:yin", 1.8,
                    "ascension:water", 1.5,
                    "ascension:shadow", 1.2,
                    "ascension:ice", 1.0
            ),
            Map.of(
                    "ascension:sun", 2.0,
                    "ascension:fire", 1.8,
                    "ascension:light", 1.5
            )
    );

    // Shadow Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> SHADOW_DAO = createDao("shadow",
            Component.literal("§8[Shadow]").withColor(0x696969),
            List.of(
                    Component.literal("Manipulation of shadows and stealth"),
                    Component.literal("Power of concealment and assassination")
            ),
            Map.of(
                    "ascension:darkness", 1.8,
                    "ascension:yin", 1.5,
                    "ascension:void", 1.2
            ),
            Map.of(
                    "ascension:light", 2.0,
                    "ascension:sun", 2.0,
                    "ascension:fire", 1.5
            )
    );

    // Spirit Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> SPIRIT_DAO = createDao("spirit",
            Component.literal("§d[Spirit]").withColor(0xDA70D6),
            List.of(
                    Component.literal("Ethereal essence and spiritual power"),
                    Component.literal("Connection to the spirit realm")
            ),
            Map.of(
                    "ascension:soul", 1.8,
                    "ascension:life", 1.5,
                    "ascension:void", 1.0
            ),
            Map.of(
                    "ascension:body", 2.0,  // Spirit vs Physical
                    "ascension:earth", 1.8  // Earth grounds spirits
            )
    );

    // Demonic Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> DEMONIC_DAO = createDao("demonic",
            Component.literal("§4[Demonic]").withColor(0x8B0000),
            List.of(
                    Component.literal("Infernal power and demonic essence"),
                    Component.literal("Corruption and dark power")
            ),
            Map.of(
                    "ascension:chaos", 1.8,
                    "ascension:blood", 1.5,
                    "ascension:fire", 1.3,
                    "ascension:defiance", 1.2
            ),
            Map.of(
                    "ascension:heaven", 2.0,
                    "ascension:light", 1.8,
                    "ascension:order", 1.5,
                    "ascension:purity", 2.0
            )
    );

    // Cosmos Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> COSMOS_DAO = createDao("cosmos",
            Component.literal("§5[Cosmos]").withColor(0x9400D3),
            List.of(
                    Component.literal("The universe as a harmonious system"),
                    Component.literal("Celestial bodies and cosmic forces")
            ),
            Map.of(
                    "ascension:space", 1.8,
                    "ascension:time", 1.5,
                    "ascension:order", 1.2,
                    "ascension:balance", 1.0
            ),
            Map.of(
                    "ascension:chaos", 2.0,
                    "ascension:void", 1.8
            )
    );

    // Time Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> TIME_DAO = createDao("time",
            Component.literal("§3[Time]").withColor(0x008080),
            List.of(
                    Component.literal("Control over temporal flow"),
                    Component.literal("Manipulation of past, present, and future")
            ),
            Map.of(
                    "ascension:space", 1.5,  // Spacetime connection
                    "ascension:reincarnation", 1.2,
                    "ascension:memory", 1.0
            ),
            Map.of(
                    "ascension:chaos", 2.0,  // Chaos disrupts time
                    "ascension:void", 1.8    // Void is timeless
            )
    );

    // Memory Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> MEMORY_DAO = createDao("memory",
            Component.literal("§d[Memory]").withColor(0xBA55D3),
            List.of(
                    Component.literal("Power of remembrance and recollection"),
                    Component.literal("Storage and retrieval of experiences")
            ),
            Map.of(
                    "ascension:soul", 1.5,
                    "ascension:spirit", 1.2,
                    "ascension:reincarnation", 1.0
            ),
            Map.of(
                    "ascension:void", 2.0,    // Void erases memory
                    "ascension:chaos", 1.8    // Chaos scrambles memory
            )
    );

    // Sharpness Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> SHARPNESS_DAO = createDao("sharpness",
            Component.literal("§7[Sharpness]").withColor(0xC0C0C0),
            List.of(
                    Component.literal("The quality of being sharp"),
                    Component.literal("Cutting edge and precision")
            ),
            Map.of(
                    "ascension:metal", 1.8,
                    "ascension:sword_intent", 1.5,
                    "ascension:blade_intent", 1.3
            ),
            Map.of(
                    "ascension:earth", 1.5,   // Earth dulls edges
                    "ascension:water", 1.3    // Water erodes sharpness
            )
    );
    // precision Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> PRECISION_DAO = createDao("precision",
            Component.literal("§7[Sharpness]").withColor(0xC0C0C0),
            List.of(
                    Component.literal("under your dao no one can escape")
            ),
            Map.of(
                    "ascension:metal", 1.8,
                    "ascension:bow_intent", 1.5,
                    "ascension:spear_intent", 1.3
            ),
            Map.of(
                    "ascension:earth", 1.5,   // Earth dulls edges
                    "ascension:water", 1.3    // Water erodes sharpness
            )
    );

    // Cutting Dao (from physique references)
    public static final DeferredHolder<IDao,GenericDao> CUTTING_DAO = createDao("cutting",
            Component.literal("§8[Cutting]").withColor(0x808080),
            List.of(
                    Component.literal("The act of dividing or severing"),
                    Component.literal("Separation and division")
            ),
            Map.of(
                    "ascension:sharpness", 1.8,
                    "ascension:sword_intent", 1.5,
                    "ascension:blade_intent", 1.3,
                    "ascension:metal", 1.2
            ),
            Map.of(
                    "ascension:earth", 1.8,   // Earth resists cutting
                    "ascension:wood", 1.5     // Wood can be cut but resists
            )
    );



    public static final DeferredHolder<IDao, GenericDao> STORM_DAO = createDao("storm",
            Component.literal("§9[Storm]").withColor(0x4A90E2),
            List.of(
                    Component.literal("The fury of wind and lightning combined"),
                    Component.literal("Unpredictable and overwhelming natural force")
            ),
            Map.of(
                    "ascension:wind", 1.5,
                    "ascension:lightning", 1.5,
                    "ascension:chaos", 1.2
            ),
            Map.of(
                    "ascension:earth", 1.8,
                    "ascension:metal", 1.5
            )
    );

    public static final DeferredHolder<IDao, GenericDao> LIGHTNING_DAO = createDao("lightning",
            Component.literal("§e[Lightning]").withColor(0xFFD700),
            List.of(
                    Component.literal("Heaven's wrath made manifest"),
                    Component.literal("Instantaneous power and divine punishment")
            ),
            Map.of(
                    "ascension:yang", 1.8,
                    "ascension:void", 0.3  // Lightning bridges emptiness
            ),
            Map.of(
                    "ascension:water", 2.0,
                    "ascension:metal", 1.5
            )
    );

    public static final DeferredHolder<IDao, GenericDao> WIND_DAO = createDao("wind",
            Component.literal("§7[Wind]").withColor(0xA0A0A0),
            List.of(
                    Component.literal("Freedom without boundaries"),
                    Component.literal("Gentle breeze or howling tempest")
            ),
            Map.of(
                    "ascension:void", 1.2,
                    "ascension:yang", 0.8
            ),
            Map.of(
                    "ascension:earth", 1.5
            )
    );

    public static final DeferredHolder<IDao, GenericDao> SOUL_DAO = createDao("soul",
            Component.literal("§d[Soul]").withColor(0xDA70D6),
            List.of(
                    Component.literal("Essence of consciousness and spirit"),
                    Component.literal("Connection to the spiritual realm")
            ),
            Map.of(
                    "ascension:reincarnation", 1.5,
                    "ascension:yin", 1.2
            ),
            Map.of(
                    "ascension:void", 0.8  // Soul resists emptiness
            )
    );

    public static final DeferredHolder<IDao, GenericDao> BLOOD_DAO = createDao("blood",
            Component.literal("§4[Blood]").withColor(0x8B0000),
            List.of(
                    Component.literal("Life essence and vitality"),
                    Component.literal("Sacrificial power and lineage")
            ),
            Map.of(
                    "ascension:fire", 1.2,
                    "ascension:life", 1.0
            ),
            Map.of(
                    "ascension:ice", 1.8,
                    "ascension:purity", 2.0
            )
    );

    public static final DeferredHolder<IDao, GenericDao> LIFE_DAO = createDao("life",
            Component.literal("§a[Life]").withColor(0x32CD32),
            List.of(
                    Component.literal("Creation and vitality"),
                    Component.literal("Healing and growth essence")
            ),
            Map.of(
                    "ascension:wood", 1.5,
                    "ascension:water", 1.2
            ),
            Map.of(
                    "ascension:death", 2.0,
                    "ascension:void", 1.8
            )
    );

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
                    "ascension:water",2D
            ),
            Map.of("ascension:metal",2D),
            Map.of("ascension:nature",0.5D)

    );
    public static final DeferredHolder<IDao,GenericDao> NATURE_DAO = createDao("nature",Component.literal("§2[Nature]"),
            new ArrayList<>());
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
