package jackiecrazy.combatcircle.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MovesetData implements ICapabilitySerializable<CompoundTag> {
    private static IMoveset OHNO = new DummyMovesetCap();

    public static Capability<IMoveset> CAP = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static IMoveset getCap(Entity le) {
        return le.getCapability(CAP).orElse(OHNO);//.orElseThrow(() -> new IllegalArgumentException("attempted to find a nonexistent capability"));
    }

    protected final IMoveset instance;

    public MovesetData() {
        this(new DummyMovesetCap());
    }

    public MovesetData(IMoveset cap) {
        instance = cap;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CAP.orEmpty(cap, LazyOptional.of(() -> instance));
    }

    @Override
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}
