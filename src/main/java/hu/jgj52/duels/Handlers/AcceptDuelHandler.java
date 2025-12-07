package hu.jgj52.duels.Handlers;

import hu.jgj52.duels.Types.Arena;
import hu.jgj52.duels.Utils.RuntimeVariables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Map;

import static java.util.Collections.min;

public class AcceptDuelHandler {
    public static boolean acceptDuel(hu.jgj52.duels.Types.Team blue, hu.jgj52.duels.Types.Team red, Map<String, Object> duelDetails) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        int z = RuntimeVariables.usedArenas.isEmpty() ? 0 : min(RuntimeVariables.usedArenas) - 1000;
        RuntimeVariables.usedArenas.add(z);
        World arenas = Bukkit.getWorld("arenas");

        Arena arena = new Arena(duelDetails.get("kit").toString(), new Location(arenas, 0, 100, z));
        if (arena.place()) {
            String blueTeamName = "team_blue";
            Team blueTeam = scoreboard.getTeam(blueTeamName);
            if (blueTeam == null) blueTeam = scoreboard.registerNewTeam(blueTeamName);
            blueTeam.setPrefix("§9");
            Location blueTeamLoc = new Location(arenas, arena.getDistance(), 100, z);

            String redTeamName = "team_red";
            Team redTeam = scoreboard.getTeam(redTeamName);
            if (redTeam == null) redTeam = scoreboard.registerNewTeam(redTeamName);
            redTeam.setPrefix("§c");
            Location redTeamLoc = new Location(arenas, -arena.getDistance(), 100, z);

            for (Player player : blue.getPlayers()) {
                blueTeam.addEntry(player.getName());
                player.teleport(blueTeamLoc);
            }
            for (Player player : red.getPlayers()) {
                redTeam.addEntry(player.getName());
                player.teleport(redTeamLoc);
            }
            for (hu.jgj52.duels.Types.Team team : List.of(blue, red)) {
                for (Player player : team.getPlayers()) {
                    player.setScoreboard(scoreboard);
                    RuntimeVariables.isInDuel.put(player, true);
                }
            }
        } else return false;


        return true;
    }
}
