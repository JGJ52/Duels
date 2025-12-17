package hu.jgj52.duels.Commands;

import hu.jgj52.duels.Handlers.DuelEndHandler;
import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Types.Team;
import hu.jgj52.duels.Utils.RuntimeVariables;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LeaveCommand extends MessageManager implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(getMessage("youAreNotAPlayer"));
            return true;
        }

        if (!RuntimeVariables.isInDuel.getOrDefault(player, false)) return true;

        Team team = new Team(List.of());
        Map<String, Object> data = new HashMap<>();
        Iterator<Map<String, Object>> iterator = RuntimeVariables.duels.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> duel = iterator.next();
            Team blue = (Team) duel.get("blue");
            Team red = (Team) duel.get("red");

            if (blue.getPlayers().contains(player)) {
                team = blue;
                iterator.remove();
                data = duel;
                break;
            }

            if (red.getPlayers().contains(player)) {
                team = red;
                iterator.remove();
                data = duel;
                break;
            }
        }
        team.removePlayer(player);
        RuntimeVariables.duels.add(data);

        return DuelEndHandler.duelEnd(player);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
