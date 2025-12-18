package hu.jgj52.duels.Utils;

import hu.jgj52.duels.Types.PlayerD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuntimeVariables {
    public static Map<Map<PlayerD, PlayerD>, Map<String, Object>> sentDuelRequests = new HashMap<>();
    public static List<Integer> usedArenas = new ArrayList<>();
    public static List<Map<String, Object>> duels = new ArrayList<>();
}
