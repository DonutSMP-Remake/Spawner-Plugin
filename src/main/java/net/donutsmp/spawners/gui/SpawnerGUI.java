
package net.donutsmp.spawners.gui;

import net.donutsmp.spawners.DonutSpawners;
import net.donutsmp.spawners.holder.SpawnerGUIHolder;
import net.donutsmp.spawners.storage.SpawnerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpawnerGUI {
    private final DonutSpawners plugin;
    private final SpawnerData data;
    private final Inventory inventory;
    private final boolean isStorage;
    private final int page;
    private final int totalPages;

    public SpawnerGUI(DonutSpawners plugin, SpawnerData data, boolean isStorage) {
        this(plugin, data, isStorage, 1);
    }

    public SpawnerGUI(DonutSpawners plugin, SpawnerData data, boolean isStorage, int page) {
        this.plugin = plugin;
        this.data = data;
        this.isStorage = isStorage;
        this.page = page;
        int totalStacks = 0;
        if (isStorage) {
            for (Map.Entry<Material, Long> entry : data.getAccumulatedDrops().entrySet()) {
                totalStacks += (int) Math.ceil(entry.getValue() / 64.0);
            }
        }
        int calcPages = isStorage ? (int) Math.ceil(totalStacks / 45.0) : 1;
        this.totalPages = calcPages == 0 ? 1 : calcPages;

        String titleKey = isStorage ? "messages.gui_title_storage" : "messages.gui_title";
        String titleFormat = plugin.getConfig().getString(titleKey, isStorage ? "&8%stack% %type% (%page%/%pages%)" : "&8%stack% %type% Spawner");

        String title = ChatColor.translateAlternateColorCodes('&', titleFormat
                .replace("%stack%", String.valueOf(data.getStackSize()))
                .replace("%type%", capitalize(data.getType().name()))
                .replace("%page%", String.valueOf(page))
                .replace("%pages%", String.valueOf(this.totalPages)));

        this.inventory = Bukkit.createInventory(new SpawnerGUIHolder(data, isStorage), isStorage ? 54 : 27, title);
        setupGUI();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    private void setupGUI() {
        if (isStorage) {
            setupStorage();
        } else {
            setupMain();
        }
    }
    private void setupMain() {
        ItemStack skull = new ItemStack(data.getType().getHeadMaterial());
        ItemMeta skullMeta = skull.getItemMeta();
        skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6" + data.getStackSize() + " " + data.getType().getDisplayName().toLowerCase() + " Spawner"));
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.gui_click_to_sell", "&7• &eClick to sell items and XP")));
        skullMeta.setLore(lore);
        skull.setItemMeta(skullMeta);
        inventory.setItem(13, skull);

        ItemStack storageItem = new ItemStack(Material.CHEST);
        ItemMeta storageMeta = storageItem.getItemMeta();
        storageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.gui_open_storage", "&bOpen Storage")));
        storageItem.setItemMeta(storageMeta);
        inventory.setItem(11, storageItem);

        ItemStack xpBottle = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta xpMeta = xpBottle.getItemMeta();
        xpMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.gui_collect_xp", "&bCollect XP")));
        xpBottle.setItemMeta(xpMeta);
        inventory.setItem(15, xpBottle);
    }


    private void setupStorage() {
        NamespacedKey pageKey = new NamespacedKey(plugin, "gui_page");
        NamespacedKey targetPageKey = new NamespacedKey(plugin, "gui_target_page");

        ItemStack redGlass = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta redMeta = redGlass.getItemMeta();
        redMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.gui_close", "&cClose")));
        redMeta.getPersistentDataContainer().set(pageKey, PersistentDataType.INTEGER, page);
        redGlass.setItemMeta(redMeta);
        inventory.setItem(45, redGlass);

        if (page > 1) {
            ItemStack arrow = new ItemStack(Material.ARROW);
            ItemMeta arrowMeta = arrow.getItemMeta();
            arrowMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.gui_previous_page", "&ePrevious Page")));
            arrowMeta.getPersistentDataContainer().set(targetPageKey, PersistentDataType.INTEGER, page - 1);
            arrow.setItemMeta(arrowMeta);
            inventory.setItem(47, arrow);
        }

        ItemStack emerald = new ItemStack(Material.EMERALD);
        ItemMeta emeraldMeta = emerald.getItemMeta();
        emeraldMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.gui_sell_items", "&aSell Items")));
        emerald.setItemMeta(emeraldMeta);
        inventory.setItem(48, emerald);

        ItemStack dropper = new ItemStack(Material.DROPPER);
        ItemMeta dropperMeta = dropper.getItemMeta();
        dropperMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.gui_drop_all", "&bDrop All")));
        dropper.setItemMeta(dropperMeta);
        inventory.setItem(49, dropper);

        ItemStack goldIngot = new ItemStack(Material.GOLD_INGOT);
        ItemMeta goldMeta = goldIngot.getItemMeta();
        goldMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.gui_sell_xp", "&6Sell XP")));
        goldIngot.setItemMeta(goldMeta);
        inventory.setItem(50, goldIngot);

        if (page < totalPages) {
            ItemStack arrow2 = new ItemStack(Material.ARROW);
            ItemMeta arrow2Meta = arrow2.getItemMeta();
            arrow2Meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.gui_next_page", "&eNext Page")));
            arrow2Meta.getPersistentDataContainer().set(targetPageKey, PersistentDataType.INTEGER, page + 1);
            arrow2.setItemMeta(arrow2Meta);
            inventory.setItem(51, arrow2);
        }

        int slot = 0;
        int startIndex = (page - 1) * 45;
        int endIndex = startIndex + 45;
        int index = 0;
        for (Map.Entry<Material, Long> entry : data.getAccumulatedDrops().entrySet()) {
            Material mat = entry.getKey();
            long totalAmount = entry.getValue();
            long remaining = totalAmount;
            while (remaining > 0 && index < endIndex) {
                if (index >= startIndex) {
                    if (slot >= 45) break;
                    int stackSize = (int) Math.min(remaining, 64);
                    ItemStack item = new ItemStack(mat, stackSize);
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e" + capitalize(mat.name()) + " x" + stackSize));
                    item.setItemMeta(itemMeta);
                    inventory.setItem(slot, item);
                    slot++;
                }
                remaining -= 64;
                index++;
            }
            if (slot >= 45) break;
        }
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }
}
