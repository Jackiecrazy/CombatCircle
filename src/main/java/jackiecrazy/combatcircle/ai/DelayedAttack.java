package jackiecrazy.combatcircle.ai;

import net.minecraft.util.math.vector.Vector3d;

public class DelayedAttack {
    private int vertAngle;
    private int horAngle;
    private int vertAngleOffset;
    private int horAngleOffset;
    private int fillTime;
    private int emptyTime;
    private int damageTime;
    private int moveWeight;
    private float radius;
    private float healthDamage;
    private float postureDamage;//negative damage to heal self
    private float interruptHealthThreshold;
    private float interruptPostureThreshold;
    private boolean canMove, canTurn, freeWill, indiscriminate, difficultyScalingHealth, difficultyScalingPosture;
    private TaskType taskType;
    private Vector3d startVec, windupVec, hitVec, endVec;//y is up and down, z is forward backward, x is side to side

    public int getDamageTime() {
        return damageTime;
    }

    public DelayedAttack setDamageTime(int damageTime) {
        this.damageTime = damageTime;
        return this;
    }

    public Vector3d getStartVec() {
        return startVec;
    }

    public DelayedAttack setStartVec(Vector3d startVec) {
        this.startVec = startVec;
        return this;
    }

    public int getVertAngleOffset() {
        return vertAngleOffset;
    }

    DelayedAttack setVertAngleOffset(int vertAngleOffset) {
        this.vertAngleOffset = vertAngleOffset;
        return this;
    }

    public int getHorAngleOffset() {
        return horAngleOffset;
    }

    DelayedAttack setHorAngleOffset(int horAngleOffset) {
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

    DelayedAttack setVertAngle(int vertAngle) {
        this.vertAngle = vertAngle;
        return this;
    }

    public int getHorAngle() {
        return horAngle;
    }

    DelayedAttack setHorAngle(int horAngle) {
        this.horAngle = horAngle;
        return this;
    }

    public int getFillTime() {
        return fillTime;
    }

    DelayedAttack setFillTime(int fillTime) {
        this.fillTime = fillTime;
        return this;
    }

    public int getEmptyTime() {
        return emptyTime;
    }

    DelayedAttack setEmptyTime(int emptyTime) {
        this.emptyTime = emptyTime;
        return this;
    }

    public int getMoveWeight() {
        return moveWeight;
    }

    DelayedAttack setMoveWeight(int moveWeight) {
        this.moveWeight = moveWeight;
        return this;
    }

    public float getRadius() {
        return radius;
    }

    DelayedAttack setRadius(float radius) {
        this.radius = radius;
        return this;
    }

    public float getHealthDamage() {
        return healthDamage;
    }

    DelayedAttack setHealthDamage(float healthDamage) {
        this.healthDamage = healthDamage;
        return this;
    }

    public float getPostureDamage() {
        return postureDamage;
    }

    DelayedAttack setPostureDamage(float postureDamage) {
        this.postureDamage = postureDamage;
        return this;
    }

    public float getInterruptHealthThreshold() {
        return interruptHealthThreshold;
    }

    DelayedAttack setInterruptHealthThreshold(float interruptHealthThreshold) {
        this.interruptHealthThreshold = interruptHealthThreshold;
        return this;
    }

    public float getInterruptPostureThreshold() {
        return interruptPostureThreshold;
    }

    DelayedAttack setInterruptPostureThreshold(float interruptPostureThreshold) {
        this.interruptPostureThreshold = interruptPostureThreshold;
        return this;
    }

    public boolean canMove() {
        return canMove;
    }

    DelayedAttack setCanMove(boolean canMove) {
        this.canMove = canMove;
        return this;
    }

    public boolean canTurn() {
        return canTurn;
    }

    DelayedAttack setCanTurn(boolean canTurn) {
        this.canTurn = canTurn;
        return this;
    }

    public boolean isIndiscriminate() {
        return indiscriminate;
    }

    DelayedAttack setIndiscriminate(boolean indiscriminate) {
        this.indiscriminate = indiscriminate;
        return this;
    }

    public boolean isHealthDamageScaling() {
        return difficultyScalingHealth;
    }

    DelayedAttack setDifficultyScalingHealth(boolean difficultyScalingHealth) {
        this.difficultyScalingHealth = difficultyScalingHealth;
        return this;
    }

    public boolean isPostureDamageScaling() {
        return difficultyScalingPosture;
    }

    DelayedAttack setDifficultyScalingPosture(boolean difficultyScalingPosture) {
        this.difficultyScalingPosture = difficultyScalingPosture;
        return this;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    DelayedAttack setTaskType(TaskType taskType) {
        this.taskType = taskType;
        return this;
    }

    public Vector3d getWindupVec() {
        return windupVec;
    }

    DelayedAttack setWindupVec(Vector3d windupVec) {
        this.windupVec = windupVec;
        return this;
    }

    public Vector3d getHitVec() {
        return hitVec;
    }

    DelayedAttack setHitVec(Vector3d hitVec) {
        this.hitVec = hitVec;
        return this;
    }

    public Vector3d getEndVec() {
        return endVec;
    }

    DelayedAttack setEndVec(Vector3d endVec) {
        this.endVec = endVec;
        return this;
    }

    public boolean canEndEarly() {
        return freeWill;
    }

    DelayedAttack setCanEndEarly(boolean freeWill) {
        this.freeWill = freeWill;
        return this;
    }
}
