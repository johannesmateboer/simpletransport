package net.pattox.simpletransport.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.pattox.simpletransport.init.Conveyorbelts;
import net.pattox.simpletransport.util.MovementUtil;

import java.util.List;

public class ConveyorDetectorEntity extends BlockEntity {

    public ConveyorDetectorEntity(BlockPos pos, BlockState state) {
        super(Conveyorbelts.CONVEYOR_DETECTOR_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, ConveyorDetectorEntity blockEntity) {
        if (world.isClient()) {
            return;
        }
        List<Entity> entities = world.getOtherEntities(null, new Box(pos));
        if (entities.size() > 0) {
            world.setBlockState(pos, state.with(Properties.POWERED, true));
            world.setBlockState(pos, state.with(Properties.POWER, 15));

            for (Entity entity : entities) {
                MovementUtil.pushEntity(entity, pos, 0.5F / 16.0F, state.get(Properties.HORIZONTAL_FACING));
            }
        } else {
            world.setBlockState(pos, state.with(Properties.POWERED, false));
            world.setBlockState(pos, state.with(Properties.POWER, 0));
        }

    }

}
