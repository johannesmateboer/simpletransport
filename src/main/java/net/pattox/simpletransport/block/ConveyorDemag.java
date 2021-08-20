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
import net.pattox.simpletransport.entity.ConveyorDemagEntity;
import net.pattox.simpletransport.entity.ConveyorEntity;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class ConveyorDemag extends BlockWithEntity {
    public ConveyorDemag(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ConveyorDemagEntity(pos, state);
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
        return checkType(type, SimpleTransport.CONVEYOR_DEMAG_ENTITY, ConveyorDemagEntity::tick);
    }



}
