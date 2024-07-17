package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class DistanceArgument implements Argument<Double> {
    private Argument<Vec3> first, second;

    @Override
    public Double resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        return first.resolve(wrapper, parent, caster, target).distanceTo(second.resolve(wrapper, parent, caster, target));
    }
}
