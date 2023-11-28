package de.j4yyy.easterevent.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    private ItemMeta itemMeta;
    private ItemStack itemStack;
    public ItemBuilder(Material mat){
        itemStack = new ItemStack(mat);
        itemMeta = itemStack.getItemMeta();
    }
    public ItemBuilder setDisplayname(String s){
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', s));
        return this;
    }
    public ItemBuilder setLocalizedName(String s){
        itemMeta.setLocalizedName(ChatColor.translateAlternateColorCodes('&', s));
        return this;
    }
    public ItemBuilder setLore(String ... lore){
        List<String> lores = new ArrayList<>();
        for(String s : lore) {
            lores.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        itemMeta.setLore(lores);
        return this;
    }
    public ItemBuilder setUnbreakable(boolean s){
        itemMeta.setUnbreakable(s);
        return this;
    }
    public ItemBuilder addItemFlags(ItemFlag... s){
        itemMeta.addItemFlags(s);
        return this;
    }
    public ItemBuilder addEnchantment(Enchantment enchantment) {
        itemStack.addEnchantment(enchantment, enchantment.getMaxLevel());
        return this;
    }
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        itemStack.addEnchantment(enchantment, level);
        return this;
    }
    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    @Override
    public String toString() {
        return "ItemBuilder{" +
                "itemMeta=" + itemMeta +
                ", itemStack=" + itemStack +
                '}';
    }
    public ItemStack build(){
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}