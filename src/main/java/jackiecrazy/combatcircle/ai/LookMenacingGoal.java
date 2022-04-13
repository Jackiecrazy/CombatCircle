package jackiecrazy.combatcircle.ai;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.utils.CombatManager;
import jackiecrazy.combatcircle.utils.GoalUtils;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class LookMenacingGoal extends LookAtGoal {
    static final EnumSet<Flag> mutex = EnumSet.allOf(Flag.class);

    public LookMenacingGoal(MobEntity bind) {
        super(bind, PlayerEntity.class, CombatCircle.CIRCLE_SIZE, 1);
    }

    @Override
    public boolean canUse() {
        lookAt = mob.getTarget();
        if (mob.getTarget() == null) return false;
        CombatManager cm = CombatManager.getManagerFor(mob.getTarget());
        return !cm.hasAttacker(mob) && GoalUtils.socialDistancing(mob);
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public EnumSet<Flag> getFlags() {
        return mutex;
    }
}
