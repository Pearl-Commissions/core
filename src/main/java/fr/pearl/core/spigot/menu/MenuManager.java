package fr.pearl.core.spigot.menu;

import fr.pearl.api.spigot.menu.handler.SimpleMenuHandler;
import fr.pearl.api.spigot.menu.handler.PaginatedMenuHandler;
import fr.pearl.api.spigot.menu.PearlMenuManager;
import fr.pearl.api.spigot.menu.type.PaginatedPearlMenu;
import fr.pearl.api.spigot.menu.type.SimplePearlMenu;
import fr.pearl.core.spigot.menu.type.PaginatedMenu;
import fr.pearl.core.spigot.menu.type.SimpleMenu;

public class MenuManager implements PearlMenuManager {

    @Override
    public SimplePearlMenu createMenu(SimpleMenuHandler handler) {
        return new SimpleMenu(handler);
    }

    @Override
    public PaginatedPearlMenu createPaginatedMenu(PaginatedMenuHandler handler) {
        return new PaginatedMenu(handler);
    }
}
