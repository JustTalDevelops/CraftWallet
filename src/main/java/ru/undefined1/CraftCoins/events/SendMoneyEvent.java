package ru.undefined1.CraftCoins.events;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

/**
 * ru.undefined1.CraftCoins.events developed by undefined1
 * <p>
 * This project can be modified by another user, but you need paste link to original GitHub or other project page!
 * You can use software API and making addons without links to this project.
 * <p>
 * Project create date: 25.05.2016
 * Adv4Core and XonarTeam 2016 (c) All rights reserved.
 */
public class SendMoneyEvent extends PlayerEvent implements Cancellable {

    Player To;
    Player From;
    double count;
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public SendMoneyEvent(Player sender, Player target, double CoinsToSend) {
        this.To = target;
        this.From = sender;
        this.count = CoinsToSend;
    }

    public Player getPlayer() {
        return To;
    }

    public Player getSender() {
        return From;
    }

    public double getCoinsAmmount() {
        return count;
    }
}
