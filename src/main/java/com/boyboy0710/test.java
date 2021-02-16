package com.boyboy0710;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.block.Action;

import java.util.Objects;

public final class test extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR
                || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(Objects.requireNonNull(e.getItem()).getType() == Material.TNT) {
                Entity entity =
                        p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.PRIMED_TNT);
                entity.setVelocity(p.getLocation().getDirection().multiply(2));
            }
        }
    }

}
