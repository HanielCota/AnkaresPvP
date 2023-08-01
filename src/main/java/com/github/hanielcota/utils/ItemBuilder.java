package com.github.hanielcota.utils;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemBuilder {

    private final ItemStack is;

    public ItemBuilder(Material m) {
        is = new ItemStack(m, 1);
    }

    public ItemBuilder(String material) {
        is = new ItemStack(Material.valueOf(material), 1);
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    public ItemBuilder setMaterial(Material material) {
        is.setType(material);
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        is.setDurability(durability);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        is.setAmount(amount);
        return this;
    }

    public ItemBuilder setPotion(PotionEffectType type, int duration, int amplifier) {
        PotionMeta im = (PotionMeta) is.getItemMeta();
        im.addCustomEffect(new PotionEffect(type, duration, amplifier), true);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment ench) {
        is.removeEnchantment(ench);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        if (is.getItemMeta() instanceof SkullMeta im) {
            im.setOwner(owner);
            is.setItemMeta(im);
            is.setDurability((short) 3);
        }
        return this;
    }


    public ItemBuilder addEnchant(Enchantment ench, int level) {
        ItemMeta im = is.getItemMeta();
        im.addEnchant(ench, level, true);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        is.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilder setInfinityDurability() {
        is.setDurability(Short.MAX_VALUE);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag flag) {
        is.getItemMeta().addItemFlags(flag);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(Arrays.asList(lore));
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder replaceLore(String key, String value) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = im.getLore().stream()
                .map(line -> line.replace(key, value))
                .toList();
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLoreIf(boolean condition, String... lore) {
        if (!condition) return this;
        ItemMeta im = is.getItemMeta();
        im.setLore(Arrays.asList(lore));
        is.setItemMeta(im);
        return this;

    }

    public ItemBuilder setLoreIf(boolean condition, List<String> lore) {
        if (!condition) return this;
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder removeLoreLine(String line) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (!lore.contains(line)) return this;
        lore.remove(line);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder removeLoreLine(int index) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (index < 0 || index >= lore.size()) return this;
        lore.remove(index);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreIf(boolean condition, String string) {
        if (!condition) return this;
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore()) lore = new ArrayList<>(im.getLore());
        lore.add(string);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLine(String string) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore()) lore = new ArrayList<>(im.getLore());
        lore.add(string);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLine(int pos, String string) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.set(pos, string);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setDyeColor(DyeColor color) {
        is.getData().setData(color.getDyeData());
        return this;
    }


    public ItemBuilder setLeatherArmorColor(Color color) {
        if (is.getItemMeta() instanceof LeatherArmorMeta im) {
            im.setColor(color);
            is.setItemMeta(im);
        }
        return this;
    }

    public ItemStack build() {
        return is;
    }

    public ItemBuilder removeLastLoreLine() {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (lore.isEmpty()) return this;
        lore.remove(lore.size() - 1);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public static ItemBuilder fromMaterialName(String materialName) {
        return new ItemBuilder(Material.valueOf(materialName));
    }
}