package jackiecrazy.combatcircle.move.argument.stack;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public abstract class ItemStackArgument extends Argument {
    public abstract ItemStack resolve(MovesetWrapper wrapper, TimerAction parent, Entity caster, Entity target);
}
