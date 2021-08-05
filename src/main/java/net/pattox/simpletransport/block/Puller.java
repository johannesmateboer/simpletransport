package net.pattox.simpletransport.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.pattox.simpletransport.SimpleTransport;
import net.pattox.simpletransport.entity.PullerEntity;
import net.pattox.simpletransport.util.VoxelUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Puller extends BlockWithEntity {
    public Puller(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PullerEntity(pos, state);
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
        return VoxelUtil.createCuboid(0, 0, 0, 16, 11, 16);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, SimpleTransport.PULLER_ENTITY, PullerEntity::tick);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        PullerEntity ee = (PullerEntity) world.getBlockEntity(pos);
        assert ee != null;

        if (Objects.equals(player.getStackInHand(hand).getItem().getTranslationKey(), "block.minecraft.redstone_torch")) {
            // Toggle editmode
            if (!ee.getEditmode()) {
                ee.enableEditmode();
                sendInfo(world, player, "Filter-editmode active! You can make your changes now.");
            } else {
                ee.disableEditmode();
                sendInfo(world, player, "Filter-editmode ended! Your changes have been saved.");
            }
            return ActionResult.SUCCESS;
        }

        if (ee.getEditmode() && !player.getStackInHand(hand).isEmpty()) {
            ee.setFilterItem(player.getStackInHand(hand).getItem().getTranslationKey());
            sendInfo(world, player, "Puller set to " + ee.getFilterItem());
            return ActionResult.SUCCESS;
        }

        if (ee.getEditmode() && player.getStackInHand(hand).isEmpty()) {
            ee.clearFilterItem();
            sendInfo(world, player, "Filter cleared. Pulling everything.");
            return ActionResult.SUCCESS;
        }

        if (ee.getEditmode()) {
            sendInfo(world, player, "Edit-mode active!");
        } else {
            sendInfo(world, player, "Edit-mode inactive.");
            if (!Objects.equals(ee.getFilterItem(), "")) {
                sendInfo(world, player, "Filtering " + ee.getFilterItem());
            } else {
                sendInfo(world, player, "Pulling no items");
            }
        }
        return ActionResult.SUCCESS;
    }

    private void sendInfo(World world, PlayerEntity player, String message) {
        if (world.isClient()) {
            player.sendSystemMessage(Text.of(message), Util.NIL_UUID);
        }
    }
}
