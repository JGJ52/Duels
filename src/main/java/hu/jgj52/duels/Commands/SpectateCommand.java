package hu.jgj52.duels.Commands;

import hu.jgj52.duels.Managers.PlayerManager;
import hu.jgj52.duels.Types.PlayerD;
import hu.jgj52.duels.Types.Team;
import hu.jgj52.duels.Utils.Replacer;
import hu.jgj52.duels.Utils.RuntimeVariables;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpectateCommand extends Replacer implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player bukkitPlayer)) {
            sender.sendMessage(getMessage("youAreNotAPlayer"));
            return true;
        }
        PlayerD player = PlayerManager.get(bukkitPlayer);

        if (args.length < 1) {
            player.sendMessage(getMessage("noArgs"));
            return true;
        }

        Player bukkitTarget = Bukkit.getPlayer(args[0]);
        if (bukkitTarget == null) {
            player.sendMessage(Replacer.playerName(getMessage("noPlayer"), args[0]));
            return true;
        }
        PlayerD target = PlayerManager.get(bukkitTarget);

        if (player.isInDuel()) {
            player.sendMessage(getMessage("youAreInDuel"));
            return true;
        }

        if (!target.isInDuel()) {
            player.sendMessage(playerName(getMessage("targetIsNotInDuel"), target));
            return true;
        }

        for (Map<String, Object> duel : RuntimeVariables.duels) {
            Team blue = (Team) duel.get("blue");
            Team red = (Team) duel.get("red");

            if (blue.getPlayers().contains(target) || red.getPlayers().contains(target)) {
                boolean enabled = Boolean.parseBoolean(duel.get("spectators").toString());
                if (!enabled) {
                    player.sendMessage(playerName(getMessage("spectatorsNotAllowed"), target));
                    return true;
                }
                break;
            }
        }

        if (player.spectating() != null) {
            player.sendMessage(playerName(getMessage("youAreAlreadySpectating"), player.spectating()));
        }

        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(target.getLocation());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> players = new ArrayList<>();
        for (Player bplayer : PlayerManager.cache.keySet()) {
            PlayerD player = PlayerManager.get(bplayer);
            if (player.isInDuel()) {
                players.add(player.getName());
            }
        }
        return players;
    }
}
