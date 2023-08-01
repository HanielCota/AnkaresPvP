package com.github.hanielcota;

import co.aikar.commands.PaperCommandManager;
import com.github.hanielcota.commands.TeamCommand;
import com.github.hanielcota.commands.WarpCommand;
import com.github.hanielcota.listeners.PlayerJoinListener;
import com.github.hanielcota.listeners.PlayerQuitListener;
import com.github.hanielcota.teams.TeamManager;
import com.github.hanielcota.utils.ConfigUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

@Getter
public final class AnkaresPlugin extends JavaPlugin {

    private TeamManager teamManager;
    private ConfigUtil locationsConfig;

    @Override
    public void onEnable() {
        loadTeamManager();
        registerCommands();
        registerListeners();
    }


    @Override
    public void onLoad() {
        this.locationsConfig = new ConfigUtil(this, "locations.yml");
    }

    private void registerCommands() {
        PaperCommandManager manager = new PaperCommandManager(this);

        manager.registerCommand(new TeamCommand(this));
        manager.registerCommand(new WarpCommand(this));

        getLogger().info("Commands registered!");
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(teamManager), this);
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
