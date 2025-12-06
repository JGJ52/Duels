package hu.jgj52.duels.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class AcceptDuelHandler {
    public static boolean acceptDuel(hu.jgj52.duels.Utils.Team blue, hu.jgj52.duels.Utils.Team red) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        String blueTeamName = "team_blue";
        Team blueTeam = scoreboard.getTeam(blueTeamName);
        if (blueTeam == null) blueTeam = scoreboard.registerNewTeam(blueTeamName);
        blueTeam.setPrefix("§9");

        String redTeamName = "team_red";
        Team redTeam = scoreboard.getTeam(redTeamName);
        if (redTeam == null) redTeam = scoreboard.registerNewTeam(redTeamName);
        redTeam.setPrefix("§c");

        for (Player player : blue.getPlayers()) {
            blueTeam.addEntry(player.getName());
        }
        for (Player player : red.getPlayers()) {
            redTeam.addEntry(player.getName());
        }
        for (hu.jgj52.duels.Utils.Team team : List.of(blue, red)) {
            for (Player player : team.getPlayers()) {
                player.setScoreboard(scoreboard);
            }
        }
        //todo: continue this thing
        return true;
    }
}
