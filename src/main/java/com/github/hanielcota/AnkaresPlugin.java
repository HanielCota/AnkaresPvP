package com.github.hanielcota;

import co.aikar.commands.PaperCommandManager;
import com.github.hanielcota.commands.*;
import com.github.hanielcota.listeners.*;
import com.github.hanielcota.managers.GameStartManager;
import com.github.hanielcota.managers.KillManager;
import com.github.hanielcota.managers.TimeManager;
import com.github.hanielcota.scoreboard.ScoreboardDesign;
import com.github.hanielcota.teams.TeamManager;
import com.github.hanielcota.utils.ConfigUtil;
import com.github.hanielcota.utils.inventory.CustomInventoryListener;
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
    private GameStartManager gameStartManager;
    private KillManager killManager;
    private TimeManager timeManager;

    @Override
    public void onEnable() {
        loadTeamManager();
        loadClass();
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
        manager.registerCommand(new StartCommand(this));
        manager.registerCommand(new AbatesCommand(this));
        manager.registerCommand(new TimeCommand(this));
        manager.registerCommand(new StopCommand(this));

        getLogger().info("Registration of commands successfully completed!");
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        pluginManager.registerEvents(new PlayerInteractListener(this), this);
        pluginManager.registerEvents(new GameStartListener(this), this);
        pluginManager.registerEvents(new PlayerDropItemListener(), this);
        pluginManager.registerEvents(new ScoreboardDesign(this, killManager), this);
        pluginManager.registerEvents(new KillListener(killManager), this);
        pluginManager.registerEvents(new GameEndListener(this), this);

        CustomInventoryListener.register(this);
    }

    private void loadClass() {
        gameStartManager = new GameStartManager(teamManager);
        killManager = new KillManager(this);
        timeManager = new TimeManager(this, this);

        new ScoreboardDesign(this, killManager);
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

        teamManager.clearTeamsOnStartup();
    }
}
