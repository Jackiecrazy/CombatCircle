package jackiecrazy.combatcircle.actions;

import net.minecraft.entity.LivingEntity;

public class DebugAction extends Action{
    @Override
    public void perform(LivingEntity performer, LivingEntity target) {
        System.out.println("here are the performer and target:");
        System.out.println(performer);
        System.out.println(target);
    }
}
