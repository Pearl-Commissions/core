package fr.pearl.core.spigot.listener;

import fr.pearl.api.spigot.menu.MenuItem;
import fr.pearl.core.spigot.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class MenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = player.getOpenInventory().getTopInventory();
        if (inventory.getHolder() instanceof Menu menu) {
            event.setCancelled(true);
            MenuItem item = menu.getItemMap().get(event.getSlot());
            if (item == null) return;
            item.click(event, player, event.getClick());
        }
    }
}
