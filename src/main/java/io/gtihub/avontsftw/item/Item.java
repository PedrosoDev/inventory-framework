package io.gtihub.avontsftw.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import io.gtihub.avontsftw.item.action.ItemAction;
import io.gtihub.avontsftw.item.click.ItemInteract;
import io.gtihub.avontsftw.item.update.ItemUpdater;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

@Getter
public class Item extends ItemStack {

    static Set<Item> items = new HashSet<>();

    ItemAction action = null;
    ItemUpdater updater = null;
    ItemInteract interact = null;

    public Item(ItemStack stack) {
        super(stack);
    }

    public Item(Material material) {
        super(material);
    }

    public Item(Material material, int amount) {
        super(material, amount);
    }

    public Item(Material material, int amount, int id) {
        super(material, amount, (short) id);
    }

    public Item(int readInt) {
        super(readInt);
    }

    public Item setMaterial(Material material) {
        super.setType(material);
        return this;
    }

    public Item setDisplayName(String name) {
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        setItemMeta(meta);
        return this;
    }

    public Item setSkullName(String name) {
        SkullMeta meta = (SkullMeta) getItemMeta();
        meta.setOwner(name);
        setItemMeta(meta);
        return this;
    }

    public Item setSkullURL(String url) {
        SkullMeta itemMeta = (SkullMeta) getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        setItemMeta(itemMeta);
        return this;
    }

    public Item setDurability(int durability) {
        super.setDurability((short) durability);
        return this;
    }

    public Item setQuantity(int value) {
        super.setAmount(value);
        return this;
    }

    public Item setLore(String... lore) {
        return this.setLore(Arrays.asList(lore));
    }

    public Item setLore(List<String> lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(lore);
        setItemMeta(meta);
        return this;
    }

    public Item addItemFlag(ItemFlag... flags) {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(flags);
        setItemMeta(meta);
        return this;
    }

    public Item setAction(ItemAction action) {
        this.action = action;
        items.add(this);
        return this;
    }

    public Item setUpdater(ItemUpdater updater) {
        this.updater = updater;
        items.add(this);
        return this;
    }

    public Item setInteract(ItemInteract interact) {
        this.interact = interact;
        items.add(this);
        return this;
    }

    public static Item convertItem(ItemStack stack) {
        return items.stream().filter(iten -> iten.isSimilar(stack)).findFirst().orElse(null);
    }

    public static boolean contains(ItemStack stack) {
        return items.contains(convertItem(stack));
    }

}
