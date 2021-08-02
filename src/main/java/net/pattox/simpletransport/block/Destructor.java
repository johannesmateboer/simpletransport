package net.pattox.simpletransport.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.pattox.simpletransport.util.VoxelUtil;

public class Destructor extends Block {

    public Destructor(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        // When the player is sneaking, the conveyor will be placed rotated.
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayer().isSneaking() ? ctx.getPlayerFacing() : ctx.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        // Set the available properties
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext entityContext) {
        // Set the bounding-box
        VoxelShape belt = VoxelUtil.createCuboid(0, 0, 0, 16, 1, 16);
        VoxelShape connector = VoxelUtil.createCuboid(2, 1, 8, 14, 10, 16);
        VoxelShape inserter = VoxelShapes.combine(belt, connector, BooleanBiFunction.OR);
        return VoxelUtil.rotateShape(Direction.NORTH, blockState.get(Properties.HORIZONTAL_FACING), inserter);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!(entity instanceof ItemEntity)) {
            return;
        }
        ItemEntity droppedItems = (ItemEntity) entity;
        droppedItems.remove(Entity.RemovalReason.DISCARDED);
    }
}
