package hu.jgj52.duels.Commands;

import hu.jgj52.duels.Handlers.AcceptDuelHandler;
import hu.jgj52.duels.Types.PlayerD;
import hu.jgj52.duels.Utils.Replacer;
import hu.jgj52.duels.Utils.RuntimeVariables;
import hu.jgj52.duels.Types.Team;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class AcceptDuelCommand extends Replacer implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player bukkitPlayer)) {
            sender.sendMessage(getMessage("youAreNotAPlayer"));
            return true;
        }
        PlayerD player = new PlayerD(bukkitPlayer);

        if (args.length < 1) {
            player.sendMessage(getMessage("noArgs"));
            return true;
        }

        Player bukkitEnemy = Bukkit.getPlayer(args[0]);
        if (bukkitEnemy == null) {
            player.sendMessage(playerName(getMessage("noPlayer"), args[0]));
            return true;
        }
        PlayerD enemy = new PlayerD(bukkitEnemy);

        if (player.isInDuel()) {
            player.sendMessage(getMessage("youAreInDuel"));
            return true;
        }

        if (enemy.isInDuel()) {
            player.sendMessage(playerName(getMessage("enemyIsInDuel"), player));
            return true;
        }

        if (RuntimeVariables.sentDuelRequests.get(Map.of(enemy, player)) == null) {
            player.sendMessage(playerName(getMessage("noDuelRequest"), enemy));
            return true;
        }

        Map<String, Object> duelDetails = RuntimeVariables.sentDuelRequests.get(Map.of(enemy, player));
        RuntimeVariables.sentDuelRequests.remove(Map.of(enemy, player));

        return AcceptDuelHandler.acceptDuel(new Team(List.of(enemy)), new Team(List.of(player)), duelDetails);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return null;
        }
        return List.of();
    }
}
