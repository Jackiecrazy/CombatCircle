package jackiecrazy.combatcircle.move.argument.stack;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.Argument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public abstract class ItemStackArgument implements Argument<ItemStack> {
    public abstract ItemStack resolve(MovesetWrapper wrapper, Action parent, Entity caster, Entity target);
}
