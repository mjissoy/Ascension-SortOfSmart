package net.thejadeproject.ascension.refactor_packages.skills.custom.passive.flight;

import net.minecraft.server.level.ServerPlayer;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.ITickingSkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.FlightHelper;
import net.thejadeproject.ascension.refactor_packages.skills.custom.passive.SimplePassiveSkill;

public class TrueFlightSkill extends SimplePassiveSkill implements ITickingSkill {

    @Override
    protected String getName() {
        return "True Flight";
    }

    @Override
    protected String getTooltip() {
        return "Grants the ability to fly freely.";
    }

    @Override
    protected String getIconPath() {
        return "textures/spells/icon/placeholder.png";
    }

    @Override
    public void onPlayerTick(ServerPlayer player, IEntityData entityData) {
        FlightHelper.enableFlight(player, false);
    }

    @Override
    public void onRemoved(IEntityData attachedEntityData, IPersistentSkillData persistentData) {
        super.onRemoved(attachedEntityData, persistentData);

        if (attachedEntityData.getAttachedEntity() instanceof ServerPlayer player) {
            FlightHelper.disableFlightIfNotVanillaAllowed(player);
        }
    }
}