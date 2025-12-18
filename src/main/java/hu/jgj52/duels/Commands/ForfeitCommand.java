package hu.jgj52.duels.Commands;

import hu.jgj52.duels.Handlers.DuelEndHandler;
import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Managers.PlayerManager;
import hu.jgj52.duels.Types.PlayerD;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForfeitCommand extends MessageManager implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player bukkitPlayer)) {
            sender.sendMessage(getMessage("youAreNotAPlayer"));
            return true;
        }
        PlayerD player = PlayerManager.get(bukkitPlayer);

        if (player.isInDuel()) return true;

        return DuelEndHandler.duelEnd(player);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
