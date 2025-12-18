package hu.jgj52.duels.Utils;

import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Types.PlayerD;

public class Replacer extends MessageManager {
    public static String playerName(String message, PlayerD player) {
        return message.replaceAll("%player_name%", player.getName());
    }
    public static String playerName(String message, String player) {
        return message.replaceAll("%player_name%", player);
    }
    public static String value(String message, String value) {
        return message.replaceAll("%value%", value);
    }
}
