package net.pattox.simpletransport.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pattox.simpletransport.gui.GenericFilterScreenHandler;
import net.pattox.simpletransport.init.Conveyorbelts;
import net.pattox.simpletransport.util.BasicInventory;
import net.pattox.simpletransport.util.ItemSpawner;

public class ExtractorUpperEntity extends BlockEntity implements NamedScreenHandlerFactory, BasicInventory {

    private int interval = 0;

    public ExtractorUpperEntity(BlockPos pos, BlockState state) {
        super(Conveyorbelts.EXTRACTOR_UPPER_ENTITY, pos, state);
    }

    // Inventory for filterstorage
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(9, ItemStack.EMPTY);

    public static void tick(World world, BlockPos pos, BlockState state, ExtractorUpperEntity blockEntity) {
        if (world.isClient()) {
            return;
        }
        if (blockEntity.interval > 30) {
            // Is there something like an inventory on the other side?
            if (world.getBlockEntity(pos.offset(Direction.UP)) instanceof Inventory) {
                Inventory targetInventory = (Inventory) world.getBlockEntity(pos.offset(Direction.UP));
                boolean hasNoFilter = blockEntity.isEmpty();

                // Iterate over the slots in the target-inventory and drop things when its not empty.
                for (int i = 0; i < targetInventory.size(); i++) {
                    ItemStack targetStack = targetInventory.getStack(i);
                    if (targetInventory instanceof SidedInventory && !((SidedInventory) targetInventory).canExtract(i, targetStack, Direction.DOWN)) {
                        continue;
                    }

                    if (!targetStack.isEmpty() && (hasNoFilter || blockEntity.isAllowedByFilter(targetStack))) {
                        ItemStack droppableStack = targetInventory.getStack(i);
                        ItemSpawner.spawnOnBelt(world, pos.offset(state.get(Properties.HORIZONTAL_FACING)), droppableStack, world.isReceivingRedstonePower(pos));
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

    /**
     * When no filter is present, everything is allowed.
     * @param stack The stack to check
     * @return  True when allowed, false when not allowed
     */
    public boolean isAllowedByFilter(ItemStack stack) {
        if (this.isEmpty()) {
            return true;
        }else {
            return this.containsStack(stack);
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, items);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("Filter");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        //We provide *this* to the screenHandler as our class Implements Inventory
        //Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        return new GenericFilterScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }


}
