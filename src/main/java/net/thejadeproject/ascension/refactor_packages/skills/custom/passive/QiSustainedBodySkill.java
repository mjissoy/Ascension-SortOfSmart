package net.thejadeproject.ascension.refactor_packages.skills.custom.passive;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.skills.ITickingSkill;

public class QiSustainedBodySkill extends SimplePassiveSkill implements ITickingSkill {

    @Override
    protected String getName() {
        return "Qi-Sustained Body";
    }

    @Override
    protected String getTooltip() {
        return "Your body is sustained by Qi, removing the need for ordinary food.";
    }

    @Override
    public void onPlayerTick(ServerPlayer player, IEntityData entityData) {
        if (player.tickCount % 20 != 0) return;

        FoodData foodData = player.getFoodData();

        if (foodData.getFoodLevel() < 20) {
            foodData.setFoodLevel(20);
        }

        if (foodData.getSaturationLevel() < 5.0F) {
            foodData.setSaturation(5.0F);
        }
    }
}