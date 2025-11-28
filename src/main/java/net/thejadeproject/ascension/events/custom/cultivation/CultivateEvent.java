package net.thejadeproject.ascension.events.custom.cultivation;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

import java.util.List;
import java.util.Set;

public class CultivateEvent extends Event {
    public final Player player;
    public final Double baseRate;
    public double multiplier;
    public double flatBaseRateIncrease;
    public double flatFinalRateIncrease;
    public final String path;
    public final List<String> ascensionAttributes;
    public CultivateEvent(Player player, double baseRate, String path, Set<String> ascensionAttributes){
        this.player = player;
        this.baseRate = baseRate;
        this.path = path;
        this.ascensionAttributes = List.copyOf(ascensionAttributes);
    }

    public void addMultiplier(double multiplier){
        this.multiplier = Math.max(this.multiplier+multiplier,0);
    }
    public void addFlatBaseRateIncrease(double flatBaseRate){
        this.flatBaseRateIncrease += flatBaseRate;
    }
    public void addFlatFinalRateIncrease(double flatFinalRateIncrease){
        this.flatFinalRateIncrease += flatFinalRateIncrease;
    }

}
