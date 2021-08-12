package net.pattox.simpletransport.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
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
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pattox.simpletransport.SimpleTransport;
import net.pattox.simpletransport.gui.GenericFilterScreenHandler;
import net.pattox.simpletransport.util.BasicInventory;
import net.pattox.simpletransport.util.ItemSpawner;

public class ExtractorUpperEntity extends BlockEntity implements BlockEntityClientSerializable, NamedScreenHandlerFactory, BasicInventory {

    private int interval = 0;

    public ExtractorUpperEntity(BlockPos pos, BlockState state) {
        super(SimpleTransport.EXTRACTOR_UPPER_ENTITY, pos, state);
    }

    // Inventory for filterstorage
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(9, ItemStack.EMPTY);

    public static void tick(World world, BlockPos pos, BlockState state, ExtractorUpperEntity blockEntity) {
        if (world.isClient()) {
            return;
        }
        if (blockEntity.interval > 30) {
            // Is there something like and inventory on the other side?
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
                        ItemSpawner.spawnOnBelt(world, pos.offset(state.get(Properties.HORIZONTAL_FACING)), droppableStack);
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
    public NbtCompound writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);
        return super.writeNbt(nbt);
    }

    @Override
    public void fromClientTag(NbtCompound compoundTag) {
        readNbt(compoundTag);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound compoundTag) {
        return writeNbt(compoundTag);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("Filter");
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
