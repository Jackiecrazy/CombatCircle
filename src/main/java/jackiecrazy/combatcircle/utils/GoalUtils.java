package jackiecrazy.combatcircle.utils;

import jackiecrazy.combatcircle.CombatCircle;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import java.util.List;

public class GoalUtils {
    public static List<Mob> getCrowd(Level level, BlockPos point, float radius) {
        return level.getEntitiesOfClass(Mob.class, new AABB(point).inflate(radius), (a -> a.getTarget() != null));
    }

    public static List<Mob> getCrowd(Level level, Vec3 point, float radius) {
        return getCrowd(level, new BlockPos(point), radius);
    }

    public static boolean socialDistancing(LivingEntity mob) {
        return getCrowd(mob.level, mob.blockPosition(), CombatCircle.SPREAD_DISTANCE).size() == 1;
    }
}
