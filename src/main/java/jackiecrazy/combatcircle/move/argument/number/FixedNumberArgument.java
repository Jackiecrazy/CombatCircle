package jackiecrazy.combatcircle.move.argument.number;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;

public class FixedNumberArgument extends NumberArgument{
    double number;

    public FixedNumberArgument(double number) {
        this.number = number;
    }

    public FixedNumberArgument() {
    }

    @Override
    public double resolve(MovesetWrapper wrapper, Entity caster, Entity target) {
        return number;
    }
}
