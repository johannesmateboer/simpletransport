package net.pattox.simpletransport.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
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
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pattox.simpletransport.SimpleTransport;
import net.pattox.simpletransport.gui.GenericFilterScreenHandler;
import net.pattox.simpletransport.init.Conveyorbelts;
import net.pattox.simpletransport.util.BasicInventory;
import net.pattox.simpletransport.util.InventoryUtil;

import java.util.List;

public class OpiEntity extends BlockEntity implements BlockEntityClientSerializable, NamedScreenHandlerFactory, BasicInventory {

    private int interval = 0;
    private final Direction targetDirection;
    private final BlockPos monitorPos;
    private final BlockPos targetPos;

    // Inventory for filterstorage
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(9, ItemStack.EMPTY);

    public OpiEntity(BlockPos pos, BlockState state) {
        super(Conveyorbelts.OPI_ENTITY, pos, state);
        this.targetDirection = state.get(Properties.HORIZONTAL_FACING).getOpposite();
        this.monitorPos = pos.offset(state.get(Properties.HORIZONTAL_FACING));
        this.targetPos = pos.offset(this.targetDirection);
    }

    public static void tick(World world, BlockPos pos, BlockState state, OpiEntity blockEntity) {
        if (world.isClient()) {
            return;
        }

        if (blockEntity.interval > 10) {
            List<Entity> entities = world.getOtherEntities(null, new Box(blockEntity.monitorPos));

            for (Entity entity : entities) {
                if (entity instanceof ItemEntity) {
                    ItemStack stack = ((ItemEntity) entity).getStack();
                    if (blockEntity.isAllowedByFilter(stack)) {
                        if (InventoryUtil.insertOrLeave(world, blockEntity.targetPos, stack, blockEntity.targetDirection)) {
                            entity.remove(Entity.RemovalReason.DISCARDED);
                        }
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
        } else {
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
