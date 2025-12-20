package net.donutsmp.spawners.economy;

import net.donutsmp.spawners.DonutSpawners;
import com.earth2me.essentials.Essentials;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Map;

public class EconomyHandler {
    private final DonutSpawners plugin;
    private final Essentials essentials;

    public EconomyHandler(DonutSpawners plugin, Essentials essentials) {
        this.plugin = plugin;
        this.essentials = essentials;
    }

    public double getWorth(Material material) {
        return plugin.getConfig().getDouble("prices." + material.name(), 0.0);
    }

    public void addMoney(Player player, double amount) {
        try {
            com.earth2me.essentials.User user = essentials.getUser(player);
            if (user != null) {
                user.setMoney(user.getMoney().add(BigDecimal.valueOf(amount)));
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to add money to " + player.getName() + ": " + e.getMessage());
        }
    }

    public double sellItems(Player player, Map<Material, Long> items) {
        double total = 0;
        for (Map.Entry<Material, Long> entry : items.entrySet()) {
            double worth = getWorth(entry.getKey()) * entry.getValue();
            total += worth;
        }
        addMoney(player, total);
        return total;
    }
}
