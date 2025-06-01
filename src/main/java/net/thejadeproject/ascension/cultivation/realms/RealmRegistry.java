package net.thejadeproject.ascension.cultivation.realms;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;

import java.security.InvalidKeyException;
import java.util.HashMap;

public class RealmRegistry {
    public static final ResourceKey<Registry<Realm>> REALM_REGISTRY_KEY = ResourceKey.createRegistryKey(AscensionCraft.prefix("realms"));
    public static final ResourceKey<Registry<SubRealm>> SUB_REALM_REGISTRY_KEY = ResourceKey.createRegistryKey(AscensionCraft.prefix("subrealm"));
    private static final DeferredRegister<Realm> REALMS = DeferredRegister.create(REALM_REGISTRY_KEY, AscensionCraft.MOD_ID);
    private static final DeferredRegister<SubRealm> SUB_REALMS = DeferredRegister.create(SUB_REALM_REGISTRY_KEY, AscensionCraft.MOD_ID);

    public static DeferredHolder<Realm, Realm> HEAVEN_QI_REFINING;
    public static DeferredHolder<Realm, Realm> EARTH_QI_REFINING;
    public static DeferredHolder<SubRealm, SubRealm> QI_REFINING_1;
    public static DeferredHolder<SubRealm, SubRealm> QI_REFINING_2;
    public static DeferredHolder<SubRealm, SubRealm> QI_REFINING_3;
    public static DeferredHolder<SubRealm, SubRealm> QI_REFINING_4;

    static {
        QI_REFINING_1 = SUB_REALMS.register("realm_1_1", location -> new SubRealm(location.getPath(), 100));
        QI_REFINING_2 = SUB_REALMS.register("realm_1_2", location -> new SubRealm(location.getPath(), 200));
        QI_REFINING_3 = SUB_REALMS.register("realm_1_3", location -> new SubRealm(location.getPath(), 300));
        QI_REFINING_4 = SUB_REALMS.register("realm_1_4", location -> new SubRealm(location.getPath(), 400));
        HEAVEN_QI_REFINING = REALMS.register("heaven_qi_refining", location -> new Realm(Realm.Types.HEAVEN, "qi_refining", QI_REFINING_1, QI_REFINING_2, QI_REFINING_3, QI_REFINING_4));
        EARTH_QI_REFINING = REALMS.register("earth_qi_refining", location -> new Realm(Realm.Types.EARTH, "qi_refining", QI_REFINING_1, QI_REFINING_2, QI_REFINING_3, QI_REFINING_4));
    }

    public static void register(IEventBus modEventBus) {
        REALMS.register(modEventBus);
        SUB_REALMS.register(modEventBus);
    }

    static final HashMap<String, Realm> REALM_MAP = new HashMap<>();
    static final HashMap<String, SubRealm> SUB_REALM_MAP = new HashMap<>();

    static void initialize() {
        REALMS.getEntries().forEach(entry -> {
            REALM_MAP.put(entry.getId().getPath(), entry.value());
        });

        SUB_REALMS.getEntries().forEach(entry -> {
            SUB_REALM_MAP.put(entry.getId().getPath(), entry.value());
        });

    }

    public static Realm getRealmFromKey(String key) throws InvalidKeyException {
        var k = REALM_MAP.get(key);
        if (k == null) {
            throw (new InvalidKeyException("ERROR !\nKEY IS NOT IN REGISTRY!"));
        } else {
            return k;
        }
    }

    public static SubRealm getSubRealmFromKey(String key) throws InvalidKeyException {
        var k = SUB_REALM_MAP.get(key);
        if (k == null) {
            throw (new InvalidKeyException("ERROR !\nKEY IS NOT IN REGISTRY!"));
        } else {
            return k;
        }
    }
}