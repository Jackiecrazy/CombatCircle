package jackiecrazy.combatcircle.move;

import jackiecrazy.combatcircle.capability.MovesetData;
import jackiecrazy.combatcircle.utils.CombatManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class MovesetGoal extends Goal {
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
    public boolean canUse() {
        if (mob.getTarget() == null) return false;
        if (mob.tickCount % 20 != 0) return false;
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

    @Override
    public void setFlags(@NotNull EnumSet<Flag> flags) {
        super.setFlags(flags);
    }

    public float getWeight() {
        return wrap.getCurrentWeight();
    }
}
