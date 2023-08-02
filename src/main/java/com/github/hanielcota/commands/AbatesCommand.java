package com.github.hanielcota.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.github.hanielcota.AnkaresPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("abates")
public class AbatesCommand extends BaseCommand {

    private final AnkaresPlugin plugin;

    public AbatesCommand(AnkaresPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("§eVocê tem atualmente: §f" + plugin.getKillManager().getKills(player));
            return;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            player.sendMessage("§cJogador não encontrado!");
            return;
        }

        player.sendMessage("§eO " + target.getName() + " tem atualmente: §f" + plugin.getKillManager().getKills(target));
    }
}
