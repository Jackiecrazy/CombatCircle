package jackiecrazy.combatcircle.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;

public class TargetingUtils {
    public static boolean isAlly(Entity entity, Entity of) {
        if (of == entity) return true;
        if (entity instanceof TameableEntity && of instanceof LivingEntity && ((TameableEntity) entity).isOwnedBy((LivingEntity) of))
            return true;
        if (of instanceof TameableEntity && entity instanceof LivingEntity && ((TameableEntity) of).isOwnedBy((LivingEntity) entity))
            return true;
        if (entity.isAlliedTo(of)) return true;
        return false;
    }
}