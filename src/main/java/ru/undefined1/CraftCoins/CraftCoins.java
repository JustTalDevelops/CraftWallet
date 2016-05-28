package ru.undefined1.CraftCoins;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import ru.undefined1.CraftCoins.api.CraftCoinsAPI;
import ru.undefined1.CraftCoins.listeners.CraftCoinsListener;

import java.io.File;

/**
 * ru.undefined1.CraftCoins.CraftCoins developed by undefined1
 * .
 * This project can be modified by another user, but you need paste link to original GitHub or other project page!
 * You can use software API and making addons without links to this project.
 * .
 * Project create date: 23.05.2016
 * Adv4Core and XonarTeam 2016 (c) All rights reserved.
 */

public class CraftCoins extends PluginBase implements Listener {

    private static CraftCoins instance;

    public static CraftCoins getPlugin() {
        return instance;
    }

    private static CraftCoins plugin;
    private static CraftCoinsAPI api;

    private File config;
    public static Config cfg;

    private File MoneyData;
    public static Config money;

    public CraftCoins() {
    }

    // Getting message from configuration
    public String getMessage(String name) {
        return TextFormat.colorize(cfg.getString("Settings.Prefix") + " " + cfg.getString("MESSAGES.COMMANDS." + name));
    }

    // Sending message to console with or without prefix
    public void sendConsoleMessage(String message, Boolean prefix) {
        String prefixs = TextFormat.colorize("&e[&6CraftCoins&e] ");
        if (prefix) {
            getServer().getConsoleSender().sendMessage(prefixs + TextFormat.colorize(message));
        } else {
            getServer().getConsoleSender().sendMessage(TextFormat.colorize(message));
        }
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    public void onEnable() {

        plugin = this;

        // Saving configuration
        this.getDataFolder().mkdirs();
        this.saveDefaultConfig();
        this.saveResource("CoinsData.yml", false);
        this.saveResource("config.yml", false);

        this.config = new File(this.getDataFolder(), "config.yml");
        cfg = new Config(this.config, 2);

        this.MoneyData = new File(this.getDataFolder(), "CoinsData.yml");
        money = new Config(this.MoneyData, 2);

        // Registering listeners
        getServer().getPluginManager().registerEvents(new CraftCoinsListener(this), this);

        // Registering CraftCoinsAPI
        api = new CraftCoinsAPI();
    }

    public static CraftCoins getCraftCoins() {
        return plugin;
    }

    public static CraftCoinsAPI getCraftCoinsAPI() {
        return api;
    }

    // Plugin commands, you can turn it off using Boolean "Enable-Commands: false"
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String cmdLabel, String[] args) {
        if(cfg.getBoolean("Settings.Enable-Commands")) {
            if (cmdLabel.equalsIgnoreCase("coins")) {
                if (args.length == 0) {
                    Player player = (Player) s;
                    s.sendMessage(getMessage("COINS-BALANCE").replaceAll("<player>", s.getName()).replaceAll("#coins", CraftCoinsAPI.getCoinsName()).replaceAll("<coins>", String.valueOf(CraftCoinsAPI.getPlayerCoins(s.getName()))));
                    if (CraftCoinsAPI.isBalanceFrozen(player)) {
                        s.sendMessage(getMessage("BALANCE-IS-FROZEN"));
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (args.length == 1) {
                        s.sendMessage(getMessage("COINS-SET.USE"));
                    } else {
                        if (s.hasPermission("CraftCoins.set")) {
                            if (s instanceof Player) {
                                Player player = (Player) s;
                                if (getServer().getPlayerExact(args[1]) != null) {
                                    if (!CraftCoinsAPI.isBalanceFrozen(player)) {
                                        if (CraftCoinsAPI.getPlayerCoins(player) != 0) {
                                            s.sendMessage(getMessage("COINS-SET.SUCCESSFUL").replaceAll("<player>", args[1]).replaceAll("#coins", CraftCoinsAPI.getCoinsName()).replaceAll("<coins>", String.valueOf(Double.parseDouble(args[2]))));
                                            CraftCoinsAPI.setPlayerCoins(getServer().getPlayerExact(args[1]), Double.parseDouble(args[2]));
                                        } else {
                                            s.sendMessage(getMessage("LOW-BALANCE"));
                                        }
                                    } else {
                                        s.sendMessage(getMessage("BALANCE-IS-FROZEN"));
                                    }
                                } else {
                                    s.sendMessage(getMessage("NOT-ONLINE").replaceAll("<player>", args[1]));
                                }

                            }
                        }
                    }

                } else if (args[0].equalsIgnoreCase("pay")) {
                    if (args.length == 1) {
                        s.sendMessage(getMessage("COINS-PAY.USE"));
                    } else {
                        if (s.hasPermission("CraftCoins.pay")) {
                            if (s instanceof Player) {
                                if (getServer().getPlayerExact(args[1]) != null) {
                                    Player player = (Player) s;
                                    if (!CraftCoinsAPI.isBalanceFrozen(player)) {
                                        if (CraftCoinsAPI.getPlayerCoins(player) != 0) {
                                            s.sendMessage(getMessage("COINS-PAY.SUCCESSFUL").replaceAll("<player>", args[1]).replaceAll("#coins", CraftCoinsAPI.getCoinsName()).replaceAll("<coins>", String.valueOf(CraftCoinsAPI.getPlayerCoins(args[1]) + Double.parseDouble(args[2]))));
                                            CraftCoinsAPI.sendCoinsToPlayer(player, getServer().getPlayerExact(args[1]), Double.parseDouble(args[2]));
                                        } else {
                                            s.sendMessage(getMessage("LOW-BALANCE"));
                                        }
                                    } else {
                                        s.sendMessage(getMessage("BALANCE-IS-FROZEN"));
                                    }
                                } else {
                                    s.sendMessage(getMessage("NOT-ONLINE").replaceAll("<player>", args[1]));
                                }
                            } else {
                                sendConsoleMessage("&cYou can't use this command in console!",false);
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("add")) {
                    if (args.length == 1) {
                        s.sendMessage(getMessage("COINS-ADD.USE"));
                    } else {
                        if (s.hasPermission("CraftCoins.add")) {
                            if (s instanceof Player) {
                                if (getServer().getPlayerExact(args[1]) != null) {
                                    Player player = (Player) s;
                                    if (!CraftCoinsAPI.isBalanceFrozen(player)) {
                                        if (CraftCoinsAPI.getPlayerCoins(player) != 0) {
                                            s.sendMessage(getMessage("COINS-ADD.SUCCESSFUL").replaceAll("<player>", args[1]).replaceAll("#coins", CraftCoinsAPI.getCoinsName()).replaceAll("<coins>", String.valueOf(CraftCoinsAPI.getPlayerCoins(args[1]) + Double.parseDouble(args[2]))));
                                            CraftCoinsAPI.addPlayerCoins(getServer().getPlayerExact(args[1]), Double.parseDouble(args[2]));
                                        } else {
                                            s.sendMessage(getMessage("LOW-BALANCE"));
                                        }
                                    } else {
                                        s.sendMessage(getMessage("BALANCE-IS-FROZEN"));
                                    }
                                } else {
                                    s.sendMessage(getMessage("NOT-ONLINE").replaceAll("<player>", args[1]));
                                }
                            } else {
                                sendConsoleMessage("&cYou can't use this command in console!",false);
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("take")) {

                    if (args.length == 1) {
                        s.sendMessage(getMessage("COINS-TAKE.USE"));
                    } else {
                        if (s.hasPermission("CraftCoins.take")) {
                            if (s instanceof Player) {
                                if (getServer().getPlayerExact(args[1]) != null) {
                                    Player player = (Player) s;
                                    if (!CraftCoinsAPI.isBalanceFrozen(player)) {
                                        if (CraftCoinsAPI.getPlayerCoins(player) != 0) {
                                            s.sendMessage(getMessage("COINS-TAKE.SUCCESSFUL").replaceAll("<player>", args[1]).replaceAll("#coins", CraftCoinsAPI.getCoinsName()).replaceAll("<coins>", String.valueOf(CraftCoinsAPI.getPlayerCoins(args[1]) - Double.parseDouble(args[2]))));
                                            CraftCoinsAPI.takePlayerCoins(getServer().getPlayerExact(args[1]), Double.parseDouble(args[2]));
                                        } else {
                                            s.sendMessage(getMessage("LOW-BALANCE"));
                                        }
                                    } else {
                                        s.sendMessage(getMessage("BALANCE-IS-FROZEN"));
                                    }
                                } else {
                                    s.sendMessage(getMessage("NOT-ONLINE").replaceAll("<player>", args[1]));
                                }
                            } else {
                                sendConsoleMessage("&cYou can't use this command in console!",false);
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (s.hasPermission("CraftCoins.reload")) {
                        CraftCoinsAPI.reloadConfigs();
                    }
                } else {
                    if (args.length == 1) {
                        if (s.hasPermission("CraftCoins.view")) {
                            s.sendMessage(getMessage("COINS-BALANCE-PLAYER").replaceAll("<player>", args[0]).replaceAll("#coins", CraftCoinsAPI.getCoinsName()).replaceAll("<coins>", String.valueOf(CraftCoinsAPI.getPlayerCoins(args[0]))));
                        }
                    }
                }
            }
        } else {
            s.sendMessage(getMessage("DISABLED"));
        }
        return true;
    }


}