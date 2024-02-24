package cc.datta.twitchdevtool.utils.menus.menubuilder;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.*;

import static cc.datta.twitchdevtool.utils.ColorClass.format;
import static cc.datta.twitchdevtool.utils.ColorClass.formatList;


@SuppressWarnings("ALL")
public class MenuItemBuilder implements Listener {

    private JavaPlugin plugin;
    private String displayName;
    private List<String> lore;
    private Material material;

    public MenuItemBuilder(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public MenuItemBuilder() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public MenuItemBuilder(Material material, String displayName, String... lore) {
        this.displayName = displayName;
        this.lore = Arrays.asList(lore); // Usar Arrays.asList para evitar la creación de una nueva lista
        this.material = material;
    }

    public MenuItemBuilder(Material material, String displayName, ArrayList<String> lore) {
        this.displayName = displayName;
        this.lore = new ArrayList<>(lore);
        this.material = material;
    }

    public MenuItemBuilder(Material material, String displayName, List<String> lore) {
        this.displayName = displayName;
        this.lore = new ArrayList<>(lore);
        this.material = material;
    }

    private void setupItemMeta(ItemStack itemStack, ItemMeta itemMeta) {
        itemMeta.setDisplayName(format(displayName));
        itemMeta.setLore(formatList(lore));
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ITEM_SPECIFICS, ItemFlag.HIDE_DYE);
        itemMeta.setUnbreakable(true);
        itemStack.setItemMeta(itemMeta);
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        setupItemMeta(itemStack, itemMeta);
        return itemStack;
    }

    public ItemStack buildEnchants(int power, Enchantment... enchantments) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        setupItemMeta(itemStack, itemMeta);
        for (Enchantment enchant : enchantments) {
            itemMeta.addEnchant(enchant, power, true);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack buildCustomModelData(int customModelData) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        setupItemMeta(itemStack, itemMeta);
        itemMeta.setCustomModelData(customModelData);
        return itemStack;
    }

    public ItemStack build(int count) {
        ItemStack itemStack = new ItemStack(material, count);
        ItemMeta itemMeta = itemStack.getItemMeta();
        setupItemMeta(itemStack, itemMeta);
        return itemStack;
    }

    public ItemStack buildLeather(Color color) {
        ItemStack itemStack = new ItemStack(material);
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        setupItemMeta(itemStack, itemMeta);
        itemMeta.setColor(color);
        return itemStack;
    }

    public ItemStack buildPlayerHead(String playerName) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        setupItemMeta(itemStack, skullMeta);
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        return itemStack;
    }

    public ItemStack buildFireworkStar(Color fireworkColor) {
        if (fireworkColor == null) {
            throw new IllegalArgumentException("Firework color cannot be null.");
        }

        ItemStack itemStack = new ItemStack(Material.FIREWORK_STAR);
        FireworkEffectMeta itemMeta = (FireworkEffectMeta) itemStack.getItemMeta();
        setupItemMeta(itemStack, itemMeta);

        FireworkEffect.Builder builder = FireworkEffect.builder();
        builder.withColor(fireworkColor);

        FireworkEffect effect = builder.build();
        itemMeta.setEffect(effect);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }


    public ItemStack buildPlayerHeadTexture(String textureUrl) {
        textureUrl = "http://textures.minecraft.net/texture/" + textureUrl;
        ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
        skullMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ITEM_SPECIFICS, ItemFlag.HIDE_DYE);
        setupItemMeta(skullItem, skullMeta);
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        String base64Texture = getBase64FromTextureUrl(textureUrl);
        profile.getProperties().put("textures", new Property("textures", base64Texture));

        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return skullItem;
    }

    private String getBase64FromTextureUrl(String textureUrl) {
        String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + UUID.randomUUID().toString();
        String payload = "{\"textures\":{\"SKIN\":{\"url\":\"" + textureUrl + "\"}}}";

        byte[] encodedPayload = Base64.getEncoder().encode(payload.getBytes());
        return new String(encodedPayload);
    }
}