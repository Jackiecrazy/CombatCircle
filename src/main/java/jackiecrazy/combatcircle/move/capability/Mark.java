package jackiecrazy.combatcircle.move.capability;

import jackiecrazy.combatcircle.move.MovesetWrapper;
import net.minecraft.world.entity.Entity;

import java.util.*;

public class Mark implements IMark {
    private final Entity tiedTo;
    private final HashMap<Entity, List<MovesetWrapper>> marks = new HashMap<>();

    public Mark(Entity linked) {
        tiedTo = linked;
    }

    @Override
    public void mark(Entity en, MovesetWrapper d) {
        marks.putIfAbsent(en, new ArrayList<>());
        marks.get(en).add(d);
        d.start();
    }

    @Override
    public void update() {
        marks.forEach((entity, lists) -> lists.forEach(ms -> ms.tick(entity, tiedTo)));
        marks.entrySet().removeIf(entry -> entry.getKey().isRemoved() || entry.getValue().isEmpty());
    }
}
