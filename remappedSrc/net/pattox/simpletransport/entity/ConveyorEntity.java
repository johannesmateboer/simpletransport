package net.pattox.simpletransport.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.pattox.simpletransport.SimpleTransport;
import net.pattox.simpletransport.init.Conveyorbelts;
import net.pattox.simpletransport.util.MovementUtil;

import java.util.List;

public class ConveyorEntity extends BlockEntity {

    public ConveyorEntity(BlockPos pos, BlockState state) {
        super(Conveyorbelts.CONVEYOR_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, ConveyorEntity blockEntity) {
        if (world.isClient()) {
            return;
        }

        List<Entity> entities = world.getOtherEntities(null, new Box(pos));
        for (Entity entity : entities) {
            MovementUtil.pushEntity(entity, pos, 0.5F / 16.0F, state.get(Properties.HORIZONTAL_FACING));
        }
    }
}
