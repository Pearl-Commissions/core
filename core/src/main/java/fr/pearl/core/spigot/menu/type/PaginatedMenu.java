package fr.pearl.core.spigot.menu.type;

import fr.pearl.api.spigot.menu.MenuItem;
import fr.pearl.api.spigot.menu.handler.PaginatedMenuHandler;
import fr.pearl.api.spigot.menu.page.PearlPage;
import fr.pearl.api.spigot.menu.type.PaginatedPearlMenu;
import fr.pearl.core.spigot.menu.page.MenuPage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PaginatedMenu implements PaginatedPearlMenu {


    private final PaginatedMenuHandler handler;
    private final List<PearlPage> pageList = new ArrayList<>();
    private final int[] pattern;
    private int pages;

    public PaginatedMenu(PaginatedMenuHandler handler) {
        this.handler = handler;
        this.pattern = handler.itemsPattern();
        this.refresh();
    }

    @Override
    public PearlPage open(Player player) {
        return this.open(player, 1);
    }

    @Override
    public PearlPage open(Player player, int page) {
        int index = page - 1;
        if (index >= this.pages || index < 0) return null;
        PearlPage pearlPage = this.pageList.get(index);
        pearlPage.open(player);
        return pearlPage;
    }

    @Override
    public void refresh() {
        this.pageList.clear();
        LinkedList<MenuItem> items = new LinkedList<>(this.handler.allItems());
        int itemsPerPage = this.pattern.length;
        this.pages = (int) Math.ceil((double) items.size() / (double) itemsPerPage);

        for (int pageIndex = 0; pageIndex < pages; pageIndex++) {
            MenuPage page = new MenuPage(this.handler, pageIndex + 1, this);
            int itemSize = Math.min(items.size(), itemsPerPage);
            for (int i = 0; i < itemSize; i++) {
                MenuItem item = items.pollFirst();
                if (item == null) break;
                int slot = this.pattern[i];
                page.setItem(slot, item);
            }
            this.pageList.add(page);
        }
    }

    @Override
    public List<PearlPage> getPageList() {
        return this.pageList;
    }

    @Override
    public int getPageSize() {
        return this.pages;
    }


}
