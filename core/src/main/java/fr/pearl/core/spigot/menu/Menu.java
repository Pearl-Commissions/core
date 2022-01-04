package fr.pearl.core.spigot.menu;

import fr.pearl.api.spigot.menu.MenuItem;
import fr.pearl.api.spigot.menu.MenuHandler;
import fr.pearl.api.spigot.menu.PearlMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Menu implements InventoryHolder, PearlMenu {

    private final Inventory inventory;
    private final Map<Integer, MenuItem> itemMap = new HashMap<>();
    private final MenuHandler menu;

    public Menu(MenuHandler menu) {
        InventoryType type = menu.inventoryType();
        if (type == InventoryType.CHEST) {
            this.inventory = Bukkit.createInventory(this, menu.lines() * 6, menu.title());
        } else {
            this.inventory = Bukkit.createInventory(this, type, menu.title());
        }

        this.menu = menu;
        this.refreshSlots();
    }

    @Override
    public void refreshSlots() {
        this.inventory.clear();
        for (Map.Entry<Integer, MenuItem> entry : menu.itemMap().entrySet()) {
            int slot = entry.getKey();
            MenuItem item = entry.getValue();
            this.inventory.setItem(slot, item.buildItem());
            this.itemMap.put(slot, item);
        }
    }

    @Override
    public void clear() {
        this.inventory.clear();
        this.itemMap.clear();
    }

    public void open(Player player) {
        player.openInventory(this.getInventory());
        player.updateInventory();
        this.menu.onOpen(this, player);
    }

    @Override
    public void setItem(int slot, MenuItem item) {
        itemMap.put(slot, item);
        this.inventory.setItem(slot, item.buildItem());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public Map<Integer, MenuItem> getItemMap() {
        return itemMap;
    }
}
