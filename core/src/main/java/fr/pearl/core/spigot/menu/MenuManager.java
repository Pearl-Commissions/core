package fr.pearl.core.spigot.menu;

import fr.pearl.api.spigot.menu.MenuHandler;
import fr.pearl.api.spigot.menu.PearlMenu;
import fr.pearl.api.spigot.menu.PearlMenuManager;

public class MenuManager implements PearlMenuManager {

    @Override
    public PearlMenu createMenu(MenuHandler handler) {
        return new Menu(handler);
    }
}
