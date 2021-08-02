package net.pattox.simpletransport.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pattox.simpletransport.SimpleTransport;
import net.pattox.simpletransport.util.ItemSpawner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExtractorEntity extends BlockEntity {

    private int interval = 0;
    private static final Logger LOGGER = LogManager.getLogger();

    public ExtractorEntity(BlockPos pos, BlockState state) {
        super(SimpleTransport.EXTRACTOR_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, ExtractorEntity blockEntity) {
        if (world.isClient()) {
            return;
        }
        if (blockEntity.interval > 50) {
            Direction direction = blockEntity.getCachedState().get(Properties.HORIZONTAL_FACING);



            // Is there something like and inventory on the other side?
            if (world.getBlockEntity(pos.offset(direction.getOpposite())) instanceof Inventory) {
                Inventory targetInventory = (Inventory) world.getBlockEntity(pos.offset(direction.getOpposite()));
                // Iterate over the slots in the target-inventory and drop things when its not empty.
                for (int i = 0; i < targetInventory.size(); i++) {
                    ItemStack targetStack = targetInventory.getStack(i);
                    if (targetInventory instanceof SidedInventory && !((SidedInventory) targetInventory).canExtract(i, targetStack, direction)) {
                        continue;
                    }
                    if (!targetStack.isEmpty()) {
                        ItemStack droppableStack = targetInventory.getStack(i);
                        ItemSpawner.spawnOnBelt(world, pos, droppableStack);
                        targetInventory.removeStack(i);
                        targetInventory.markDirty();
                        break;
                    }
                }
            }
            blockEntity.interval = 0;
        } else {
            blockEntity.interval = blockEntity.interval + 1;
        }
    }
}
