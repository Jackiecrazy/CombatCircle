package jackiecrazy.combatcircle.utils;

import jackiecrazy.combatcircle.CombatCircle;
import net.minecraft.world.entity.Entity;
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

    public static List<Mob> getCrowd(Level level, Entity point, float radius) {
        return level.getEntitiesOfClass(Mob.class, point.getBoundingBox().inflate(radius), (a -> a.getTarget() != null));
    }
}
