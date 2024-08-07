package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import net.minecraft.world.entity.Entity;

public class TrueCondition extends Condition {
    public static final TrueCondition INSTANCE = new TrueCondition();

    @Override
    public Boolean resolve(MovesetWrapper wrapper, Action parent, Entity performer, Entity target) {
        return true;
    }
}
