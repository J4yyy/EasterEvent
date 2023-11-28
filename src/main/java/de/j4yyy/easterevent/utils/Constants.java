package de.j4yyy.easterevent.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {

    private static final String[] urls = {"4455ad45aecfeea92c25a2ab253d5fc2101dbea1e5cc1e38c153b7fb962d", "69f973e78559e168149dc043626323043388b9335a7d5852c6bd14bc6ee4191", "bff88a25c4b114e82abb5ce98ccd5b2b186ecd68fd8abf85270673a3e8b90a9", "c3746aec25887480eed17330c43eabcc549c2bb8ce4f15ba3c64a52b07166ef", "924869799efd3e3e267feb45f12c94aba12bad31956cf2e42f035d7e9b4c", "b353c794ff681c8b356a4d1f6d74d2a51c1a4fec517678279f2e3f2ae3b09ff7"};

    public static final ArrayList<Integer> shopFrameNineByThree = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 17, 25, 24, 23, 22, 21, 20, 19, 9));
    public static final ArrayList<Integer> shopFrameNineByThreeHome = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 17, 25, 24, 23, 22, 21, 20, 19, 18, 9));
    public static final ArrayList<Integer> shopSlotsNineByThree = new ArrayList<>(Arrays.asList(10, 11, 12, 13, 14, 15, 16));

    public static ArrayList<ItemStack> eggs() {
        ArrayList<ItemStack> heads = new ArrayList<>();
        for(String url : urls) {
            ItemStack stack = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/"+url);
            heads.add(stack);
        }
        return heads;
    }

    public static ArrayList<ItemStack> tierOneRewards() {
        String[] ids = {"e89d65ac7586a27c0c5d226fd63cb989bee7c62a4e80dd1f1aea2e22c1611cdf", "7be7545297dfd6266bbaa2051825e8879cbfa42c7e7e24e50796f27ca6a18", "6c1db26d0ce23579841b0e725bdc54baa05c482a3de0c0f199583253958eabfc", "e9332f43f6546c745d66de8ca47062dbf352e9c073d733808ec311022caa167e"};

        ArrayList<ItemStack> rewards = new ArrayList<>();

        for(String s : ids) {
            ItemStack stack = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/"+s);
            ItemMeta meta = stack.getItemMeta();
            meta.displayName(Component.text(ChatColor.GOLD + "Osterei"));
            stack.setItemMeta(meta);
            rewards.add(stack);
        }

        return rewards;
    }

    public static ArrayList<ItemStack> tierTwoRewards() {
        return new ArrayList<>(List.of(
                new ItemBuilder(Material.RABBIT_SPAWN_EGG).setLore("", "&3Osterevent &a2023", "&7Preis: &95 Eier").build(),
                new ItemBuilder(Material.GLOW_SQUID_SPAWN_EGG).setLore("", "&3Osterevent &a2023", "&7Preis: &95 Eier").build(),
                new ItemBuilder(Material.OCELOT_SPAWN_EGG).setLore("", "&3Osterevent &a2023", "&7Preis: &95 Eier").build(),
                new ItemBuilder(Material.FOX_SPAWN_EGG).setLore("", "&3Osterevent &a2023", "&7Preis: &95 Eier").build(),
                new ItemBuilder(Material.PARROT_SPAWN_EGG).setLore("", "&3Osterevent &a2023", "&7Preis: &95 Eier").build()
                ));
    }

    public static ArrayList<ItemStack> tierThreeRewards() {
        return new ArrayList<>(List.of(
                new ItemBuilder(Material.AXOLOTL_SPAWN_EGG).setLore("", "&3Osterevent &a2023", "&7Preis: &915 Eier").build(),
                new ItemBuilder(Material.PANDA_SPAWN_EGG).setLore("", "&3Osterevent &a2023", "&7Preis: &915 Eier").build(),
                new ItemBuilder(Material.MAGMA_CUBE_SPAWN_EGG).setLore("", "&3Osterevent &a2023", "&7Preis: &915 Eier").build()
        ));
    }

    public static ArrayList<ItemStack> tierFourRewards() {
        return new ArrayList<>(List.of(
                new ItemBuilder(Material.SLIME_SPAWN_EGG).setLore("", "&3Osterevent &a2023", "&7Preis: &925 Eier").build()
        ));
    }
}


// TODO: Remove Title Message
// TODO: Particle on not found Eggs
// TODO: Reset Easter Event