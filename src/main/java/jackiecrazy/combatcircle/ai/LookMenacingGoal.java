package jackiecrazy.combatcircle.ai;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.utils.CombatManager;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

/**
 * this goal causes mobs to stand still and stare at the player
 */
public class LookMenacingGoal extends LookAtPlayerGoal {
    static final EnumSet<Flag> mutex = EnumSet.allOf(Flag.class);
    int strafeTick = 0;
    boolean flip = false;

    public LookMenacingGoal(Mob bind) {
        super(bind, Player.class, CombatCircle.CIRCLE_SIZE, 1);
    }

    @Override
    public boolean canUse() {
        lookAt = mob.getTarget();
        if (mob.getTarget() == null) return false;
        //too far away, just charge in
        if (mob.getTarget().distanceToSqr(mob) > CombatCircle.SPREAD_DISTANCE * CombatCircle.SPREAD_DISTANCE)
            return false;
        CombatManager cm = CombatManager.getManagerFor(mob.getTarget());
        return !cm.hasAttacker(mob);
    }

    @Override
    public boolean canContinueToUse() {
        if (strafeTick > 60 && mob.tickCount % 24 == 0) return false;
        return canUse();
    }

    @Override
    public void start() {
        super.start();
        flip=mob.tickCount%7==0;
        strafeTick = 0;
    }

    @Override
    public void stop() {
        strafeTick = 0;
        flip = !flip;
        super.stop();
    }

    @Override
    public void tick() {
        strafeTick++;
        if (mob.getTarget() != null)
            mob.lookAt(mob.getTarget(), 30, 30);
        if (strafeTick > 20) {
            mob.getMoveControl().strafe(0, flip ? 0.2f : -0.2f);
        }
        super.tick();
    }

    @Override
    public boolean isInterruptable() {
        return true;
    }

    @Override
    public EnumSet<Flag> getFlags() {
        return mutex;
    }
}
