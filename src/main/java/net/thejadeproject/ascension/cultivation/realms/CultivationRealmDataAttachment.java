package net.thejadeproject.ascension.cultivation.realms;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.util.HashMap;

import static net.thejadeproject.ascension.cultivation.realms.RealmRegistry.getRealmFromKey;
import static net.thejadeproject.ascension.cultivation.realms.RealmRegistry.getSubRealmFromKey;

public class CultivationRealmDataAttachment implements INBTSerializable<CompoundTag> {
    HashMap<Realm, SubRealm> REALMS;
    HashMap<Realm, BigInteger> REALM_QI;

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        var t = new CompoundTag();
        REALMS.forEach((r,s) -> {
            t.putString(r.key, s.key());
        });

        REALM_QI.forEach((r,i) -> {
            t.putString(r.key, i.toString());
        });

        return t;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag tag) {
        REALMS = new HashMap<>();
        REALM_QI = new HashMap<>();

        // Deserialize REALMS (Realm -> SubRealm mappings)
        for (String key : tag.getAllKeys()) {
            try {
                Realm realm = getRealmFromKey(key);
                String subRealmStr = tag.getString(key);
                SubRealm subRealm = getSubRealmFromKey(subRealmStr);
                REALMS.put(realm, subRealm);
            } catch (InvalidKeyException e) {
                // Skip invalid entries or log a warning
                System.err.println("Skipping invalid realm/subrealm pair: " + key + " -> " + tag.getString(key));
            }
        }

        // Deserialize REALM_QI (Realm -> BigInteger mappings)
        for (String key : tag.getAllKeys()) {
            try {
                Realm realm = getRealmFromKey(key);
                String qiStr = tag.getString(key);
                BigInteger qi = new BigInteger(qiStr);
                REALM_QI.put(realm, qi);
            } catch (InvalidKeyException e) {
                // Skip invalid entries or log a warning
                System.err.println("Skipping invalid realm/Qi pair: " + key + " -> " + tag.getString(key));
            } catch (NumberFormatException e) {
                System.err.println("Skipping invalid Qi value for realm " + key + ": " + tag.getString(key));
            }
        }
    }
}
