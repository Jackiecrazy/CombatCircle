package jackiecrazy.combatcircle.move.argument.stack;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.argument.entity.CasterEntityArgument;
import jackiecrazy.combatcircle.move.argument.entity.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class EquippedItemArgument extends ItemStackArgument {
    private EntityArgument wielder = CasterEntityArgument.INSTANCE;
    private EquipmentSlot slot=EquipmentSlot.MAINHAND;

    @Override
    public ItemStack resolve(MovesetWrapper wrapper, TimerAction parent, Entity caster, Entity target) {
        return wielder.resolveAsEntity(wrapper, parent, caster, target) instanceof LivingEntity e ? e.getItemBySlot(slot) : ItemStack.EMPTY;
    }
}
