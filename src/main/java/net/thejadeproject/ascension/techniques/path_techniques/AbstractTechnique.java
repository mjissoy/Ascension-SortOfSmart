package net.thejadeproject.ascension.techniques.path_techniques;

import net.thejadeproject.ascension.events.custom.MajorRealmChangeEvent;
import net.thejadeproject.ascension.events.custom.MinorRealmChangeEvent;
import net.thejadeproject.ascension.techniques.ITechnique;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractTechnique implements ITechnique {
    public Consumer<MinorRealmChangeEvent> minorRealmChangeEventConsumer;
    public Consumer<MajorRealmChangeEvent> majorRealmChangeEventConsumer;

    @Override
    public void onMinorRealmIncrease(MinorRealmChangeEvent event) {
        ITechnique.super.onMinorRealmIncrease(event);
        minorRealmChangeEventConsumer.accept(event);
    }

    @Override
    public void onMajorRealmIncrease(MajorRealmChangeEvent event) {
        ITechnique.super.onMajorRealmIncrease(event);
        majorRealmChangeEventConsumer.accept(event);
    }

    public AbstractTechnique setOnMinorRealmChange(Consumer<MinorRealmChangeEvent> consumer){
        minorRealmChangeEventConsumer = consumer;
        return this;
    }
    public AbstractTechnique setOnMajorRealmChange(Consumer<MajorRealmChangeEvent> consumer){
        majorRealmChangeEventConsumer = consumer;
        return this;
    }
}
