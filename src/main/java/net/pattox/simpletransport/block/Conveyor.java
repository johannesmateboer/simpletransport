package net.pattox.simpletransport.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.pattox.simpletransport.util.MovementUtil;

public class Conveyor extends Block {
    public Conveyor(Settings settings) {
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
        return VoxelShapes.cuboid(0, 0, 0, 1, (1F / 16F), 1);
    }

    @Override
    public void onEntityCollision(BlockState blockState, World world, BlockPos blockPos, Entity entity) {
        // Player can sneak over the conveyor
        if (entity instanceof PlayerEntity && entity.isSneaking()) {
            return;
        }
        // Check if the conveyor should move the entity
        /*if (!entity.isOnGround() || (entity.getY() - blockPos.getY()) != (4F / 16F)) {
            return;
        }*/
        // Do the movement.
        MovementUtil.pushEntity(entity, blockPos, 1.0F / 16.0F, blockState.get(Properties.HORIZONTAL_FACING));
    }

}
