package jackiecrazy.combatcircle.ai;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.utils.CombatManager;
import jackiecrazy.combatcircle.utils.GoalUtils;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;

public class LookMenacingGoal extends LookAtGoal {
    static final EnumSet<Flag> mutex = EnumSet.allOf(Flag.class);
    int strafeTick = 0;
    boolean flip = false;

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
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public EnumSet<Flag> getFlags() {
        return mutex;
    }

    @Override
    public void start() {
        super.start();
        strafeTick = 0;
    }

    @Override
    public void stop() {
        strafeTick = 0;
        super.stop();
    }

    @Override
    public void tick() {
//        strafeTick++;
//        if(mob.getTarget()!=null)
//        mob.lookAt(mob.getTarget(), 30, 30);
//        if (strafeTick > 30 && mob.getRandom().nextInt(20) == 0) {
//            flip = !flip;
//            strafeTick = 0;
//        }
//        if (strafeTick > 20) {
//            mob.getMoveControl().strafe(0, flip ? 0.2f : -0.2f);
//        }
        super.tick();
    }
}
