package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public abstract class VectorArgument extends Argument {
    private NumberArgument scale = NumberArgument.ONE;
    private NumberArgument min_length = NumberArgument.ZERO;
    private NumberArgument max_length = NumberArgument.ZERO;

    public Vec3 resolve(Entity caster, Entity target) {
        double minLength = min_length.resolve(caster, target);
        double maxLength = max_length.resolve(caster, target);
        double sc = scale.resolve(caster, target);
        Vec3 ret = _resolve(caster, target).scale(sc);
        if (minLength > 0 && ret.lengthSqr() < minLength * minLength) {
            ret=ret.scale(minLength / ret.length());
        }
        if (maxLength > 0 && ret.lengthSqr() > maxLength * maxLength) {
            ret=ret.scale(maxLength / ret.length());
        }
        return ret;
    }

    abstract Vec3 _resolve(Entity caster, Entity target);
}
