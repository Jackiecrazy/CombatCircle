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
    static final EnumSet<Flag> mutex = EnumSet.of(Flag.LOOK, Flag.MOVE);
    int strafeTick = 0;
    float strafeX, strafeZ;
    boolean flip = false;

    public LookMenacingGoal(Mob bind) {
        super(bind, Player.class, CombatCircle.CIRCLE_SIZE, 1);
    }

    @Override
    public boolean canUse() {
        strafeTick--;
        if (strafeTick > 0) return false;
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
        if (strafeTick > 60 && mob.tickCount % 60 == 0) return false;
        return canUse();
    }

    @Override
    public void start() {
        //todo wolf pack locks up entire goalSelector and prevents strafing
        super.start();
        flip = mob.tickCount % 7 == 0;
        strafeX = CombatCircle.rand.nextFloat() * 0.5f-0.25f;
        strafeZ = CombatCircle.rand.nextFloat() * 0.3f-0.15f;
        strafeTick = 0;
    }

    @Override
    public void stop() {
        flip = !flip;
        mob.getMoveControl().strafe(0, 0);
        super.stop();
    }

    @Override
    public void tick() {
        strafeTick++;
        if (mob.getTarget() != null) {
            mob.getLookControl().setLookAt(mob.getTarget());
            mob.lookAt(mob.getTarget(), 30, 30);
        }
        mob.getMoveControl().strafe(strafeZ, strafeX);
        super.tick();
    }

    @Override
    public EnumSet<Flag> getFlags() {
        return mutex;
    }
}
