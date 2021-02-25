package com.boyboy0710;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.block.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class test extends JavaPlugin implements Listener {


    @Override
    public void onEnable() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                List<ArmorStand> stands = new ArrayList<>();
                List<Entity> entities = Bukkit.getWorld("world").getEntities();
                for (Entity entity : entities) {
                    if (entity.getType().equals(EntityType.ARMOR_STAND)) {
                        stands.add((ArmorStand) entity);
                    }
                }
                for (ArmorStand armorStand : stands) {
                    if (armorStand.getScoreboardTags().contains(player.getName())) {
                        armorStand.teleport(armorStand.getLocation().add(armorStand.getLocation().getDirection().multiply(1)));
                    }
                    for (Entity entity : entities) {
                        if (entity.getLocation().distance(armorStand.getLocation()) < 1) {
                            ((org.bukkit.entity.LivingEntity) entity).damage(7);
                        }
                    }
                }
            }
        }, 0, 0);
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
            else if(e.getItem().getType() == Material.DIAMOND_SWORD) {
                World world = e.getPlayer().getWorld();
                ArmorStand stand = world.spawn(p.getLocation(), ArmorStand.class);
                stand.addScoreboardTag(p.getName());
                stand.setItemInHand(p.getInventory().getItemInMainHand());
                p.getInventory().setItemInMainHand(null);
            }
        }
    }

}
