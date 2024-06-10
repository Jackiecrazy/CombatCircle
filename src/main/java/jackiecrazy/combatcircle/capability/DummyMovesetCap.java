package jackiecrazy.combatcircle.capability;

import jackiecrazy.combatcircle.move.MovesetManager;
import jackiecrazy.combatcircle.move.MovesetWrapper;
import net.minecraft.world.entity.Entity;

public class DummyMovesetCap implements IMoveset {


    @Override
    public void setMovesetManager(MovesetManager mm) {

    }

    @Override
    public MovesetManager getMovesetManager() {
        return null;
    }

    @Override
    public void mark(Entity from, MovesetWrapper d) {

    }

    @Override
    public void update() {

    }
}
