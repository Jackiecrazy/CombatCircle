package jackiecrazy.combatcircle.capability;

import jackiecrazy.combatcircle.move.MovesetManager;
import jackiecrazy.combatcircle.move.MovesetWrapper;
import net.minecraft.world.entity.Entity;

import java.util.*;

public class Moveset implements IMoveset {
    private final Entity tiedTo;
    private final HashMap<Entity, List<MovesetWrapper>> marks = new HashMap<>();
    private MovesetManager manager;

    public Moveset(Entity linked) {
        tiedTo = linked;
    }

    @Override
    public MovesetManager getMovesetManager() {
        return manager;
    }

    @Override
    public void setMovesetManager(MovesetManager mm) {
        manager = mm;
    }

    @Override
    public void mark(Entity en, MovesetWrapper d) {
        marks.putIfAbsent(en, new ArrayList<>());
        marks.get(en).add(d);
        d.start(en, tiedTo);
    }

    @Override
    public void update() {
        marks.forEach((entity, lists) -> lists.forEach(ms -> ms.tick(entity, tiedTo)));
        marks.values().forEach(wrappers -> wrappers.removeIf(wrapper -> !wrapper.executing()));
        marks.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }
}
