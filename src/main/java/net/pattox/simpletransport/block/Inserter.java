package net.pattox.simpletransport.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Inserter extends Block {

    public Inserter(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        // When the player is sneaking, the conveyor will be placed rotated.
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayer().isSneaking() ? ctx.getPlayerFacing().getOpposite() : ctx.getPlayerFacing());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        // Set the available properties
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext entityContext) {
        // Set the bounding-box
        return VoxelShapes.cuboid(0, 0, 0, 1, (1f/16f), 1);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!(entity instanceof ItemEntity)) {
            return;
        }

        ItemEntity droppedItems = (ItemEntity) entity;
        Direction direction = state.get(HorizontalFacingBlock.FACING);

        if (world.getBlockEntity(pos.offset(direction.getOpposite())) instanceof Inventory) {
            Inventory targetInventory = (Inventory) world.getBlockEntity(pos.offset(direction.getOpposite()));
            for (int i = 0; i < targetInventory.size(); i++) {
                if (targetInventory.isValid(i, droppedItems.getStack())) {
                    targetInventory.setStack(i, droppedItems.getStack());
                    droppedItems.remove(Entity.RemovalReason.DISCARDED);
                    break;
                }
            }
        }
    }


}
