package jackiecrazy.combatcircle.move;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import jackiecrazy.combatcircle.CombatCircle;
import jackiecrazy.combatcircle.move.action.Action;
import jackiecrazy.combatcircle.utils.JsonAdapters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;

import java.util.HashMap;
import java.util.Map;

public class Moves extends SimpleJsonResourceReloadListener {
    public static Action a;
    public static final HashMap<ResourceLocation, JsonArray> moves = new HashMap<>();

    public Moves() {
        super(JsonAdapters.gson, "combat_circle_moves");
    }

    public static void register(AddReloadListenerEvent event) {
        event.addListener(new Moves());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        moves.clear();
        object.forEach((key, value) -> {
            JsonArray file = value.getAsJsonArray();
            CombatCircle.LOGGER.debug("loading move definition found under {}", key);
            try {
                a=JsonAdapters.gson.fromJson(file, Action[].class)[0];
                moves.put(key, file);
            } catch (Exception e) {
                CombatCircle.LOGGER.error("{} is an invalid moveset, it will not be registered!", key);
                e.printStackTrace();
            }
        });
    }
}
