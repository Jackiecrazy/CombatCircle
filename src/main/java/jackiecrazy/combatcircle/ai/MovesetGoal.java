package jackiecrazy.combatcircle.ai;

import jackiecrazy.combatcircle.capability.MovesetData;
import jackiecrazy.combatcircle.move.MovesetManager;
import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.utils.CombatManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class MovesetGoal extends Goal {
    static final EnumSet<Flag> mutex = EnumSet.of(Flag.LOOK, Flag.JUMP, Flag.MOVE);//putting target here breaks the mob's innate targeting tasks because this runs on targetSelector
    protected MovesetManager set;
    protected MovesetWrapper wrap;
    protected PathfinderMob mob;
    protected LivingEntity target;
    /*
     * the mob's goal holds the entire moveset.
     * the first move is read and executes until the timer ends or the move is interrupted in any way, then the move index/move is updated and the process repeats until the move index exceeds the current list size in any way.
     * the mob relinquishes control to the combat manager after the end of one moveset.
     */
    private int startTime;

    public MovesetGoal(PathfinderMob entityIn, MovesetManager mm) {
        super();
        mob = entityIn;
        MovesetData.getCap(mob).setMovesetManager(mm);
        set = mm;
    }

    @Override
    public EnumSet<Flag> getFlags() {
        return mutex;
    }


    @Override
    public boolean canUse() {
        if (mob.getTarget() == null) return false;
        CombatManager.getManagerFor(mob.getTarget()).addMob(mob);
        wrap = set.selectMove(mob.getTarget(), CombatManager.getManagerFor(mob.getTarget()).getRemainingAttackPower());
        if (wrap == null) return false;
        if (!CombatManager.getManagerFor(mob.getTarget()).addAttacker(mob, this)) return false;
        target = mob.getTarget();
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        //ends when the index goes out of bounds, either by a forced jump or by natural progression.
        return wrap.executing();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void start() {
        super.start();
        startTime = mob.tickCount;
        wrap.start();
    }

    @Override
    public void stop() {
        super.stop();
        CombatManager.getManagerFor(target).removeAttacker(mob, this);
        target = null;
        wrap.reset();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        wrap.tick(mob, target);
    }

    public float getWeight() {
        return wrap.getCurrentWeight();
    }
}
