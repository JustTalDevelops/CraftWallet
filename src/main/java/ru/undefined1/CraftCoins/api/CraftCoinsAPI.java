package ru.undefined1.CraftCoins.api;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import ru.undefined1.CraftCoins.CraftCoins;
import ru.undefined1.CraftCoins.events.*;

/**
 * ru.undefined1.CraftCoins.CraftCoinsAPI developed by undefined1
 * .
 * This project can be modified by another user, but you need paste link to original GitHub or other project page!
 * You can use software API and making addons without links to this project.
 * .
 * Project create date: 23.05.2016
 * Adv4Core and XonarTeam 2016 (c) All rights reserved.
 */
public class CraftCoinsAPI {

    private static CraftCoins main = CraftCoins.getPlugin();

    public static void reloadConfigs() {
        main.money.save();
        main.cfg.save();
        main.money.reload();
        main.cfg.reload();
        main.sendConsoleMessage("&8Saved and reloaded coins data!", true);
    }

    public static String getCoinsName() {
        if(main.cfg.getString("Settings.Balance.coins-name") != null) {
            return main.cfg.getString("Settings.Balance.coins-name");
        } else {
            return null;
        }
    }

    // Получение баланса игрока

    public static double getPlayerCoins(Player player) {
        if (main.money.getString(player.getName() + ".balance") != null) {
            return main.money.getDouble(player.getName() + ".balance");
        } else {
            main.sendConsoleMessage("&c[getPlayerCoins] &8Player don't have #coins!".replaceAll("#coins", getCoinsName()), true);
            return 0;

        }
    }

    public static double getPlayerCoins(String player) {
        if (main.money.getString(player + ".balance") != null) {
            return main.money.getDouble(player + ".balance");
        } else {
            main.sendConsoleMessage("&c[getPlayerCoins] &8Player don't have #coins!".replaceAll("#coins", getCoinsName()), true);
            return 0;

        }
    }

    // Установка баланса игрока

    public static void setPlayerCoins(Player sender, Player player, double Coins) {
        if (main.money.getString(player.getName() + ".balance") != null) {
            main.money.set(player.getName() + ".balance", Coins);
            main.sendConsoleMessage("&8You set &e" + Coins + " #coins &8to player!".replaceAll("#coins", getCoinsName()), true);
            reloadConfigs();
            main.getServer().getPluginManager().callEvent(new SetCoinsEvent(sender, player, Coins));
        } else {
            main.sendConsoleMessage("&c[setPlayerCoins] &8Player don't have #coins!".replaceAll("#coins", getCoinsName()), true);
        }
    }


    // Отдать деньги другому игроку

    public static void sendCoinsToPlayer(Player sender, Player target, double CoinsToSend) {
        if (main.money.getString(sender.getName() + ".balance") != null) {
            double coins = getPlayerCoins(sender);
            if (coins != 0) {

                if (CoinsToSend > coins) {
                    main.sendConsoleMessage("&c[sendCoinsToPlayer] &8Player don't have too much #coins!".replaceAll("#coins", getCoinsName()), true);
                    } else {
                    if (main.money.getString(target + ".balance") != null) {
                        createPlayerBalance(target);
                    }
                    String coinsToSend = String.valueOf(CoinsToSend).replaceAll("-", "");
                    double send = Double.parseDouble(coinsToSend);
                    takePlayerCoins(sender, sender, send);
                    addPlayerCoins(sender, target, send);
                    main.sendConsoleMessage("&8Sender balance: &e" + coins + " #coins".replaceAll("#coins", getCoinsName()), true);
                    main.sendConsoleMessage("&8Target balance: &e" + getPlayerCoins(target) + " #coins".replaceAll("#coins", getCoinsName()), true);
                    main.getServer().getPluginManager().callEvent(new SendCoinsEvent(sender, target, CoinsToSend));
                    }

            } else {
                main.sendConsoleMessage("&c[sendCoinsToPlayer] &8Player don't have #coins!".replaceAll("#coins", getCoinsName()), true);
            }

        } else {
            main.sendConsoleMessage("&c[sendCoinsToPlayer] &8Player don't have #coins!".replaceAll("#coins", getCoinsName()), true);
        }
    }

    // Забрать определенное количество коинов у игрока

