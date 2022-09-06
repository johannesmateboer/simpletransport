package net.pattox.simpletransport.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.pattox.simpletransport.gui.GenericFilterScreenHandler;
import net.pattox.simpletransport.init.Conveyorbelts;
import net.pattox.simpletransport.util.BasicInventory;
import net.pattox.simpletransport.util.ItemSpawner;

import java.util.List;

public class InserterUpperEntity extends BlockEntity implements NamedScreenHandlerFactory, BasicInventory {
    private int interval = 0;

    // Inventory for filterstorage
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(9, ItemStack.EMPTY);

    public InserterUpperEntity(BlockPos pos, BlockState state) {
        super(Conveyorbelts.INSERTER_UPPER_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, InserterUpperEntity blockEntity) {
        if (world.isClient()) {
            return;
        }

        if (blockEntity.interval > 20) {
            BlockPos monitorPos = pos.offset(state.get(Properties.HORIZONTAL_FACING));
            BlockPos targetPos = pos.up();
            List<Entity> entities = world.getOtherEntities(null, new Box(monitorPos));

            for (Entity entity : entities) {
                if (entity instanceof ItemEntity) {
                    ItemStack stack = ((ItemEntity) entity).getStack();
                    if (blockEntity.isAllowedByFilter(stack)) {
                        ItemSpawner.insertOrDrop(world, targetPos, stack);
                        entity.remove(Entity.RemovalReason.DISCARDED);
                    }
                }
            }
            blockEntity.interval = 0;
        } else {
            blockEntity.interval = blockEntity.interval + 1;
        }
    }

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