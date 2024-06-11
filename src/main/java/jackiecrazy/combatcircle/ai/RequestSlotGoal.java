package jackiecrazy.combatcircle.ai;

import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.capability.MovesetData;
import jackiecrazy.combatcircle.move.MovesetManager;
import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.utils.CombatManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class RequestSlotGoal extends Goal {
    protected PathfinderMob mob;
    protected LivingEntity target;
    private int startTime;

    public RequestSlotGoal(PathfinderMob entityIn) {
        super();
        mob = entityIn;
    }


    @Override
    public boolean canUse() {
        if (mob.getTarget() == null) return false;
        if (CombatManager.getManagerFor(mob.getTarget()).addAttacker(mob, null)) return false;
        target = mob.getTarget();
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        //ends when the index goes out of bounds, either by a forced jump or by natural progression.
        return startTime < mob.tickCount + CombatCircle.MAXIMUM_CHASE_TIME;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void start() {
        super.start();
        startTime = mob.tickCount;
    }

    @Override
    public void stop() {
        super.stop();
        CombatManager.getManagerFor(target).removeAttacker(mob, null);
        target = null;
    }
}
