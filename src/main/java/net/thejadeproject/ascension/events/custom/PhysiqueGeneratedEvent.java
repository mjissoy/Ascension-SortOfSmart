package net.thejadeproject.ascension.events.custom;


import net.neoforged.bus.api.Event;

public class PhysiqueGeneratedEvent extends Event {

    public final String generatedPhysique;
    public final String[] otherPhysiques;

    public PhysiqueGeneratedEvent(String generatedPhysique,String[] otherPhysiques){
        this.generatedPhysique = generatedPhysique;
        this.otherPhysiques = otherPhysiques;
    }

}
