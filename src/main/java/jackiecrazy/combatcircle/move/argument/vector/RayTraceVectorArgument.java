package jackiecrazy.combatcircle.move.argument.vector;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.number.NumberArgument;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.move.condition.TrueCondition;
import jackiecrazy.footwork.utils.GeneralUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;

public class RayTraceVectorArgument extends VectorArgument {
    VectorArgument position;
    VectorArgument direction;
    NumberArgument distance;
    Condition scans_entities = TrueCondition.INSTANCE;
    ClipContext.Block block_clip = ClipContext.Block.COLLIDER;
    ClipContext.Fluid fluid_clip = ClipContext.Fluid.NONE;

    @Override
    public Vec3 _resolve(MovesetWrapper wrapper, Entity caster, Entity target) {
        Vec3 start = position.resolveAsVector(wrapper, caster, target);
        Vec3 look = direction.resolveAsVector(wrapper, caster, target);
        double range = distance.resolve(wrapper, caster, target);
        return GeneralUtils.raytraceAnything(caster.level(), start, look, range, scans_entities.resolve(wrapper, caster, target), block_clip, fluid_clip).getLocation();
    }
}
