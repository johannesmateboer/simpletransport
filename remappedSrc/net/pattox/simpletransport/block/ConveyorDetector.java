package net.pattox.simpletransport.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.pattox.simpletransport.SimpleTransport;
import net.pattox.simpletransport.entity.ConveyorDetectorEntity;
import net.pattox.simpletransport.init.Conveyorbelts;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class ConveyorDetector extends BlockWithEntity {

    public ConveyorDetector(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState()
                .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
                .with(Properties.POWER, 0)
                .with(Properties.POWERED, false)
        );
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ConveyorDetectorEntity(pos, state);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        // When the player is sneaking, the conveyor will be placed rotated.
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayer().isSneaking() ? ctx.getPlayerFacing().getOpposite() : ctx.getPlayerFacing());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(
                Properties.POWER,
                Properties.HORIZONTAL_FACING,
                Properties.POWERED);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext entityContext) {
        // Set the bounding-box
        return VoxelShapes.cuboid(0, 0, 0, 1, (1F / 16F), 1);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, Conveyorbelts.CONVEYOR_DETECTOR_ENTITY, ConveyorDetectorEntity::tick);
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return false;
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    // Returns the signal for this block. So 'true' when the proper key is inserted
    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(Properties.POWER);
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        // Rotating, so we only output at the back
        return state.get(Properties.POWER);
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return this.getWeakRedstonePower(state, world, pos, direction);
    }
}
