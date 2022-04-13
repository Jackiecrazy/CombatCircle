package jackiecrazy.combatcircle.utils;

import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;

public class MotionUtils {
    public static Vector3d convertToFacing(LivingEntity mover, Vector3d moveVec) {
        return moveVec.yRot(GeneralUtils.rad(-mover.yHeadRot)).xRot(GeneralUtils.rad(-mover.xRot));
    }
}
