package net.thejadeproject.ascension.progression.skills.passive_skills;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.thejadeproject.ascension.entity.custom.AscensionSkillEntity;
import net.thejadeproject.ascension.progression.skills.AbstractPassiveSkill;
import net.thejadeproject.ascension.progression.skills.data.IPersistentSkillData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.util.ModTags;
//deals a bonus hit of sword_intent damage
public class SwordIntent extends AbstractPassiveSkill {
    public SwordIntent() {
        super(Component.literal("Sword Intent"));

        this.path = "ascension:intent";
        NeoForge.EVENT_BUS.addListener(this::onLivingDamageEvent);
    }
    public void onLivingDamageEvent(LivingDamageEvent.Pre event){
        if(event.getSource().getEntity() != null) {
            if (!(event.getSource().getEntity() instanceof Player player)) return;


            if (!player.getData(ModAttachments.PLAYER_SKILL_DATA).hasPassiveSkill("ascension:sword_intent_skill"))return;

            //weapon was used
            if(event.getSource().isDirect() &&
                    event.getSource().getWeaponItem() != ItemStack.EMPTY &&
                    !event.getSource().getWeaponItem().is(ModTags.Items.daoItemTags.get("ascension:sword_intent"))
            ) return;
            else {
                if(event.getSource().getDirectEntity() instanceof AscensionSkillEntity ascensionSkillEntity){
                    if(!ascensionSkillEntity.getDaoTags().contains("ascension:sword_intent")) return;
                }
            }

            //player has the skill so apply the bonus damage

            //get Cultivation stage;
            int majorRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(getSkillPath()).majorRealm;
            int minorRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(getSkillPath()).minorRealm;
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
