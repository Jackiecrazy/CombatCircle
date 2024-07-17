package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public abstract class Condition implements Argument<Boolean> {

    public abstract Boolean resolve(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target);
}
