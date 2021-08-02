package net.pattox.simpletransport.util;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSpawner {

    public static void spawnOnBelt(World world, BlockPos pos, ItemStack stack) {
        while (!stack.isEmpty()) {
            double y = pos.getY() + 0.4f;
            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5f, y, pos.getZ() + 0.5f, stack.split(8), 0, 0, 0);
            world.spawnEntity(itemEntity);
        }
    }
}
