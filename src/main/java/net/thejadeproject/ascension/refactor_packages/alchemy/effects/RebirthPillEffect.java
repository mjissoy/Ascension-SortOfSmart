package net.thejadeproject.ascension.refactor_packages.alchemy.effects;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.thejadeproject.ascension.refactor_packages.alchemy.BasicPillEffect;

public class RebirthPillEffect extends BasicPillEffect {

    public RebirthPillEffect(Component name, Component description) {
        super(name, description);
    }

    @Override
    public boolean tryConsume(LivingEntity entity, ItemStack itemStack, double purityScale, double realmMultiplier) {
        //TODO
        return true;
    }
}
