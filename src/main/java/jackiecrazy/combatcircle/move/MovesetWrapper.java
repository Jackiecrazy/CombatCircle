package jackiecrazy.combatcircle.move;

import jackiecrazy.combatcircle.move.action.timer.TimerAction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovesetWrapper {
    private List<TimerAction> actions;
    private Map<String, Vec3> vecStorage=new HashMap<>();
    private TimerAction currentMove;
    private int index = 0;

    public MovesetWrapper(List<TimerAction> actions) {
        this.actions = actions;
    }

    public boolean executing() {
        return index < actions.size() && index >= 0;
    }

    public void start() {
        currentMove = actions.get(0);
    }

    public void reset() {
        actions.forEach(TimerAction::reset);
        index = 0;
        currentMove = null;
    }

    public void tick(Entity performer, Entity target){
        int ret = currentMove.perform(this, null, performer, target);
        if (ret < 0) {
            //natural progression//
            index++;
            currentMove = actions.get(index % actions.size());
        }
        if (ret > 0) {
            //jump, reset everything//
            //TODO implement loop
            actions.forEach(TimerAction::reset);
            index = ret - 1;
            currentMove = actions.get(index % actions.size());
        }
    }
}
