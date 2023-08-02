package com.github.hanielcota;

import co.aikar.commands.PaperCommandManager;
import com.github.hanielcota.commands.AbatesCommand;
import com.github.hanielcota.commands.StartCommand;
import com.github.hanielcota.commands.TeamCommand;
import com.github.hanielcota.commands.WarpCommand;
import com.github.hanielcota.listeners.*;
import com.github.hanielcota.managers.GameStartManager;
import com.github.hanielcota.managers.KillManager;
import com.github.hanielcota.scoreboard.ScoreboardDesing;
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

        getLogger().info("Registration of commands successfully completed!");
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        pluginManager.registerEvents(new PlayerInteractListener(this), this);
        pluginManager.registerEvents(new GameStartListener(this), this);
        pluginManager.registerEvents(new PlayerDropItemListener(), this);
        pluginManager.registerEvents(new ScoreboardDesing(this, killManager), this);
        pluginManager.registerEvents(new KillListener(killManager), this);

        CustomInventoryListener.register(this);
    }

    private void loadClass() {
        gameStartManager = new GameStartManager(teamManager);
        killManager = new KillManager(this);

        new ScoreboardDesing(this, killManager);
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
