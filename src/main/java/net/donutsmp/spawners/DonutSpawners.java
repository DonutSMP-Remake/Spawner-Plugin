package net.donutsmp.spawners;

import net.donutsmp.spawners.commands.GiveSpawnerCommand;
import net.donutsmp.spawners.economy.EconomyHandler;
import net.donutsmp.spawners.listeners.SpawnerListener;
import net.donutsmp.spawners.storage.SpawnerManager;
import net.donutsmp.spawners.tasks.ProductionTask;
import com.earth2me.essentials.Essentials;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class DonutSpawners extends JavaPlugin {

    private static DonutSpawners instance;
    private SpawnerManager spawnerManager;
    private EconomyHandler economyHandler;
    private Essentials essentials;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        if (essentials == null) {
            getLogger().severe("Essentials not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        spawnerManager = new SpawnerManager(this);
        spawnerManager.loadSpawners();

        economyHandler = new EconomyHandler(this, essentials);

        getServer().getPluginManager().registerEvents(new SpawnerListener(this), this);
        getCommand("givespawner").setExecutor(new GiveSpawnerCommand(this));

        getLogger().info("DonutSpawners enabled!");
    }

    @Override
    public void onDisable() {
        if (spawnerManager != null) {
            spawnerManager.saveSpawners();
        }
        getLogger().info("DonutSpawners disabled!");
    }

    public static DonutSpawners getInstance() {
        return instance;
    }

    public SpawnerManager getSpawnerManager() {
        return spawnerManager;
    }

    public EconomyHandler getEconomyHandler() {
        return economyHandler;
    }

    public Essentials getEssentials() {
        return essentials;
    }
}
