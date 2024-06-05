package jackiecrazy.combatcircle.move.filter;

import jackiecrazy.footwork.move.Move;
import net.minecraft.world.entity.Entity;

import java.util.List;

public abstract class Filter extends Move {
    protected String ID;
    protected int limit;

    public abstract List<Entity> filter(Entity performer, Entity target, List<Entity> targets);

    public String toString() {
        return ID;
    }
}
