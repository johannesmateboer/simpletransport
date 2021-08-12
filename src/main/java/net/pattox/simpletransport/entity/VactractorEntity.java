package net.pattox.simpletransport.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pattox.simpletransport.SimpleTransport;
import net.pattox.simpletransport.util.ItemSpawner;

import java.util.List;

public class VactractorEntity extends BlockEntity {

    // Tick-controller
    private int interval = 0;

    public VactractorEntity(BlockPos pos, BlockState state) {
        super(SimpleTransport.VACTRACTOR_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, VactractorEntity blockEntity) {
        if (world.isClient()) {
            return;
        }
        if (blockEntity.interval > 40) {
            //Target is below
            BlockPos targetPos = pos.offset(Direction.DOWN);

            for (BlockPos loc:  BlockPos.iterateInSquare(pos.up(), 2, Direction.NORTH, Direction.EAST)) {
                List<Entity> entities = world.getOtherEntities(null, new Box(loc));
                // Iterate over the entities
                for (Entity entity : entities) {
                    if (entity instanceof ItemEntity) {
                        ItemStack stack = ((ItemEntity) entity).getStack();
                        ItemSpawner.spawnOnBelt(world, targetPos, stack);
                        entity.remove(Entity.RemovalReason.DISCARDED);
                    }
                }
            }
            blockEntity.interval = 0;
        } else {
            blockEntity.interval = blockEntity.interval + 1;
        }
    }
}