package com.github.hanielcota;

import co.aikar.commands.PaperCommandManager;
import com.github.hanielcota.commands.TeamCommand;
import com.github.hanielcota.listeners.TeamListener;
import com.github.hanielcota.teams.TeamManager;
import lombok.Getter;
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
        getServer().getPluginManager().registerEvents(new TeamListener(this), this);
    }

    private void loadTeamManager() {
        ScoreboardManager scoreboardManager = getServer().getScoreboardManager();
        Scoreboard mainScoreboard = scoreboardManager.getMainScoreboard();

        teamManager = new TeamManager(mainScoreboard);
    }
}

