package net.kayega.core.nbt;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class VNbt<T, Z> {
    ItemStack item;
    NamespacedKey key;
    ItemMeta itemMeta;
    PersistentDataContainer itemContainer;

    public VNbt(ItemStack item, NamespacedKey key) {
        this.item = item;
        this.key = key;
        this.itemMeta = this.item.getItemMeta();
        this.itemContainer = Objects.requireNonNull(this.itemMeta).getPersistentDataContainer();
    }

    public void add(PersistentDataType<T, Z> type, Z value) {
        this.itemContainer.set(key, type, value);
        this.item.setItemMeta(itemMeta);
    }

    public boolean has(PersistentDataType<T, Z> type) {
        return this.itemContainer.has(key, type);
    }

    public Z get(PersistentDataType<T, Z> type) {
        return this.itemContainer.get(key, type);
    }

    public PersistentDataContainer getItemContainer() {
        return this.itemContainer;
    }

    public NamespacedKey getKey() {
        return this.key;
    }
}
