package net.thejadeproject.ascension.runic_path.skills.passive;

import net.lucent.easygui.gui.textures.TextureData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;

public class RunicHealthRegenSkill extends AbstractRunicPassiveSkill {

    public RunicHealthRegenSkill(Component title) {
        super(title);
        this.icon = new TextureData(
                ResourceLocation.fromNamespaceAndPath(
                        AscensionCraft.MOD_ID,
                        "textures/spells/icon/runic_health_regen.png"
                ),
                16,
                16
        );
    }

    @Override
    public RunicHealthRegenSkill setShortDescription(Component shortDescription) {
        super.setShortDescription(shortDescription);
        return this;
    }

    @Override
    public RunicHealthRegenSkill setDescription(Component description) {
        super.setDescription(description);
        return this;
    }

    @Override
    protected void apply(IEntityData entityData) {
    }

    @Override
    protected void remove(IEntityData entityData) {
    }
}

