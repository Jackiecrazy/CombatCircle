package jackiecrazy.combatcircle.move.argument;

import jackiecrazy.combatcircle.utils.MoveUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class Vector extends Argument {
    enum REFERENCE {
        COORDINATE,
        ABSOLUTE,
        RELATIVE
    }

    private double x, y, z;
    private REFERENCE reference_point;
    private transient Vec3 vec;

    public Vec3 resolve(LivingEntity caster) {
        if (vec == null)
            vec = new Vec3(x, y, z);
        return switch (reference_point) {
            case ABSOLUTE -> vec;
            case COORDINATE -> caster.position().subtract(vec);
            case RELATIVE -> MoveUtils.convertToFacing(caster, vec);
        };
    }
}
