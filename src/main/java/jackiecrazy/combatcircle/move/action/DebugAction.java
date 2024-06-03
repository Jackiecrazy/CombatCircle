package jackiecrazy.combatcircle.move.action;

import net.minecraft.world.entity.LivingEntity;

public class DebugAction extends Action{
    @Override
    public void perform(LivingEntity performer, LivingEntity target) {
        System.out.println("here are the performer and target:");
        System.out.println(performer);
        System.out.println(target);
    }
}
