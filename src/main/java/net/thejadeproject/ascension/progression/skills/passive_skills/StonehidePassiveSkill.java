package net.thejadeproject.ascension.progression.skills.passive_skills;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.events.custom.PhysiqueChangeEvent;
import net.thejadeproject.ascension.events.custom.TechniqueChangeEvent;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.progression.skills.AbstractPassiveSkill;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

public class StonehidePassiveSkill extends AbstractPassiveSkill {
    public StonehidePassiveSkill(){
        super(Component.translatable("ascension.physique.passive.stonehide"));
        this.path = "ascension:body";
    }

    @Override
    public void onPhysiqueChange(PhysiqueChangeEvent event) {
        if(event.player.getData(ModAttachments.PLAYER_SKILL_DATA).hasSkill("ascension:stonehide_passive", "Passive"))
            updateSkillData(event.player);
    }

    @Override
    public void onTechniqueChange(TechniqueChangeEvent event) {
        if(event.player.getData(ModAttachments.PLAYER_SKILL_DATA).hasSkill("ascension:stonehide_passive", "Passive"))
            updateSkillData(event.player);
    }

    @Override
    public void onRealmChange(RealmChangeEvent event) {
        if(event.player.getData(ModAttachments.PLAYER_SKILL_DATA).hasSkill("ascension:stonehide_passive", "Passive"))
            updateSkillData(event.player);
    }

    public void updateSkillData(Player player){
        int majorRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(this.path).majorRealm;
        int minorRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(this.path).minorRealm;

        //Calc damage reduc: Base 5% + 5% per major realm
        double damageReduction = 0.05 + (majorRealm * 0.05);

        // Change to custom Attribute later?
        player.getAttribute(Attributes.ARMOR).addOrReplacePermanentModifier(new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "stonehide_armor"),
                damageReduction * 4,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        ));

        player.getAttribute(Attributes.ARMOR_TOUGHNESS).addOrReplacePermanentModifier(new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "stonehide_thoughness"),
                damageReduction * 2,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        ));
    }

    @Override
    public void onSkillAdded(Player player) {
        super.onSkillAdded(player);
        updateSkillData(player);

        player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addOrReplacePermanentModifier(new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "stonehide_knockback"),
                1.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        ));
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        player.getAttribute(Attributes.ARMOR).removeModifier(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "stonehide_armor")
        );
        player.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "stonehide_toughness")
        );
        player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).removeModifier(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "stonehide_knockback")
        );
    }

    @Override
    public ISkillData decode(RegistryFriendlyByteBuf buf){
        return null;
    }
}
