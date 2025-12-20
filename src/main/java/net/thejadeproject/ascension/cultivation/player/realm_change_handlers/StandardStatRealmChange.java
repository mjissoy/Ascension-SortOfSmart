package net.thejadeproject.ascension.cultivation.player.realm_change_handlers;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.player.EntityAttributeManager;

import java.util.Map;

public class StandardStatRealmChange implements IRealmChangeHandler{

    public final Map<Holder<Attribute>,Double> minorRealmAttributeIncrease;
    public final Map<Holder<Attribute>,Double> majorRealmAttributeIncrease;

    public StandardStatRealmChange(Map<Holder<Attribute>, Double> minorRealmAttributeIncrease, Map<Holder<Attribute>, Double> majorRealmAttributeIncrease) {
        this.minorRealmAttributeIncrease = minorRealmAttributeIncrease;
        this.majorRealmAttributeIncrease = majorRealmAttributeIncrease;
    }


    @Override
    public void onMinorRealmIncrease(Player player, String path, int numberOfMinorRealms) {
        for(Holder<Attribute> attributeHolder : minorRealmAttributeIncrease.keySet()){
            
            EntityAttributeManager.increaseAttribute(
                    player,
                    minorRealmAttributeIncrease.get(attributeHolder),
                    attributeHolder
            );
        }
    }

    @Override
    public void onMajorRealmIncrease(Player player, String path, int numberOfMajorRealms) {
        for(Holder<Attribute> attributeHolder : majorRealmAttributeIncrease.keySet()){
            EntityAttributeManager.increaseAttribute(
                    player,
                    majorRealmAttributeIncrease.get(attributeHolder),
                    attributeHolder
            );
        }
    }

    @Override
    public void onMinorRealmDecrease(Player player, String path, int numberOfMinorRealms) {
        for(Holder<Attribute> attributeHolder : minorRealmAttributeIncrease.keySet()){
            EntityAttributeManager.decreaseAttribute(
                    player,
                    minorRealmAttributeIncrease.get(attributeHolder),
                    attributeHolder
            );
        }
    }

    @Override
    public void onMajorRealmDecrease(Player player, String path, int numberOfMajorRealms) {
        for(Holder<Attribute> attributeHolder : majorRealmAttributeIncrease.keySet()){
            EntityAttributeManager.decreaseAttribute(
                    player,
                    majorRealmAttributeIncrease.get(attributeHolder),
                    attributeHolder
            );
        }
    }
}
