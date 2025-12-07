package hu.jgj52.duels.Types;

import com.sk89q.worldguard.protection.flags.StateFlag;
import hu.jgj52.duels.Managers.ArenaManager;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Map;
import java.util.UUID;

public class Region {
    private final World world;
    private final Location loc1;
    private final Location loc2;
    private final Map<StateFlag, StateFlag.State> flags;

    public Region (World world, Location loc1, Location loc2, Map<StateFlag, StateFlag.State> flags) {
        this.world = world;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.flags = flags;
    }

    public World getWorld() {
        return world;
    }

    public Location getFirstLocation() {
        return loc1;
    }

    public Location getSecondLocation() {
        return loc2;
    }

    public Map<StateFlag, StateFlag.State> getFlags() {
        return flags;
    }

    public boolean place() {
        return new ArenaManager().createRegion(world, UUID.randomUUID().toString(), loc1, loc2, flags);
    }
}
