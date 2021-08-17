package net.pattox.simpletransport.util;

import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ItemSpawner {

    /**
     * @param world    The World itself that holds all magic
     * @param pos      The position where to spawn the stack
     * @param stack    The stack in question.
     * @param noPickup When true: the spawned entity cannot be picked up by the player
     */
    public static void spawnOnBelt(World world, BlockPos pos, ItemStack stack, boolean noPickup) {

        while (!stack.isEmpty()) {
            double y = pos.getY() + 0.4f;
            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5f, y, pos.getZ() + 0.5f, stack.split(8), 0, 0, 0);
            if (noPickup) {
                itemEntity.setPickupDelayInfinite();
                itemEntity.setNeverDespawn();
            }
            world.spawnEntity(itemEntity);
        }
    }

    /**
     * Tries the block below for Inventory. If so: see if we can drop it. If not, make it explode.
     *
     * @param world World
     * @param pos   Target-position
     * @param stack The stack
     */
    public static void insertOrDrop(World world, BlockPos pos, ItemStack stack) {
        if (world.getBlockEntity(pos) instanceof Inventory targetInventory) {
            if (InventoryUtil.insertOrMerge(stack, targetInventory, Direction.UP)) {
                return;
            }
        }
        spawnOnBelt(world, pos, stack, true);
    }
}
