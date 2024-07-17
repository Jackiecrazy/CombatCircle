package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class HealthPercArgument extends NumberArgument{
    private Argument<Entity> reference_point;

    @Override
    public Double resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target) {
        return reference_point.resolve(wrapper, parent, caster, target) instanceof LivingEntity le ? (double)(le.getHealth()/le.getMaxHealth()) : 1.0;
    }
}
