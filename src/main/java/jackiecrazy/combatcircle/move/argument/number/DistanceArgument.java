package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class DistanceArgument implements Argument<Double> {
    private Argument<?> first, second;

    @Override
    public Double resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        Object f = first.resolve(wrapper, parent, caster, target), s = second.resolve(wrapper, parent, caster, target);
        Vec3 from = null, to = null;
        if (f instanceof Entity e)
            from = e.position();
        else if (f instanceof Vec3 e)
            from = e;
        else if (f instanceof BlockPos e)
            from = e.getCenter();
        if (s instanceof Entity e)
            to = e.position();
        else if (s instanceof Vec3 e)
            to = e;
        else if (s instanceof BlockPos e)
            to = e.getCenter();
        if (from != null && to != null)
            return from.distanceTo(to);
        return 0d;
    }
}
