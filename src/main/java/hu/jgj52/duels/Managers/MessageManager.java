package hu.jgj52.duels.Managers;

import static hu.jgj52.duels.Duels.plugin;

public class MessageManager {
    public static String getMessage(String path) {
        return plugin.getConfig().getString("languages." + plugin.getConfig().get("language") + "." + path);
    }
}
