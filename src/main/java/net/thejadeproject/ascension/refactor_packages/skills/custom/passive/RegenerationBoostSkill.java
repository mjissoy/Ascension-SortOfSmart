package net.thejadeproject.ascension.refactor_packages.skills.custom.passive;

import net.minecraft.server.level.ServerPlayer;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.skills.ITickingSkill;

public class RegenerationBoostSkill extends SimplePassiveSkill implements ITickingSkill {

    @Override
    protected String getName() {
        return "Regenerative Body";
    }

    @Override
    protected String getTooltip() {
        return "Your refined body slowly restores lost health over time.";
    }

    @Override
    public void onPlayerTick(ServerPlayer player, IEntityData entityData) {
        if (player.tickCount % 80 != 0) return;
        if (player.getHealth() >= player.getMaxHealth()) return;

        //TODO: add scaling based on major realm

        player.heal(1.0F);
    }
}