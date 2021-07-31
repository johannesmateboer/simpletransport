package net.pattox.simpletransport.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pattox.simpletransport.SimpleTransport;
import net.pattox.simpletransport.util.ItemSpawner;

public class ExtractorEntity extends BlockEntity {

    private static long interval;

    public ExtractorEntity(BlockPos pos, BlockState state) {
        super(SimpleTransport.EXTRACTOR_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (world.isClient()) {
            return;
        }

        if (interval == 50) {
            // We Have a second
            Direction direction = state.get(Properties.HORIZONTAL_FACING);

            // Is there something like and inventory on the other side?
            if (world.getBlockEntity(pos.offset(direction.getOpposite())) instanceof Inventory) {
                Inventory targetInventory = (Inventory) world.getBlockEntity(pos.offset(direction.getOpposite()));
                if (!targetInventory.isEmpty()) {

                    // Get the targetspot for dropping.
                    BlockPos targetDropSpot = pos.offset(direction);

                    // Iterate over the slots in the target-inventory and drop things when its not empty.
                    for (int i = 0; i < targetInventory.size(); i++) {
                        if (!targetInventory.getStack(i).isEmpty()){
                            ItemStack droppableStack = targetInventory.getStack(i);
                            ItemSpawner.spawnOnBelt(world, targetDropSpot, droppableStack);
                            targetInventory.removeStack(i);
                            targetInventory.markDirty();
                            break;
                        }
                    }
                }
            }
            interval = 0;
        } else {
            interval = interval + 1;
        }
    }
}
