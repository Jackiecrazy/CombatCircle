package jackiecrazy.combatcircle.utils;

import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class MotionUtils {
    public static Vec3 convertToFacing(LivingEntity mover, Vec3 moveVec) {
        return moveVec.yRot(GeneralUtils.rad(-mover.yHeadRot)).xRot(GeneralUtils.rad(-mover.getXRot()));
    }

}
