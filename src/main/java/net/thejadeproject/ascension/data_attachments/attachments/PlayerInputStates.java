package net.thejadeproject.ascension.data_attachments.attachments;

import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

//stores player input states, essential isHeld
//does not store key but an input id
public class PlayerInputStates {

    //integer here is modifiers
    private final HashMap<String,Integer> inputs = new HashMap<>();
    public final Player player;

    public PlayerInputStates(Player player){
        this.player = player;
    }

    public boolean isHeld(String input){
        return inputs.containsKey(input);
    }
    public int getModifier(String input){
        return inputs.get(input);
    }

    public void inputDown(String input,int modifier){
        inputs.put(input,modifier);
    }
    public void inputReleased(String input){
        inputs.remove(input);
    }
}
