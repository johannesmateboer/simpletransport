package net.pattox.simpletransport.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pattox.simpletransport.SimpleTransport;
import net.pattox.simpletransport.util.ItemSpawner;

import java.util.Objects;

public class ExtractorEntity extends BlockEntity implements BlockEntityClientSerializable {

    private int interval = 0;
    private String filterItem = "";
    private Integer filterAmount = 1;
    private Boolean editMode = false;

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

                    if (!targetStack.isEmpty() && blockEntity.isAllowedByFilter(targetStack)) {
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

    public Integer getFilterAmount() {
        return filterAmount;
    }

    public boolean hasFilter() {
        return !Objects.equals(this.filterItem, "");
    }

    public boolean isAllowedByFilter(ItemStack stack) {
        if (!hasFilter()) {
            return true;
        }

        if (Objects.equals(stack.getItem().getTranslationKey(), getFilterItem())) {
            return true;
        }
        return false;
    }

    public void setFilterAmount(Integer filterAmount) {
        this.filterAmount = filterAmount;
        markDirty();
    }

    public void disableEditmode() {
        this.editMode = false;
        markDirty();
    }

    public void enableEditmode() {
        this.editMode = true;
        markDirty();
    }

    public Boolean getEditmode() {
        return Objects.requireNonNullElse(this.editMode, false);
    }


}
