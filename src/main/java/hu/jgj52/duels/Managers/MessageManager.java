package hu.jgj52.duels.Managers;

import hu.jgj52.duels.Duels;

public class MessageManager {
    private static final Duels plugin = Duels.plugin;

    public static String getMessage(String path) {
        return plugin.getConfig().getString("languages." + plugin.getConfig().get("language") + "." + path);
    }
}
