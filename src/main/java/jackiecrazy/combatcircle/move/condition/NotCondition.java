package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import net.minecraft.world.entity.Entity;

public class NotCondition extends Condition {
    Condition of;

    @Override
    public boolean resolve(MovesetWrapper wrapper, Entity performer, Entity target) {
        return !of.resolve(wrapper, performer, target);
    }
}
