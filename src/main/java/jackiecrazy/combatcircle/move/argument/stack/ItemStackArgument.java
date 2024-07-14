package jackiecrazy.combatcircle.move.argument.stack;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public abstract class ItemStackArgument {
    public abstract ItemStack resolve(MovesetWrapper wrapper, Entity caster, Entity target);
}
