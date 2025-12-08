package hu.jgj52.duels.Handlers;

import com.mojang.brigadier.Message;
import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Types.Arena;
import hu.jgj52.duels.Types.Kit;
import hu.jgj52.duels.Utils.Replacer;
import hu.jgj52.duels.Utils.RuntimeVariables;
import hu.jgj52.duels.Utils.Sound;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hu.jgj52.duels.Duels.plugin;
import static java.util.Collections.min;

public class AcceptDuelHandler {
    public static boolean acceptDuel(hu.jgj52.duels.Types.Team blue, hu.jgj52.duels.Types.Team red, Map<String, Object> duelDetails) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        int z = RuntimeVariables.usedArenas.isEmpty() ? 0 : min(RuntimeVariables.usedArenas) - 1000;
        RuntimeVariables.usedArenas.add(z);
        World arenas = Bukkit.getWorld("arenas");
        //todo: this will not work until i make it possible to make arenas, and define them in kit create (yeah im gonna be lazy and not make a gui for it.)

        Kit kit = new Kit(Integer.parseInt(duelDetails.get("kit").toString()));

        Arena arena = new Arena(kit.getId(), new Location(arenas, 0, 100, z));
        if (arena.place()) {
            String blueTeamName = "team_blue";
            Team blueTeam = scoreboard.getTeam(blueTeamName);
            if (blueTeam == null) blueTeam = scoreboard.registerNewTeam(blueTeamName);
            blueTeam.setPrefix("§9");
            blueTeam.setAllowFriendlyFire(false);
            blueTeam.setCanSeeFriendlyInvisibles(true);
            Location blueTeamLoc = new Location(arenas, arena.getDistance(), 100, z);

            String redTeamName = "team_red";
            Team redTeam = scoreboard.getTeam(redTeamName);
            if (redTeam == null) redTeam = scoreboard.registerNewTeam(redTeamName);
            redTeam.setPrefix("§c");
            redTeam.setAllowFriendlyFire(false);
            redTeam.setCanSeeFriendlyInvisibles(true);
            Location redTeamLoc = new Location(arenas, -arena.getDistance(), 100, z);

            for (Player player : blue.getPlayers()) {
                blueTeam.addEntry(player.getName());
                player.teleport(blueTeamLoc);
            }
            for (Player player : red.getPlayers()) {
                redTeam.addEntry(player.getName());
                player.teleport(redTeamLoc);
            }
            List<Player> allPlayers = new ArrayList<>();
            for (hu.jgj52.duels.Types.Team team : List.of(blue, red)) {
                for (Player player : team.getPlayers()) {
                    player.setScoreboard(scoreboard);
                    RuntimeVariables.isInDuel.put(player, true);
                    player.getInventory().clear();
                    for (PotionEffect effect : player.getActivePotionEffects()) {
                        player.removePotionEffect(effect.getType());
                    }
                    player.setFallDistance(0);
                    player.setMaxHealth(kit.getMaxHealth());
                    player.setHealth(player.getMaxHealth());
                    player.setFoodLevel(20);
                    player.setSaturation(5);
                    player.setExperienceLevelAndProgress(0);
                    player.setFireTicks(0);
                    player.setVelocity(new Vector(0, 0, 0));
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setInvulnerable(true);
                    player.setCollidable(true);
                    player.getInventory().setContents(kit.getContents());
                    allPlayers.add(player);
                }
            }
            Map<String, Object> duelData = new HashMap<>();
            duelData.put("blue", blue);
            duelData.put("red", red);
            duelData.put("start", System.currentTimeMillis());
            duelData.put("kit", kit);
            duelData.put("spectators", duelDetails.get("spectators"));
            duelData.put("rounds", duelDetails.get("rounds"));
            RuntimeVariables.duels.add(duelData);
            new BukkitRunnable() {
                int time = arena.getCooldown();

                @Override
                public void run() {
                    if (time > 0) {
                        for (Player p : allPlayers) {
                            p.sendTitle(Replacer.value(MessageManager.getMessage("duelStart.countback"), String.valueOf(time)), "", 0, 20, 0);
                            Sound.wait(p);
                        }
                        time--;
                        return;
                    }

                    for (Player p : allPlayers) {
                        p.sendTitle(MessageManager.getMessage("duelStart.started"), "", 0, 20, 0);
                        p.setInvulnerable(false);
                        Sound.done(p);
                    }

                    cancel();
                }
            }.runTaskTimer(plugin, 0L, 20L);
        } else return false;

        return true;
    }
}
