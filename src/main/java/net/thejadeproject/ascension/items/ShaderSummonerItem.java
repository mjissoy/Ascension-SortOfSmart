package net.thejadeproject.ascension.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.thejadeproject.ascension.entity.ModEntities;
import net.thejadeproject.ascension.entity.client.shaders.RiftEntity;

public class ShaderSummonerItem extends Item {
    public ShaderSummonerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            // Calculate position 2 blocks in front of the player
            Vec3 lookDirection = player.getLookAngle(); // Get the direction player is looking
            double distance = 2.0; // Distance in blocks

            // Calculate spawn position: player position + look direction * distance
            double spawnX = player.getX() + lookDirection.x * distance;
            double spawnY = player.getY() + player.getEyeHeight() + lookDirection.y * distance; // Adjust for eye height
            double spawnZ = player.getZ() + lookDirection.z * distance;

            // Ensure the rift spawns at ground level if looking downward
            if (lookDirection.y < 0) {
                // If looking down, adjust so it doesn't spawn underground
                spawnY = Math.max(level.getMinBuildHeight() + 1, spawnY);
            }

            RiftEntity rift = new RiftEntity(ModEntities.RIFT.get(), level);
            rift.setPos(spawnX, spawnY, spawnZ);
            level.addFreshEntity(rift);
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
