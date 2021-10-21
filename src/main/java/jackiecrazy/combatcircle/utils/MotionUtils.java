package jackiecrazy.combatcircle.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;

public class MotionUtils {
    public static Vector3d convertToFacing(LivingEntity mover, Vector3d moveVec){
        return moveVec.xRot(mover.xRot);
    }
}
