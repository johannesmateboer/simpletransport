package net.pattox.simpletransport.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.pattox.simpletransport.SimpleTransport;
import net.pattox.simpletransport.entity.ExtractorEntity;
import net.pattox.simpletransport.init.Conveyorbelts;
import net.pattox.simpletransport.util.MovementUtil;
import net.pattox.simpletransport.util.VoxelUtil;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class Extractor extends BlockWithEntity {

    public Extractor(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState()
                .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
        );
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ExtractorEntity(pos, state);
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
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext entityContext) {
        // Set the bounding-box
        VoxelShape belt = VoxelUtil.createCuboid(0, 0, 0, 16, 1, 16);
        VoxelShape connector = VoxelUtil.createCuboid(1, 1, 12, 15, 10, 16);
        VoxelShape extractor = VoxelShapes.combine(belt, connector, BooleanBiFunction.OR);
        return VoxelUtil.rotateShape(Direction.NORTH, blockState.get(Properties.HORIZONTAL_FACING), extractor);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, Conveyorbelts.EXTRACTOR_ENTITY, ExtractorEntity::tick);
    }

    @Override
    public void onEntityCollision(BlockState blockState, World world, BlockPos blockPos, Entity entity) {
        // Player can sneak over the conveyor
        if (entity instanceof PlayerEntity) {
            return;
        }
        // Do the movement.
        MovementUtil.pushEntity(entity, blockPos, 1.0F / 16.0F, blockState.get(Properties.HORIZONTAL_FACING));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            //This will call the createScreenHandlerFactory method from BlockWithEntity, which will return our blockEntity casted to
            //a namedScreenHandlerFactory. If your block class does not extend BlockWithEntity, it needs to implement createScreenHandlerFactory.
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

            if (screenHandlerFactory != null) {
                //With this call the server will request the client to open the appropriate Screenhandler
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }
}
