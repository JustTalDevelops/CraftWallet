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
public class AddCoinsEvent extends PlayerEvent implements Cancellable {

    Player s;
    Player By;
    double c;
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public AddCoinsEvent(Player by, Player sender, double CoinsToAdd) {
        this.s = sender;
        this.c = CoinsToAdd;
        this.By = by;
    }

    public Player getPlayer() {
        return s;
    }

    public Player getSender() {
        return this.By;
    }

    public double getCoinsAmmount() {
        return c;
    }

}
