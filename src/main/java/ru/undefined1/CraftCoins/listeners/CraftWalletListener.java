package ru.undefined1.CraftCoins.listeners;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import ru.undefined1.CraftCoins.CraftWallet;
import ru.undefined1.CraftCoins.api.CraftWalletAPI;
import ru.undefined1.CraftCoins.events.SendMoneyEvent;

/**
 * ru.undefined1.CraftCoins.CraftWalletListener developed by undefined1
 * .
 * This project can be modified by another user, but you need paste link to original GitHub or other project page!
 * You can use software API and making addons without links to this project.
 * .
 * Project create date: 23.05.2016
 * Adv4Core and XonarTeam 2016 (c) All rights reserved.
 */
public class CraftWalletListener implements Listener {

    CraftWallet plugin;

    public CraftWalletListener(CraftWallet plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onJoin(PlayerJoinEvent e) {
        if(plugin.cfg.getString("Settings.Balance.create-on-player-join") != null) {
            if(plugin.cfg.getBoolean("Settings.Balance.create-on-player-join")) {
                if(plugin.money.getString(e.getPlayer().getName() + ".balance") == "") {
                    plugin.getCraftCoinsAPI().createPlayerBalance(e.getPlayer());
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onSend(SendMoneyEvent e) {
        if(plugin.cfg.getBoolean("Settings.Enable-Commands")) {
            e.getPlayer().sendMessage((plugin.getMessage("WALLET-PAY.SUCCESSFUL").replaceAll("<player>", e.getSender().getName()).replaceAll("#walletSymbol", CraftWalletAPI.getMoneySymbol()).replaceAll("<count>", String.valueOf(e.getCoinsAmmount()))));
        }
    }

}
