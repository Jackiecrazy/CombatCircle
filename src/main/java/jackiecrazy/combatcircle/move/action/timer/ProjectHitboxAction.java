package jackiecrazy.combatcircle.move.action.timer;

import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.move.argument.SelectorArgument;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectHitboxAction extends TimerAction {
    /**
     * 0 for once only, other hitboxes repeat every x ticks.
     */
    private int hit_cooldown;
    private final transient HashMap<Entity, Long> lastHit = new HashMap<>();
    private SelectorArgument selector;
    private List<Action> act = new ArrayList<>();

    @Override
    public int tick(Entity performer, Entity target) {
        for (Entity e : selector.resolve(performer, target)) {
            if (hit_cooldown == 0 && lastHit.containsKey(e)) continue;
            if (lastHit.get(e) < e.level().getGameTime() + hit_cooldown) continue;
            int childRet = runActions(this, act, performer, e);
            if (childRet != 0) return childRet;
            lastHit.put(e, e.level().getGameTime());
        }
        return super.tick(performer, target);
    }

    @Override
    public void reset() {
        lastHit.clear();
        for (Action child : act)
            child.reset();
        super.reset();
    }
}
