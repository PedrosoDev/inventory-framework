package io.gtihub.avontsftw.listener;

import io.gtihub.avontsftw.Inventory;
import io.gtihub.avontsftw.item.Item;
import io.gtihub.avontsftw.item.action.ItemAction;
import io.gtihub.avontsftw.item.update.ItemUpdater;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    void event(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Inventory) {
            event.setCancelled(true);
            event.setCursor(null);

            if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR))
                return;

            Inventory inventory = (Inventory) event.getInventory().getHolder();
            Item item = inventory.getContents().get(event.getSlot());

            if (item == null) return;

            if (!Item.contains(event.getCurrentItem())) return;

            ItemAction action = item.getAction();
            if (action != null)
                action.onInteract(event);
        }
    }

    @EventHandler
    void event(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Inventory) {
            Inventory inventory = (Inventory) event.getInventory().getHolder();

            inventory.getContents().values().forEach(content -> {
                if (content == null) return;
                ItemUpdater updater = content.getUpdater();
                if (updater != null) {
                    updater.cancel();
                }
            });
        }
    }

}
