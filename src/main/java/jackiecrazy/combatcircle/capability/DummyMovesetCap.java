package jackiecrazy.combatcircle.capability;

import jackiecrazy.combatcircle.move.MovesetManager;
import jackiecrazy.combatcircle.move.MovesetWrapper;
import net.minecraft.world.entity.Entity;

public class DummyMovesetCap implements IMoveset {
    private static final MovesetManager literallyNothing=new MovesetManager(null);

    @Override
    public void setMovesetManager(MovesetManager mm) {

    }

    @Override
    public MovesetManager getMovesetManager() {
        return literallyNothing;
    }

    @Override
    public void mark(Entity from, MovesetWrapper d) {

    }

    @Override
    public void update() {

    }
}
