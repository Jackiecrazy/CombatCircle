package jackiecrazy.combatcircle.move.argument.number;

import net.minecraft.world.entity.Entity;

public class FixedNumberArgument extends NumberArgument{
    double number;

    public FixedNumberArgument(double number) {
        this.number = number;
    }

    @Override
    public double resolve(Entity caster, Entity target) {
        return number;
    }
}
