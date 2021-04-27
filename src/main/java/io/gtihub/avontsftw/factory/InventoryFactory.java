package io.gtihub.avontsftw.factory;

import io.gtihub.avontsftw.Inventory;
import org.bukkit.entity.Player;

public class InventoryFactory {

    public static Inventory createInventory(String title) {
        return new Inventory(title, 3) {
            @Override
            public void initialize(Player player) {

            }
        };
    }

    public static Inventory createInventory(String title, int rows) {
        return new Inventory(title, rows) {
            @Override
            public void initialize(Player player) {

            }
        };
    }

    public static Inventory createInventory(String title, int rows, Inventory last) {
        return new Inventory(title, rows, last) {
            @Override
            public void initialize(Player player) {

            }
        };
    }

}
