package jackiecrazy.combatcircle.move;

import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import jackiecrazy.combatcircle.move.condition.Condition;
import jackiecrazy.combatcircle.move.condition.TrueCondition;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class MovesetGoal extends Goal {
    /*
     * the mob's goal holds the entire moveset.
     * the first move is read and executes until the timer ends or the move is interrupted in any way, then the move index/move is updated and the process repeats until the move index exceeds the current list size in any way.
     * the mob relinquishes control to the combat manager after the end of one moveset.
     */
    protected int weight;
    private Condition condition = new TrueCondition();
    protected MovesetWrapper set;
    protected PathfinderMob mob;
    protected LivingEntity target;

    public MovesetGoal(PathfinderMob entityIn, int weight, MovesetWrapper wrapper, Condition cond) {
        super();
        mob = entityIn;
//        WaitAction test = new WaitAction();
//        test.max_time = 20;
//        DebugAction debug = new DebugAction();
//        debug.condition = new StartAtCondition(3);
//        test.waiting.add(debug);
//        actions.add(test);
        this.weight = weight;
        condition = cond;
        set = wrapper;
    }

    @Override
    public boolean canUse() {
        if (mob.getTarget() == null) return false;
        if (!condition.evaluate(null, mob, mob.getTarget())) return false;
        target = mob.getTarget();
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        //ends when the index goes out of bounds, either by a forced jump or by natural progression.
        return set.executing();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void start() {
        super.start();
        set.start();
    }

    @Override
    public void stop() {
        super.stop();
        target = null;
        set.reset();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        set.tick(mob, target);
    }

    @Override
    public void setFlags(@NotNull EnumSet<Flag> flags) {
        super.setFlags(flags);
    }
}