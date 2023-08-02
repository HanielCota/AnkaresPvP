package com.github.hanielcota.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.github.hanielcota.AnkaresPlugin;
import com.github.hanielcota.utils.TimeUtils;
import org.bukkit.entity.Player;

@CommandAlias("tempo")
public class TimeCommand extends BaseCommand {
    private final AnkaresPlugin plugin;

    public TimeCommand(AnkaresPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onCommand(Player player) {
        int timeInSeconds = plugin.getTimeManager().getTimeInSeconds();
        String formattedTime = TimeUtils.formatTime(timeInSeconds);

        player.sendMessage("§eO tempo da partida atualmente está em " + formattedTime);
    }
}
