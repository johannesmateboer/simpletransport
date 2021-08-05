package net.pattox.simpletransport.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pattox.simpletransport.SimpleTransport;
import net.pattox.simpletransport.util.ItemSpawner;
import java.util.List;
import java.util.Objects;

public class PullerEntity extends BlockEntity implements BlockEntityClientSerializable {
    private int interval = 0;
    private String filterItem = "";
    private Boolean editMode = false;

    public PullerEntity(BlockPos pos, BlockState state) {
        super(SimpleTransport.PULLER_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, PullerEntity blockEntity) {
        if (world.isClient()) {
            return;
        }
        if (blockEntity.interval > 10) {
            Direction targetDirection = state.get(Properties.HORIZONTAL_FACING).getOpposite();
            BlockPos targetPos = pos.offset(targetDirection);
            BlockPos monitorPos = pos.offset(state.get(Properties.HORIZONTAL_FACING));

            List<Entity> entities = world.getOtherEntities(null, new Box(monitorPos));
            for (Entity entity : entities) {
                if (entity instanceof ItemEntity) {
                    ItemStack stack = ((ItemEntity) entity).getStack();
                    if (blockEntity.isAllowedByFilter(stack)) {
                        ItemSpawner.spawnOnBelt(world, targetPos, stack);
                        entity.remove(Entity.RemovalReason.DISCARDED);
                    }
                }
            }
            blockEntity.interval = 0;
        } else {
            blockEntity.interval = blockEntity.interval + 1;
        }
    }

    public boolean hasFilter() {
        return !Objects.equals(filterItem, "");
    }

    public boolean isAllowedByFilter(ItemStack stack) {
        // Only works with filter
        if (!hasFilter()) {
            return false;
        }
        // Only works with the set item
        if (Objects.equals(stack.getItem().getTranslationKey(), getFilterItem())) {
            return true;
        }
        return false;
    }

    public void disableEditmode() {
        this.editMode = false;
        markDirty();
    }

    public void enableEditmode() {
        this.editMode = true;
        markDirty();
    }

    public String getFilterItem() {
        return filterItem;
    }

    public void setFilterItem(String filterItem) {
        this.filterItem = filterItem;
        markDirty();
    }

    public void clearFilterItem() {
        this.filterItem = "";
        markDirty();
    }

    public Boolean getEditmode() {
        return Objects.requireNonNullElse(this.editMode, false);
    }

    @Override
    public void readNbt(NbtCompound compoundTag) {
        super.readNbt(compoundTag);
        filterItem = compoundTag.getString("filterItem");
        editMode = compoundTag.getBoolean("editmode");
    }

    @Override
    public NbtCompound writeNbt(NbtCompound compoundTag) {
        super.writeNbt(compoundTag);
        compoundTag.putString("filterItem", filterItem);
        compoundTag.putBoolean("editmode", editMode);
        return compoundTag;
    }

    @Override
    public void fromClientTag(NbtCompound compoundTag) {
        readNbt(compoundTag);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound compoundTag) {
        return writeNbt(compoundTag);
    }
}
