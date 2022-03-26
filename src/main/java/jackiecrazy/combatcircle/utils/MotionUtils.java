package jackiecrazy.combatcircle.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;

public class MotionUtils {
    public static Vector3d convertToFacing(LivingEntity mover, Vector3d moveVec) {//FIXME does this work?
        return moveVec.yRot(GeneralUtils.rad(-mover.yRot)).xRot(GeneralUtils.rad(-mover.xRot));
    }
}