    public static void takePlayerCoins(Player sender, Player player, double CoinsToTake) {
        if (main.money.getString(player.getName() + ".balance") != null) {
            double coins = getPlayerCoins(player);
            if (coins != 0) {
                if (CoinsToTake > coins) {
                    main.sendConsoleMessage("&c[takePlayerCoins] &8Player don't have too much #coins!".replaceAll("#coins", getCoinsName()), true);
                } else {
                    String check = String.valueOf(CoinsToTake).replaceAll("-", "");
                    double toTake = coins - Double.valueOf(check);
                    setPlayerCoins(sender, player, toTake);
                    reloadConfigs();
                    main.getServer().getPluginManager().callEvent(new TakeCoinsEvent(sender, player, CoinsToTake));
                    main.sendConsoleMessage("&8Player balance: &e" + toTake + " #coins".replaceAll("#coins", getCoinsName()), true);
                }
            } else {
                main.sendConsoleMessage("&c[takePlayerCoins] &8Player don't have #coins!".replaceAll("#coins", getCoinsName()), true);
            }
        } else {
            main.sendConsoleMessage("&c[takePlayerCoins] &8Player don't have #coins!".replaceAll("#coins", getCoinsName()), true);
        }
    }

    // Добавить определенное количество коинов игроку

    public static void addPlayerCoins(Player sender, Player player, double CoinsToAdd) {
        if (main.money.getString(player.getName() + ".balance") != null) {
            double coins = getPlayerCoins(player);
            if (coins != 0) {
                double finalCoins = coins + CoinsToAdd;
                main.money.set(player.getName() + ".balance", finalCoins);
                main.sendConsoleMessage("&8You add &e" + CoinsToAdd + " #coins &8to player!".replaceAll("#coins", getCoinsName()), true);
                reloadConfigs();
                main.getServer().getPluginManager().callEvent(new AddCoinsEvent(sender, player, CoinsToAdd));
                double coinsAfter = getPlayerCoins(player);
                main.sendConsoleMessage("&8Player balance: &e" + coinsAfter + " #coins".replaceAll("#coins", getCoinsName()), true);

            } else {
                main.sendConsoleMessage("&c[addPlayerCoins] &8Player don't have #coins!".replaceAll("#coins", getCoinsName()), true);
            }
        } else {
            main.sendConsoleMessage("&c[addPlayerCoins] &8Player don't have #coins!".replaceAll("#coins", getCoinsName()), true);
        }
    }


    // Создание баланса игрока

    public static void createPlayerBalance(Player player) {
        if (main.money.getString(player.getName() + ".balance") != null) {
            if (main.cfg.getString("Settings.Balance.start-balance") != null) {
                main.money.set(player.getName() + ".balance", main.cfg.getDouble("Settings.Balance.start-balance"));
                main.money.set(player.getName() + ".frozen", false);
                reloadConfigs();
                main.getServer().getPluginManager().callEvent(new PlayerBalanceCreatedEvent(player));
            } else {
                main.sendConsoleMessage("&c[createPlayerBalance] &cWarning! &8String &e'Settings.Balance.start-balance'&8 doesn't exists!", true);
            }
        } else {
            main.sendConsoleMessage("&c[createPlayerBalance] &8Player balance already exists!", true);
        }
    }

    // Управление заморозкой баланса игрока

    public static void frozePlayerBalance(Player player) {
        if (main.money.getString(player.getName() + ".balance") != null) {
            main.money.set(player.getName() + ".frozen", true);
            reloadConfigs();
                main.getServer().getPluginManager().callEvent(new PlayerBalanceFrozeEvent(player));
        } else {
            main.sendConsoleMessage("&c[frozePlayerBalance] &8Player balance already exists!", true);
        }
    }

    // Специальный код

    public static int getSpecialCode(Player player) {
        if (main.money.getString(player.getName() + ".code") != null) {
            return main.money.getInt(player.getName() + ".code");
        } else {
            main.sendConsoleMessage("&c[getSpecialCode] &8Player don't have special code!", true);
            return 0;
        }
    }

    public static Boolean ifPlayerHaveSpecialCode(Player player) {
        if (main.money.getString(player.getName() + ".code") != null) {
            return true;
        } else {
            main.sendConsoleMessage("&c[getSpecialCode] &8Player don't have special code!", true);
            return false;
        }
    }

    // Other features

    public static Boolean isBalanceFrozen(Player player) {
        if (main.money.getString(player.getName() + ".balance") != null) {
            if (main.money.getBoolean(player.getName() + ".frozen")) {
                main.sendConsoleMessage("&c[isBalanceFrozen] &8Player balance is frozen!", true);
                return true;
            } else {
                return false;
            }
        } else {
            main.sendConsoleMessage("&c[isBalanceFrozen] &8Player don't have #coins!".replaceAll("#coins", getCoinsName()), true);
            return false;
        }
    }

}
