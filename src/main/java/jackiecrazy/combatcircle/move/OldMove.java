package jackiecrazy.combatcircle.move;

import net.minecraft.util.math.vector.Vector3d;

public class OldMove {
    private int vertAngle;
    private int horAngle;
    private int vertAngleOffset;
    private int horAngleOffset;
    private int fillTime;
    private int emptyTime;
    private int damageTime;
    private int moveWeight = 0;
    private float iradius = 1;
    private float aradius = 1;
    private float healthDamage;//negative damage to use default
    private float postureDamage;//negative damage to use default
    private float healthMultiplier = 1, postureMultiplier = 1;
    private float selfHealing;
    private float interruptHealthThreshold = 9999;
    private float interruptPostureThreshold = 9999;
    private boolean canMove = false;
    private boolean canTurn = false;
    private boolean freeWill = false;
    private boolean indiscriminate = false;
    private boolean difficultyScalingPosture = false;
    private TaskType taskType = TaskType.MELEEATTACK;
    private Vector3d startVec = Vector3d.ZERO, windupVec = Vector3d.ZERO, hitVec = Vector3d.ZERO, endVec = Vector3d.ZERO;//y is up and down, z is forward backward, x is side to side

    public OldMove(int initial, int hurt, int recover) {
        setFillTime(initial).setDamageTime(hurt).setEmptyTime(recover);
    }

    public float getHealthMultiplier() {
        return healthMultiplier;
    }

    public OldMove setHealthMultiplier(float healthMultiplier) {
        this.healthMultiplier = healthMultiplier;
        return this;
    }

    public float getPostureMultiplier() {
        return postureMultiplier;
    }

    public OldMove setPostureMultiplier(float postureMultiplier) {
        this.postureMultiplier = postureMultiplier;
        return this;
    }

    public float getSelfHealing() {
        return selfHealing;
    }

    public OldMove setSelfHealing(float selfHealing) {
        this.selfHealing = selfHealing;
        return this;
    }

    public int getDamageTime() {
        return damageTime;
    }

    public OldMove setDamageTime(int damageTime) {
        this.damageTime = damageTime;
        return this;
    }

    public Vector3d getStartVec() {
        return startVec;
    }

    public OldMove setStartVec(Vector3d startVec) {
        this.startVec = startVec;
        return this;
    }

    public int getVertAngleOffset() {
        return vertAngleOffset;
    }

    OldMove setVertAngleOffset(int vertAngleOffset) {
        this.vertAngleOffset = vertAngleOffset;
        return this;
    }

    public int getHorAngleOffset() {
        return horAngleOffset;
    }

    OldMove setHorAngleOffset(int horAngleOffset) {
        this.horAngleOffset = horAngleOffset;
        return this;
    }
//next attack in a chain

    public int getTotalTime() {
        return fillTime + emptyTime + damageTime;
    }

    public int getVertAngle() {
        return vertAngle;
    }

    OldMove setVertAngle(int vertAngle) {
        this.vertAngle = vertAngle;
        return this;
    }

    public int getHorAngle() {
        return horAngle;
    }

    OldMove setHorAngle(int horAngle) {
        this.horAngle = horAngle;
        return this;
    }

    public int getFillTime() {
        return fillTime;
    }

    OldMove setFillTime(int fillTime) {
        this.fillTime = fillTime;
        return this;
    }

    public int getEmptyTime() {
        return emptyTime;
    }

    OldMove setEmptyTime(int emptyTime) {
        this.emptyTime = emptyTime;
        return this;
    }

    public int getMoveWeight() {
        return moveWeight;
    }//fixme

    OldMove setMoveWeight(int moveWeight) {
        this.moveWeight = moveWeight;
        return this;
    }

    public float getInitiationRadius() {
        return iradius;
    }

    OldMove setInitiationRadius(float radius) {
        this.iradius = radius;
        return this;
    }

    public float getAttackRadius() {
        return aradius;
    }

    OldMove setAttackRadius(float radius) {
        this.aradius = radius;
        return this;
    }

    public float getHealthDamage() {
        return healthDamage;
    }

    OldMove setHealthDamage(float healthDamage) {
        this.healthDamage = healthDamage;
        return this;
    }

    public float getPostureDamage() {
        return postureDamage;
    }

    OldMove setPostureDamage(float postureDamage) {
        this.postureDamage = postureDamage;
        return this;
    }

    public float getInterruptHealthThreshold() {
        return interruptHealthThreshold;
    }

    OldMove setInterruptHealthThreshold(float interruptHealthThreshold) {
        this.interruptHealthThreshold = interruptHealthThreshold;
        return this;
    }

    public float getInterruptPostureThreshold() {
        return interruptPostureThreshold;
    }

    OldMove setInterruptPostureThreshold(float interruptPostureThreshold) {
        this.interruptPostureThreshold = interruptPostureThreshold;
        return this;
    }

    public boolean canMove() {
        return canMove;
    }

    OldMove setCanMove(boolean canMove) {
        this.canMove = canMove;
        return this;
    }

    public boolean canTurn() {
        return canTurn;
    }

    OldMove setCanTurn(boolean canTurn) {
        this.canTurn = canTurn;
        return this;
    }

    public boolean isIndiscriminate() {
        return indiscriminate;
    }

    OldMove setIndiscriminate(boolean indiscriminate) {
        this.indiscriminate = indiscriminate;
        return this;
    }

    public boolean isPostureDamageScaling() {
        return difficultyScalingPosture;
    }

    OldMove setDifficultyScalingPosture(boolean difficultyScalingPosture) {
        this.difficultyScalingPosture = difficultyScalingPosture;
        return this;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    OldMove setTaskType(TaskType taskType) {
        this.taskType = taskType;
        return this;
    }

    public Vector3d getWindupVec() {
        return windupVec;
    }

    OldMove setWindupVec(Vector3d windupVec) {
        this.windupVec = windupVec;
        return this;
    }

    public Vector3d getHitVec() {
        return hitVec;
    }

    OldMove setHitVec(Vector3d hitVec) {
        this.hitVec = hitVec;
        return this;
    }

    public Vector3d getEndVec() {
        return endVec;
    }

    OldMove setEndVec(Vector3d endVec) {
        this.endVec = endVec;
        return this;
    }

    public boolean canEndEarly() {
        return freeWill;
    }

    OldMove setCanEndEarly(boolean freeWill) {
        this.freeWill = freeWill;
        return this;
    }
}
