package fr.pearl.core.spigot.menu.page;

import fr.pearl.api.spigot.menu.MenuItem;
import fr.pearl.api.spigot.menu.PearlMenu;
import fr.pearl.api.spigot.menu.handler.PaginatedMenuHandler;
import fr.pearl.api.spigot.menu.page.PearlPage;
import fr.pearl.api.spigot.menu.holder.PageHolder;
import fr.pearl.core.spigot.menu.type.PaginatedMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MenuPage implements PageHolder, PearlPage {

    private final PaginatedMenu menu;
    private final PaginatedMenuHandler handler;
    private final Inventory inventory;
    private final int page;
    private final Map<Integer, MenuItem> itemMap = new HashMap<>();

    public MenuPage(PaginatedMenuHandler handler, int page, PaginatedMenu menu) {
        this.inventory = Bukkit.createInventory(this, 9 * handler.linesPerPage(), String.format(handler.title(), page, menu.getPageSize()));
        this.page = page;
        this.handler = handler;
        this.menu = menu;

        this.refresh();
    }

    @Override
    public void open(Player player) {
        player.openInventory(this.inventory);
        this.handler.onPageOpened(player, this);
        player.updateInventory();
    }

    @Override
    public void setItem(int slot, MenuItem item) {
        this.inventory.setItem(slot, item.buildItem());
        this.itemMap.put(slot, item);
    }

    @Override
    public void refresh() {
        this.handler.itemsPerPage().forEach(this::setItem);
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    public PearlPage getPage() {
        return this;
    }

    @Override
    public int getPageIndex() {
        return this.page;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public Map<Integer, MenuItem> getItemMap() {
        return this.itemMap;
    }

    @Override
    public PearlMenu getMenu() {
        return this.menu;
    }
}
