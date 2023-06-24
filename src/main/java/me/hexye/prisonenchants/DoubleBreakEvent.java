package me.hexye.prisonenchants;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

import static net.md_5.bungee.api.ChatColor.translateAlternateColorCodes;

public class DoubleBreakEvent implements Listener {

    private final Plugin plugin;

    public DoubleBreakEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    public String getMessage(String message) {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        return translateAlternateColorCodes('&', config.getString(message));
    }

    @EventHandler()
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
            return;
        }
        if (!event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.DOUBLE_BREAK)) {
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


        if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.DOUBLE_BREAK)) {
            List<Integer> chanches = new ArrayList<Integer>();
            int base_chance = 2;
            int chance = base_chance;
            Map<Enchantment, Integer> enchantments = event.getPlayer().getInventory().getItemInMainHand().getEnchantments();
            for (Enchantment e : enchantments.keySet()) {
                if (e == CustomEnchants.DOUBLE_BREAK) {
                    int level = enchantments.get(e);
                    chance = base_chance * level;
                }
            }
            for (int i = 0; i < chance; i++) {
                chanches.add(1);
            }
            for (int i = 0; i < 100 - chance; i++) {
                chanches.add(0);
            }
            Random rand = new Random();
            int random = chanches.get(rand.nextInt(chanches.size()));
            if (random == 0) {
                return;
            }
            event.setDropItems(false);
            Player player = event.getPlayer();
            Block block = event.getBlock();

            Collection<ItemStack> drops = block.getDrops(player.getInventory().getItemInMainHand());
            Collection<ItemStack> drops_clone = block.getDrops(player.getInventory().getItemInMainHand());
            if (drops.isEmpty()) {
                return;
            }
            player.getInventory().addItem(drops.iterator().next());
            player.getInventory().addItem(drops_clone.iterator().next());
            String doublebreak_success = getMessage("doublebreak-success");
            player.sendMessage(doublebreak_success);
        }
    }
}
