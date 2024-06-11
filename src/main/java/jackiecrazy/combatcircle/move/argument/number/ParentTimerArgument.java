package jackiecrazy.combatcircle.move.argument.number;

import net.minecraft.world.entity.Entity;

public class ParentTimerArgument extends NumberArgument{

    public ParentTimerArgument() {
    }

    @Override
    public double resolve(Entity caster, Entity target) {
        return number;
    }
}
