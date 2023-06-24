package me.hexye.prisonenchants;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZoneBreakEvent implements Listener {

    List<Location> broken_blocks = new ArrayList<Location>();
    @EventHandler()
    public void onBlockBreak(BlockBreakEvent event) {
        if (broken_blocks.contains(event.getBlock().getLocation())) {
            return;
        }
        if (!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
            return;
        }
        if (!event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.DOUBLE_BREAK) && !(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.ZONE_BREAK))) {
            return;
        }
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE || event.getPlayer().getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        if (event.getPlayer().getInventory().firstEmpty() == -1) {
            return;
        }
        if (event.getBlock().getState() instanceof org.bukkit.block.Container) {
            return;
        }
        if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.ZONE_BREAK)) {
            Map<Enchantment, Integer> enchantments = event.getPlayer().getInventory().getItemInMainHand().getEnchantments();
            int level = 0;
            for (Enchantment e : enchantments.keySet()) {
                if (e == CustomEnchants.ZONE_BREAK) {
                    level = enchantments.get(e);
                }
            }
            Block block = event.getBlock();
            Location loc = block.getLocation();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            int radius = level * 2;
            for (int i = 0; i<radius; i++) {
                Block b = block.getWorld().getBlockAt(x + i, y, z);
                b.breakNaturally(event.getPlayer().getInventory().getItemInMainHand());
                broken_blocks.add(b.getLocation());
            }
            for (int i = 0; i<radius; i++) {
                Block b = block.getWorld().getBlockAt(x + i, y, z);
                b.breakNaturally(event.getPlayer().getInventory().getItemInMainHand());
                broken_blocks.add(b.getLocation());
            }
            for (int i = 0; i<radius; i++) {
                Block b = block.getWorld().getBlockAt(x, y, z+i);
                b.breakNaturally(event.getPlayer().getInventory().getItemInMainHand());
                broken_blocks.add(b.getLocation());
            }
            for (int i = 0; i<radius; i++) {
                Block b = block.getWorld().getBlockAt(x, y, z-i);
                b.breakNaturally(event.getPlayer().getInventory().getItemInMainHand());
                broken_blocks.add(b.getLocation());
            }
            for (int i = 0; i<radius; i++) {
                Block b = block.getWorld().getBlockAt(x, y+i, z);
                b.breakNaturally(event.getPlayer().getInventory().getItemInMainHand());
                broken_blocks.add(b.getLocation());
            }
            for (int i = 0; i<radius; i++) {
                Block b = block.getWorld().getBlockAt(x , y-1, z);
                b.breakNaturally(event.getPlayer().getInventory().getItemInMainHand());
                broken_blocks.add(b.getLocation());
            }
        }
        ItemStack main_item = event.getPlayer().getInventory().getItemInMainHand();
        main_item.setDurability((short) (main_item.getDurability() + 2));
        if (main_item.getDurability() >= main_item.getType().getMaxDurability()) {
            event.getPlayer().getInventory().setItemInMainHand(null);
        }
    }
}
