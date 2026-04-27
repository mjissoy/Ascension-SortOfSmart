package net.thejadeproject.ascension.refactor_packages.skills;

import net.minecraft.server.level.ServerPlayer;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;

public interface ITickingSkill {

    void onPlayerTick(ServerPlayer player, IEntityData entityData);

}
