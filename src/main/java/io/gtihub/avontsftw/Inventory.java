package io.gtihub.avontsftw;

import io.gtihub.avontsftw.item.Item;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

@Getter
@Setter
public abstract class Inventory implements InventoryHolder {

    private org.bukkit.inventory.Inventory inventory;

    public HashMap<Integer, Item> contents;

    @NonNull
    public String TITLE;

    public int SIZE, ROWS;
    public int pageNumber = 1, pageIndex = 0, totalPages = 1, maxItems;

    BukkitTask bukkitTask = null;

    public boolean isPaginated;
    public boolean returnable;

    public Inventory lastInventory;

    public Inventory(String title, int rows) {
        this(title, rows, null);
    }

    public Inventory(String title, int rows, Inventory lastInventory) {
        this(title, rows, 0, lastInventory);
    }

    public Inventory(String title, int rows, int maxItems) {
        this(title, rows, maxItems, null);
    }

    public Inventory(String title, int rows, int maxItems, Inventory lastInventory) {
        TITLE = title;
        ROWS = rows;
        SIZE = rows * 9;

        contents = new HashMap<>();

        this.lastInventory = lastInventory;
        returnable = lastInventory != null;

        isPaginated = maxItems > 0;
        this.maxItems = maxItems;
    }

    public void clear() {
        if (inventory != null)
            inventory.clear();
        if (!contents.isEmpty())
            contents.clear();
    }

    public void setItem(int slot, Item content) {
        contents.put(slot, content);
    }

    public void clear(int... slots) {
        for (int slot : slots) {
            contents.remove(slot);
            inventory.getItem(slot).setType(Material.AIR);
        }
    }

    public void addBackToPage() {
        addBackToPage(getROWS() * 9 - 1);
    }

    public void addBackToPage(int slot) {
        setItem(slot, new Item(Material.INK_SACK, 1, 1).setDisplayName("§cVoltar para página anterior").setAction(event -> {
            Player player = (Player) event.getWhoClicked();

            getContents().values().forEach(content -> {
                if (content.getUpdater() != null)
                    content.getUpdater().cancel();
            });

            Inventory.this.lastInventory.initialize(player);
            playSound(player, SoundType.CHANGE_MENU);
        }));
    }

    public void open(Player player) {
        inventory = Bukkit.createInventory(this, SIZE, TITLE);

        contents.forEach((slot, content) -> {
            inventory.setItem(slot, content);
        });

        player.openInventory(inventory);
    }

    public abstract void initialize(Player player);

    public Inventory instance() {
        return this;
    }

    public void playSound(Player player, SoundType type) {
        if (type.equals(SoundType.CHANGE_MENU)) {
            player.playSound(player.getLocation(), Sound.CLICK, 2f, 2f);
        } else if (type.equals(SoundType.CLICK_MENU)) {
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 2f, 2f);
        }
    }

    public void updateTitle(Player player) {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(ep.activeContainer.windowId, "minecraft:chest", new ChatMessage(TITLE), player.getOpenInventory().getTopInventory().getSize());
        ep.playerConnection.sendPacket(packet);
        ep.updateInventory(ep.activeContainer);
    }

    public enum SoundType {
        CHANGE_MENU, CLICK_MENU
    }
}
