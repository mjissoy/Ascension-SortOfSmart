package net.thejadeproject.ascension.progression.techniques.stability_handlers;

public class LnStabilityHandler extends GenericCalculatedStabilityHandler{

    public LnStabilityHandler(){
        super(input->Math.log(6*input+1),0,10,100);

    }
}
