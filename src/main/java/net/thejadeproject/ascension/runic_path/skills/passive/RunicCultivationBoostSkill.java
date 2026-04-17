package net.thejadeproject.ascension.runic_path.skills.passive;

import net.lucent.easygui.gui.textures.TextureData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;

public class RunicCultivationBoostSkill extends AbstractRunicPassiveSkill {

    public RunicCultivationBoostSkill(Component title) {
        super(title);
        this.icon = new TextureData(
                ResourceLocation.fromNamespaceAndPath(
                        AscensionCraft.MOD_ID,
                        "textures/spells/icon/runic_cultivation_boost.png"
                ),
                16,
                16
        );
    }

    @Override
    public RunicCultivationBoostSkill setShortDescription(Component shortDescription) {
        super.setShortDescription(shortDescription);
        return this;
    }

    @Override
    public RunicCultivationBoostSkill setDescription(Component description) {
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