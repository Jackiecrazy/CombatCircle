package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class CurrentHealthArgument extends NumberArgument {
    private EntityArgument reference_point;

    @Override
    public double resolve(MovesetWrapper wrapper, Entity caster, Entity target) {
        return reference_point.resolveAsEntity(wrapper, caster, target) instanceof LivingEntity le ? le.getHealth() : 0;
    }
}
