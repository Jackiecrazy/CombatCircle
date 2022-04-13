package jackiecrazy.combatcircle.utils;

import jackiecrazy.combatcircle.CombatCircle;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;

public class GoalUtils {
    public static List<MobEntity> getCrowd(World level, BlockPos point, float radius) {
        return level.getEntitiesOfClass(MobEntity.class, new AxisAlignedBB(point).inflate(radius), (a -> a.getTarget() != null));
    }

    public static List<MobEntity> getCrowd(World level, Vector3d point, float radius) {
        return getCrowd(level, new BlockPos(point), radius);
    }

    public static boolean socialDistancing(LivingEntity mob) {
        return getCrowd(mob.level, mob.blockPosition(), CombatCircle.INNER_DISTANCE).size() == 1;
    }
}
