package jackiecrazy.combatcircle.move;

import jackiecrazy.combatcircle.CombatCircle;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MovesetManager {
    Entity boundTo;
    /*
    each move has a starting weight and a weight change per execution.
    If the weight change is positive, it is added to the move's weight for future calculations.
    If the weight change is negative, it is added to all other move weights.
     */
    private final List<MovesetWrapper> movesetByWeight = new ArrayList<>();
    private int currentWeightSum = 0;
    private MovesetWrapper currentMove;

    public MovesetManager(Entity applied, MovesetFactory... movesets) {
        boundTo = applied;
        for (MovesetFactory mf : movesets) {
            movesetByWeight.add(mf.generateMoveset());
        }
    }

    public MovesetWrapper selectMove(Entity target, float sizeConstraint) {
        Set<MovesetWrapper> executable = movesetByWeight.stream().filter(a -> a.canRun(boundTo, target) && a.getPower() <= sizeConstraint).collect(Collectors.toSet());
        //update weight sum for calculation
        currentWeightSum = 0;
        executable.forEach(a -> currentWeightSum += a.getCurrentWeight());
        //no valid moves
        if (currentWeightSum == 0) return null;
        int rand = CombatCircle.rand.nextInt(currentWeightSum);
        //grab from the eligible
        for (MovesetWrapper w : executable) {
            currentMove = w;
            rand -= w.getCurrentWeight();
            if (rand <= 0) break;
        }
        if (currentMove == null) return null;
        //negative weight change adds to all other weights
        if (currentMove.getChangePer() < 0) {
            movesetByWeight.forEach((ms) -> {
                if (ms != currentMove) {
                    ms.changeCurrentWeight(currentMove.getChangePer());
                }
            });
        }
        //positive weight change adds to current weight
        if (currentMove.getChangePer() > 0) {
            currentMove.changeCurrentWeight(currentMove.getChangePer());
        }
        return currentMove;
    }

    public MovesetWrapper getCurrentMove() {
        return currentMove;
    }
}
