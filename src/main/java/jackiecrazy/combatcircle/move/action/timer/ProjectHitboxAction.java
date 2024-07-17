package jackiecrazy.combatcircle.move.action.timer;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.SelectorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectHitboxAction extends TimerAction {
    /**
     * 0 for once only, other hitboxes repeat every x ticks.
     */
    private int hit_cooldown;
    private SelectorArgument selector;
    private List<Action> actions = new ArrayList<>();

    @Override
    public void start(MovesetWrapper wrapper, Entity performer, Entity target) {
        wrapper.setData(this, new HashMap<>());
    }

    @Override
    public int tick(MovesetWrapper wrapper, Entity performer, Entity target) {
        HashMap<Entity, Long> lastHit = wrapper.getData(this);
        for (Entity e : selector.resolve(wrapper, this, performer, target)) {
            if (hit_cooldown == 0 && lastHit.containsKey(e)) continue;
            if (lastHit.containsKey(e) && lastHit.get(e) < e.level().getGameTime() + hit_cooldown) continue;
            int childRet = runActions(wrapper, this, actions, performer, e);
            if (childRet != 0) return childRet;
            System.out.println("hit something "+e);
            lastHit.put(e, e.level().getGameTime());
        }
        return super.tick(wrapper, performer, target);
    }

    public void stop(MovesetWrapper wrapper, Entity performer, Entity target, boolean recursive) {
        if (recursive) {
            actions.forEach(a -> a.stop(wrapper, performer, target, true));
        }
        super.stop(wrapper, performer, target, recursive);
    }

}
