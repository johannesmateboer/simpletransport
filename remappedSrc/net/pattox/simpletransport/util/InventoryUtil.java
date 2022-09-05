package net.pattox.simpletransport.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class InventoryUtil {

    /**
     * Tries to merge or insert to an Inventory
     * @param sourceStack       The stack that must be inserted or merged
     * @param targetInventory   The target-inventory
     * @param direction         The direction which the target should accept from
     * @return                  True when insert/merge is successful, false, if not.
     */
    public static Boolean insertOrMerge(ItemStack sourceStack, Inventory targetInventory, Direction direction) {
        for (int i = 0; i < targetInventory.size(); i++) {
            ItemStack targetStack = targetInventory.getStack(i);
            if (targetInventory instanceof SidedInventory && !((SidedInventory) targetInventory).canInsert(i, targetStack, direction)) {
                continue;
            }
            if (targetStack.getItem() == sourceStack.getItem()) {
                // Can we merge the items?
                int available = targetStack.getMaxCount() - targetStack.getCount();
                if (available >= sourceStack.getCount()) {
                    targetStack.setCount(targetStack.getCount() + sourceStack.getCount());
                    targetInventory.markDirty();
                    return true;
                }
            }else if (targetStack.isEmpty()) {
                targetInventory.setStack(i, sourceStack);
                targetInventory.markDirty();
                return true;
            }
        }
        return false;
    }

    /**
     * Tries to insert to an adjacent inventory, otherwise doesn't do anything.
     * @param world     The World we all live in
     * @param pos       The inventory-position
     * @param stack     The stack in question
     * @param direction The insert-direction of the target
     * @return          True when success, false when not changed
     */
    public static boolean insertOrLeave(World world, BlockPos pos, ItemStack stack, Direction direction) {
        if (world.getBlockEntity(pos) instanceof Inventory targetInventory) {
            return InventoryUtil.insertOrMerge(stack, targetInventory, direction);
        }
        return false;
    }
}

