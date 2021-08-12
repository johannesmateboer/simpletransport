package net.pattox.simpletransport.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
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

import java.util.Objects;

public class ExtractorEntity extends BlockEntity implements BlockEntityClientSerializable, NamedScreenHandlerFactory, BasicInventory {

    private int interval = 0;

    public ExtractorEntity(BlockPos pos, BlockState state) {
        super(SimpleTransport.EXTRACTOR_ENTITY, pos, state);
    }

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(9, ItemStack.EMPTY);

    public static void tick(World world, BlockPos pos, BlockState state, ExtractorEntity blockEntity) {
        if (world.isClient()) {
            return;
        }

        if (blockEntity.interval > 30) {
            Direction direction = blockEntity.getCachedState().get(Properties.HORIZONTAL_FACING);

            // Is there something like and inventory on the other side?
            if (world.getBlockEntity(pos.offset(direction.getOpposite())) instanceof Inventory) {
                Inventory targetInventory = (Inventory) world.getBlockEntity(pos.offset(direction.getOpposite()));
                boolean hasNoFilter = blockEntity.isEmpty();

                // Iterate over the slots in the target-inventory and drop things when its not empty.
                for (int i = 0; i < targetInventory.size(); i++) {
                    ItemStack targetStack = targetInventory.getStack(i);
                    if (targetInventory instanceof SidedInventory && !((SidedInventory) targetInventory).canExtract(i, targetStack, direction)) {
                        continue;
                    }

                    if (!targetStack.isEmpty() && (hasNoFilter || blockEntity.isAllowedByFilter(targetStack))) {
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

    public boolean isAllowedByFilter(ItemStack stack) {
        if (this.isEmpty()) {
            return true;
        }else {
            return this.containsStack(stack);
        }
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
