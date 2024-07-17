package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.move.condition.TrueCondition;
import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;

public class RayTraceVectorArgument extends VectorArgument {
    Argument<Vec3> position;
    Argument<Vec3> direction;
    Argument<Double> distance;
    Condition scans_entities = TrueCondition.INSTANCE;
    ClipContext.Block block_clip = ClipContext.Block.COLLIDER;
    ClipContext.Fluid fluid_clip = ClipContext.Fluid.NONE;

    @Override
    public Vec3 _resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        Vec3 start = position.resolve(wrapper, parent, caster, target);
        Vec3 look = direction.resolve(wrapper, parent, caster, target);
        double range = distance.resolve(wrapper, parent, caster, target);
        return GeneralUtils.raytraceAnything(caster.level(), start, look, range, scans_entities.resolve(wrapper, parent, caster, target), block_clip, fluid_clip).getLocation();
    }
}
