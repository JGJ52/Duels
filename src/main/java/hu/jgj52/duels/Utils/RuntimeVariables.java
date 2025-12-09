package hu.jgj52.duels.Utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuntimeVariables {
    public static Map<Map<Player, Player>, Map<String, Object>> sentDuelRequests = new HashMap<>();
    public static Map<Player, Boolean> isInDuel = new HashMap<>();
    public static List<Integer> usedArenas = new ArrayList<>();
    public static List<Map<String, Object>> duels = new ArrayList<>();
}
