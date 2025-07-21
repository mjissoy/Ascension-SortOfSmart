package net.thejadeproject.ascension.progression.skills.passive_skills;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.thejadeproject.ascension.progression.skills.AbstractPassiveSkill;
import net.thejadeproject.ascension.util.ModAttachments;
import net.thejadeproject.ascension.util.ModTags;

public class FistAura extends AbstractPassiveSkill {
    public FistAura(){
        super("Fist Aura");

        this.path = "ascension:intent";
        NeoForge.EVENT_BUS.addListener(this::onLivingDamageEvent);
    }


    public void onLivingDamageEvent(LivingDamageEvent.Pre event){
        if(event.getSource().getEntity() != null) {
            if (!(event.getSource().getEntity() instanceof Player player)) return;
            if(event.getSource().getWeaponItem() != ItemStack.EMPTY && !event.getSource().getWeaponItem().is(ModTags.Items.FIST_INTENT)) return;
            if (!player.getData(ModAttachments.PLAYER_DATA).hasPassiveSkill("ascension:fist_aura_skill"))return;


            //player has the skill so apply the bonus damage

            //get Cultivation stage;
            int majorRealm = player.getData(ModAttachments.PLAYER_DATA).getPathData(getSkillPath()).majorRealm;
            int minorRealm = player.getData(ModAttachments.PLAYER_DATA).getPathData(getSkillPath()).minorRealm;
            //possibly some sort of aura stat?

            double damage = 2*(1+majorRealm*10)*(1+minorRealm);

            event.setNewDamage((float) (event.getNewDamage() + damage));


        }
    }


    //TODO have it update on realm change and technique change and physique change
    @Override
    public void onSkillAdded(Player player) {
        super.onSkillAdded(player);

    }


}
