package hu.jgj52.duels.Utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class RuntimeVariables {
    public static Map<Player, Player> duelRequests = new HashMap<>();
    public static Map<Map<Player, Player>, Map<String, Object>> sentDuelRequests = new HashMap<>();
    public static Map<Player, Boolean> isInDuel = new HashMap<>();
}
