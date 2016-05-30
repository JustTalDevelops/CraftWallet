package ru.undefined1.CraftCoins;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import ru.undefined1.CraftCoins.api.CraftWalletAPI;
import ru.undefined1.CraftCoins.listeners.CraftWalletListener;

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

public class CraftWallet extends PluginBase implements Listener {

    private static CraftWallet instance;

    public static CraftWallet getPlugin() {
        return instance;
    }

    private static CraftWallet plugin;
    private static CraftWalletAPI api;

    private File config;
    public static Config cfg;

    private File MoneyData;
    public static Config money;

    public CraftWallet() {
    }

    // Getting message from configuration
    public String getMessage(String name) {
        return TextFormat.colorize(cfg.getString("Settings.Prefix") + " " + cfg.getString("MESSAGES.COMMANDS." + name));
    }

    // Sending message to console with or without prefix
    public void sendConsoleMessage(String message, Boolean prefix) {
        String prefixs = TextFormat.colorize("&d[&5CraftWallet&d] ");
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
        this.saveResource("data.yml", false);
        this.saveResource("config.yml", false);

        this.config = new File(this.getDataFolder(), "config.yml");
        cfg = new Config(this.config, 2);

        this.MoneyData = new File(this.getDataFolder(), "data.yml");
        money = new Config(this.MoneyData, 2);

        // Registering listeners
        getServer().getPluginManager().registerEvents(new CraftWalletListener(this), this);

        // Registering CraftWalletAPI
        api = new CraftWalletAPI();
    }

    public static CraftWallet getCraftCoins() {
        return plugin;
    }

    public static CraftWalletAPI getCraftCoinsAPI() {
        return api;
    }

    // Plugin commands, you can turn it off using Boolean "Enable-Commands: false"
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String cmdLabel, String[] args) {
        if(cfg.getBoolean("Settings.Enable-Commands")) {
            if (cmdLabel.equalsIgnoreCase("money")) {
                if (args.length == 0) {
                    Player player = (Player) s;
                    s.sendMessage(getMessage("WALLET-BALANCE").replaceAll("<player>", s.getName()).replaceAll("#walletSymbol", CraftWalletAPI.getMoneySymbol()).replaceAll("<count>", String.valueOf(CraftWalletAPI.getPlayerMoney(s.getName()))));
                    if (CraftWalletAPI.isBalanceFrozen(player)) {
                        s.sendMessage(getMessage("BALANCE-IS-FROZEN"));
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (args.length == 1) {
                        s.sendMessage(getMessage("WALLET-SET.USE"));
                    } else {
                        if (s.hasPermission("CraftWallet.set")) {
                            if (s instanceof Player) {
                                Player player = (Player) s;
                                if (getServer().getPlayerExact(args[1]) != null) {
                                    if (!CraftWalletAPI.isBalanceFrozen(player)) {
                                        if (CraftWalletAPI.getPlayerMoney(player) != 0) {
                                                s.sendMessage(getMessage("WALLET-SET.SUCCESSFUL").replaceAll("<player>", args[1]).replaceAll("#walletSymbol", CraftWalletAPI.getMoneySymbol()).replaceAll("<count>", String.valueOf(Double.parseDouble(args[2]))));
                                                CraftWalletAPI.setPlayerMoney(getServer().getPlayerExact(args[1]), Double.parseDouble(args[2]));
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
                        s.sendMessage(getMessage("WALLET-PAY.USE"));
                    } else {
                        if (s.hasPermission("CraftWallet.pay")) {
                            if (s instanceof Player) {
                                if (getServer().getPlayerExact(args[1]) != null) {
                                    Player player = (Player) s;
                                    if (!CraftWalletAPI.isBalanceFrozen(player)) {
                                        if (CraftWalletAPI.getPlayerMoney(player) != 0) {
                                            if(getServer().getPlayerExact(args[1]).getName() != s.getName()) {
                                            s.sendMessage(getMessage("WALLET-PAY.SUCCESSFUL").replaceAll("<player>", args[1]).replaceAll("#walletSymbol", CraftWalletAPI.getMoneySymbol()).replaceAll("<count>", String.valueOf(CraftWalletAPI.getPlayerMoney(args[1]) + Double.parseDouble(args[2]))));
                                            CraftWalletAPI.sendMoneyToPlayer(player, getServer().getPlayerExact(args[1]), Double.parseDouble(args[2]));
                                            } else {
                                                s.sendMessage((getMessage("WALLET-PAY.YOUSELF")));
                                            }
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
                        s.sendMessage(getMessage("WALLET-ADD.USE"));
                    } else {
                        if (s.hasPermission("CraftWallet.add")) {
                            if (s instanceof Player) {
                                if (getServer().getPlayerExact(args[1]) != null) {
                                    Player player = (Player) s;
                                    if (!CraftWalletAPI.isBalanceFrozen(player)) {
                                        if (CraftWalletAPI.getPlayerMoney(player) != 0) {
                                            s.sendMessage(getMessage("WALLET-ADD.SUCCESSFUL").replaceAll("<player>", args[1]).replaceAll("#walletSymbol", CraftWalletAPI.getMoneySymbol()).replaceAll("<count>", String.valueOf(CraftWalletAPI.getPlayerMoney(args[1]) + Double.parseDouble(args[2]))));
                                            CraftWalletAPI.addPlayerMoney(getServer().getPlayerExact(args[1]), Double.parseDouble(args[2]));
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
                        s.sendMessage(getMessage("WALLET-TAKE.USE"));
                    } else {
                        if (s.hasPermission("CraftWallet.take")) {
                            if (s instanceof Player) {
                                if (getServer().getPlayerExact(args[1]) != null) {
                                    Player player = (Player) s;
                                    if (!CraftWalletAPI.isBalanceFrozen(player)) {
                                        if (CraftWalletAPI.getPlayerMoney(player) != 0) {
                                            s.sendMessage(getMessage("WALLET-TAKE.SUCCESSFUL").replaceAll("<player>", args[1]).replaceAll("#walletSymbol", CraftWalletAPI.getMoneySymbol()).replaceAll("<count>", String.valueOf(CraftWalletAPI.getPlayerMoney(args[1]) - Double.parseDouble(args[2]))));
                                            CraftWalletAPI.takePlayerMoney(getServer().getPlayerExact(args[1]), Double.parseDouble(args[2]));
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
                    if (s.hasPermission("CraftWallet.reload")) {
                        s.sendMessage("Configuration reloaded successful!");
                        money.reload();
                        cfg.reload();
                    }
                } else {
                    if (args.length == 1) {
                        if (s.hasPermission("CraftWallet.view")) {
                            s.sendMessage(getMessage("WALLET-BALANCE-PLAYER").replaceAll("<player>", args[0]).replaceAll("#walletSymbol", CraftWalletAPI.getMoneySymbol()).replaceAll("<count>", String.valueOf(CraftWalletAPI.getPlayerMoney(args[0]))));
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