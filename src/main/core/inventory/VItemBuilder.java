package net.kayega.core.inventory;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class VItemBuilder {
    private final ItemStack item;

    public static VItemBuilder copyOf(ItemStack item) {
        return new VItemBuilder(item.clone());
    }

    public VItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public VItemBuilder(ItemStack item) {
        this.item = Objects.requireNonNull(item, "item");
    }

    public VItemBuilder edit(Consumer<ItemStack> function) {
        function.accept(this.item);
        return this;
    }

    public VItemBuilder meta(Consumer<ItemMeta> metaConsumer) {
        return edit(item -> {
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                metaConsumer.accept(meta);
                item.setItemMeta(meta);
            }
        });
    }

    public <T extends ItemMeta> VItemBuilder meta(Class<T> metaClass, Consumer<T> metaConsumer) {
        return meta(meta -> {
            if (metaClass.isInstance(meta)) {
                metaConsumer.accept(metaClass.cast(meta));
            }
        });
    }

    public VItemBuilder type(Material material) {
        return edit(item -> item.setType(material));
    }

    public VItemBuilder data(int data) {
        return durability((short) data);
    }

    @SuppressWarnings("deprecation")
    public VItemBuilder durability(short durability) {
        return edit(item -> item.setDurability(durability));
    }

    public VItemBuilder amount(int amount) {
        return edit(item -> item.setAmount(amount));
    }

    public VItemBuilder enchant(Enchantment enchantment) {
        return enchant(enchantment, 1);
    }

    public VItemBuilder enchant(Enchantment enchantment, int level) {
        return meta(meta -> meta.addEnchant(enchantment, level, true));
    }

    public VItemBuilder removeEnchant(Enchantment enchantment) {
        return meta(meta -> meta.removeEnchant(enchantment));
    }

    public VItemBuilder removeEnchants() {
        return meta(m -> m.getEnchants().keySet().forEach(m::removeEnchant));
    }

    public VItemBuilder name(String name) {
        return meta(meta -> meta.setDisplayName(name));
    }

    public VItemBuilder lore(String lore) {
        return lore(Collections.singletonList(lore));
    }

    public VItemBuilder lore(String... lore) {
        return lore(Arrays.asList(lore));
    }

    public VItemBuilder lore(List<String> lore) {
        return meta(meta -> meta.setLore(lore));
    }

    public VItemBuilder addLore(String line) {
        return meta(meta -> {
            List<String> lore = meta.getLore();

            if (lore == null) {
                meta.setLore(Collections.singletonList(line));
                return;
            }

            lore.add(line);
            meta.setLore(lore);
        });
    }

    public VItemBuilder addLore(String... lines) {
        return addLore(Arrays.asList(lines));
    }

    public VItemBuilder addLore(List<String> lines) {
        return meta(meta -> {
            List<String> lore = meta.getLore();

            if (lore == null) {
                meta.setLore(lines);
                return;
            }

            lore.addAll(lines);
            meta.setLore(lore);
        });
    }

    public VItemBuilder flags(ItemFlag... flags) {
        return meta(meta -> meta.addItemFlags(flags));
    }

    public VItemBuilder flags() {
        return flags(ItemFlag.values());
    }

    public VItemBuilder removeFlags(ItemFlag... flags) {
        return meta(meta -> meta.removeItemFlags(flags));
    }

    public VItemBuilder removeFlags() {
        return removeFlags(ItemFlag.values());
    }

    public VItemBuilder armorColor(Color color) {
        return meta(LeatherArmorMeta.class, meta -> meta.setColor(color));
    }

    public ItemStack build() {
        return this.item;
    }
}
