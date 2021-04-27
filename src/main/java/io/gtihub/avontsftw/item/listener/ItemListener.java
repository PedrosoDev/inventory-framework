package io.gtihub.avontsftw.item.listener;

import io.gtihub.avontsftw.Inventory;
import io.gtihub.avontsftw.item.Item;
import io.gtihub.avontsftw.item.action.ItemAction;
import io.gtihub.avontsftw.item.click.ItemInteract;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemListener implements Listener {

    @EventHandler
    void event(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getItemInHand() == null) return;
        if (player.getItemInHand().getType().equals(Material.AIR)) return;
        if (!player.getItemInHand().hasItemMeta()) return;

        Item item = Item.convertItem(player.getItemInHand());

        if (item == null) return;

        ItemInteract interact = item.getInteract();

        if (interact != null) {
            if (event.getAction() != Action.PHYSICAL)
                interact.onInteract(event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void event(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof Inventory)) {
            if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR))
                return;

            Item item = Item.convertItem(event.getCurrentItem());

            if (item == null) return;

            ItemAction action = item.getAction();
            if (action != null)
                action.onInteract(event);
        }
    }

}
