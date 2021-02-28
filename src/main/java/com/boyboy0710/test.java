package com.boyboy0710;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.internal.annotation.Selection;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionOwner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.block.Action;
import org.bukkit.util.BlockVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class test extends JavaPlugin implements Listener {


    @Override
    public void onEnable() {
         wep = getWorldEdit();

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

    WorldEditPlugin wep;

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public WorldEditPlugin getWorldEdit() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (p instanceof WorldEditPlugin) return (WorldEditPlugin) p;
        else return null;
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

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("/test")) {
            Region region = null;
            BukkitPlayer bplayer = BukkitAdapter.adapt(p);
            try {
                region = wep.getSession(p).getSelection(bplayer.getWorld());
            } catch (IncompleteRegionException e) {
                e.printStackTrace();
            }

            if(region == null) {
                p.sendMessage("구역지정을 해주십시오");
                return false;
            }

            BlockVector3 max = region.getMaximumPoint();
            BlockVector3 min = region.getMinimumPoint();

            p.sendMessage("구역확인 "+"첫번째 위치:" + max.toString() + ", 두변째위치"+ min.toString());
        }
        return false;
    }

}
