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
    private boolean inner;

    public LookMenacingGoal(MobEntity bind, boolean inner) {
        super(bind, PlayerEntity.class, inner ? CombatCircle.INNER_DISTANCE : CombatCircle.OUTER_DISTANCE, 1);
        this.inner = inner;
    }

    @Override
    public boolean canUse() {
        lookAt = mob.getTarget();
        if (mob.getTarget() == null) return false;
        CombatManager cm = CombatManager.getManagerFor(mob.getTarget());
        return (inner ? !cm.hasAttacker(mob) : !cm.hasMob(mob)) && GoalUtils.socialDistancing(mob);
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
