package jackiecrazy.combatcircle.move.condition;

import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.LivingEntity;

public class StartAtCondition extends Condition {
    private int time;
    @Override
    public boolean evaluate(TimerAction parent, LivingEntity performer, LivingEntity target) {
        return false;//parent.getTimer()==time;
    }
}
