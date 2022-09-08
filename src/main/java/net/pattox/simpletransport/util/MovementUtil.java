package net.pattox.simpletransport.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class MovementUtil {
    public static void pushEntity(Entity entity, BlockPos pos, float speed, Direction facing) {
        pushEntity(entity, pos, speed, facing, true, false);
    }

    public static void pushEntity(Entity entity, BlockPos pos, float speed, Direction facing, boolean shouldCenter, boolean shouldGoUp) {
        double motionX = entity.getVelocity().getX();
        double motionZ = entity.getVelocity().getZ();

        double velX = 0;
        double velY = 0;
        double velZ = 0;
        float throwAddAmount = 0;

        if (shouldGoUp) {
            velY = 0.075F;
            speed = speed + 0.035F;
        }
        speed = speed + throwAddAmount;

        if (speed * facing.getOffsetX() > 0 && motionX < speed) {
            velX = speed / 2;
        } else if (speed * facing.getOffsetX() < 0 && motionX > -speed) {
            velX = -speed / 2;
        }

        if (speed * facing.getOffsetZ() > 0 && motionZ < speed) {
            velZ = speed / 2;
        } else if (speed * facing.getOffsetZ() < 0 && motionZ > -speed) {
            velZ = -speed / 2;
        }

        if (shouldCenter) {
            centerEntity(entity, pos, speed, facing);
        }

        entity.addVelocity(velX, velY, velZ);
        entity.velocityDirty = true;
    }

    private static void centerEntity(Entity entity, BlockPos pos, float speed, Direction facing) {
        if (speed * facing.getOffsetX() > 0 || speed * facing.getOffsetX() < 0) {
            centerZ(entity, pos);
        }

        if (speed * facing.getOffsetZ() > 0 || speed * facing.getOffsetZ() < 0) {
            centerX(entity, pos);
        }
    }

    public static void centerZ(Entity entity, BlockPos pos) {
        if (entity.getZ() > pos.getZ() + .50) {
            entity.addVelocity(0, 0, -0.1F);
        } else if (entity.getZ() < pos.getZ() + .40) {
            entity.addVelocity(0, 0, 0.1F);
        } else {
            entity.setVelocity(entity.getVelocity().getX(), entity.getVelocity().getY(), 0);
        }
    }

    public static void centerX(Entity entity, BlockPos pos) {
        if (entity.getX() > pos.getX() + .55) {
            entity.addVelocity(-0.1F, 0, 0);
        } else if (entity.getX() < pos.getX() + .45) {
            entity.addVelocity(0.1F, 0, 0);
        } else {
            entity.setVelocity(0, entity.getVelocity().getY(), entity.getVelocity().getZ());
        }
    }
}
