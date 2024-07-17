package jackiecrazy.combatcircle.move.action;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.argument.Argument;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SetItemAction extends Action {
    private Argument<Entity> wielder = CasterEntityArgument.INSTANCE;
    private Argument<ItemStack> stack;
    private EquipmentSlot slot;

    @Override
    public int perform(MovesetWrapper wrapper, Action parent, @Nullable Entity performer, Entity target) {
        if (wielder.resolve(wrapper, parent, performer, target) instanceof LivingEntity e)
            e.setItemSlot(slot, stack.resolve(wrapper, parent, performer, target).copy());
        return 0;
    }
}
