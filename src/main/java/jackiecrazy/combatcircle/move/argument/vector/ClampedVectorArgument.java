package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ClampedVectorArgument extends VectorArgument {
    VectorArgument clamp;
    NumberArgument max_x = NumberArgument.MAX, max_y = NumberArgument.MAX, max_z = NumberArgument.MAX,
            min_x = NumberArgument.MIN, min_y = NumberArgument.MIN, min_z = NumberArgument.MIN;

    @Override
    public Vec3 _resolve(MovesetWrapper wrapper, TimerAction parent, Entity caster, Entity target) {
        Vec3 resolve = clamp.resolveAsVector(wrapper, parent, caster, target);
        return new Vec3(
                Mth.clamp(resolve.x, min_x.resolve(wrapper, parent, caster, target), max_x.resolve(wrapper, parent, caster, target)),
                Mth.clamp(resolve.y, min_y.resolve(wrapper, parent, caster, target), max_y.resolve(wrapper, parent, caster, target)),
                Mth.clamp(resolve.z, min_z.resolve(wrapper, parent, caster, target), max_z.resolve(wrapper, parent, caster, target))
        );
    }
}
