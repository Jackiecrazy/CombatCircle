package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.number.FixedNumberArgument;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ClampedVectorArgument extends VectorArgument {
    Argument<Vec3> clamp;
    Argument<Double> max_x = FixedNumberArgument.MAX, max_y = FixedNumberArgument.MAX, max_z = FixedNumberArgument.MAX,
            min_x = FixedNumberArgument.MIN, min_y = FixedNumberArgument.MIN, min_z = FixedNumberArgument.MIN;

    @Override
    public Vec3 _resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        Vec3 resolve = clamp.resolve(wrapper, parent, caster, target);
        return new Vec3(
                Mth.clamp(resolve.x, min_x.resolve(wrapper, parent, caster, target), max_x.resolve(wrapper, parent, caster, target)),
                Mth.clamp(resolve.y, min_y.resolve(wrapper, parent, caster, target), max_y.resolve(wrapper, parent, caster, target)),
                Mth.clamp(resolve.z, min_z.resolve(wrapper, parent, caster, target), max_z.resolve(wrapper, parent, caster, target))
        );
    }
}
