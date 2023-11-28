package de.j4yyy.easterevent.commands.tabcomp;

import de.j4yyy.easterevent.EasterEvent;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EasterCommandTabComp implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1) {
            ArrayList<String> possibilities = new ArrayList<>(Arrays.asList(
                    "builder",
                    "give",
                    "set",
                    "remove",
                    "reset",
                    "show"
            ));
            return possibilities.stream().filter(text -> StringUtils.startsWithIgnoreCase(text, args[0])).collect(Collectors.toList());
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("reset")) {
                ArrayList<String> possibilities = new ArrayList<>(Arrays.asList(
                        "full",
                        "balance"
                ));
                return possibilities.stream().filter(text -> StringUtils.startsWithIgnoreCase(text, args[1])).collect(Collectors.toList());
            } else {
                ArrayList<String> players = new ArrayList<>();
                for(Player p : Bukkit.getOnlinePlayers().stream().filter(player -> StringUtils.startsWithIgnoreCase(player.getName(), args[1])).collect(Collectors.toList())) {
                    players.add(p.getName());
                }

                for(@NotNull OfflinePlayer p : Arrays.stream(Bukkit.getOfflinePlayers()).filter(player -> StringUtils.startsWithIgnoreCase(player.getName(), args[1])).collect(Collectors.toList())) {
                    players.add(p.getName());
                }
                return players;
            }
        } else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("remove")) {
                int maxBalance = (Bukkit.getPlayer(args[1]) != null ? EasterEvent.instance.getDbConnection().getPlayerEasterBalance(Objects.requireNonNull(Bukkit.getPlayer(args[1])).getUniqueId()) : EasterEvent.instance.getDbConnection().getPlayerEasterBalance(Bukkit.getOfflinePlayer(args[1]).getUniqueId()));
                return new ArrayList<>(List.of(maxBalance + ""));
            }
            return new ArrayList<>(Arrays.asList("1", "10", "25", "50", "100"));
        }
        return null;
    }
}