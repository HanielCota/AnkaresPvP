package com.github.hanielcota;

import co.aikar.commands.PaperCommandManager;
import com.github.hanielcota.commands.TeamCommand;
import com.github.hanielcota.listeners.TeamListener;
import com.github.hanielcota.teams.TeamManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

@Getter
public final class AnkaresPlugin extends JavaPlugin {

    private TeamManager teamManager;

    @Override
    public void onEnable() {
        loadTeamManager();
        registerCommands();
        registerListeners();
    }

    private void registerCommands() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new TeamCommand(this));

        getLogger().info("Commands registered!");
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new TeamListener(this), this);
    }

    private void loadTeamManager() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        if (scoreboardManager == null) {
            getLogger().severe("Could not load ScoreboardManager. Disabling the plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Scoreboard mainScoreboard = scoreboardManager.getMainScoreboard();
        teamManager = new TeamManager(mainScoreboard);
    }
}
