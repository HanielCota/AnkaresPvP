package com.github.hanielcota.utils.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class InventoryScheme {

    private final List<String> masks = new ArrayList<>();
    private final Map<Character, ItemStack> items = new HashMap<>();
    private final Map<Character, Consumer<InventoryClickEvent>> handlers = new HashMap<>();

    /**
     * Add a mask to this scheme including all sort of characters.
     * For example: "110101011"
     *
     * @param mask a 9 characters mask
     * @return this scheme instance
     * @throws IllegalArgumentException if the mask length is not 9
     */
    public InventoryScheme mask(String mask) {
        requireNonNull(mask, "mask cannot be null");
        if (mask.length() != 9) {
            throw new IllegalArgumentException("Mask length must be 9");
        }
        this.masks.add(mask);
        return this;
    }

    /**
     * Add multiple masks to this scheme including all sorts of characters.
     * For example: "111111111", "110101011", "111111111"
     *
     * @param masks multiple 9-characters masks
     * @return this scheme instance
     * @throws IllegalArgumentException if any mask length is not 9
     */
    public InventoryScheme masks(String... masks) {
        for (String mask : requireNonNull(masks, "masks cannot be null")) {
            mask(mask);
        }
        return this;
    }

    /**
     * Bind a character to the corresponding item in the inventory.
     *
     * @param character the associated character in the mask
     * @param item      the item to use for this character
     * @param handler   consumer for the item
     * @return this scheme instance
     */
    public InventoryScheme bindItem(char character, ItemStack item, Consumer<InventoryClickEvent> handler) {
        items.put(character, requireNonNull(item, "item cannot be null"));

        if (handler != null) {
            handlers.put(character, handler);
        }
        return this;
    }

    /**
     * Bind a character to the corresponding item in the inventory.
     *
     * @param character the associated character in the mask
     * @param item      the item to use for this character
     * @return this scheme instance
     */
    public InventoryScheme bindItem(char character, ItemStack item) {
        return bindItem(character, item, null);
    }

    /**
     * Unbind any item from this character.
     *
     * @param character the character to unbind
     * @return this scheme instance
     */
    public InventoryScheme unbindItem(char character) {
        items.remove(character);
        handlers.remove(character);
        return this;
    }

    /**
     * Apply the current inventory scheme to the CustomInventory instance.
     *
     * @param inv the CustomInventory instance to apply this scheme to
     */
    public void apply(CustomInventory inv) {
        for (int line = 0; line < masks.size(); line++) {
            String mask = masks.get(line);

            for (int slot = 0; slot < mask.length(); slot++) {
                char c = mask.charAt(slot);
                ItemStack item = items.get(c);
                Consumer<InventoryClickEvent> handler = handlers.get(c);

                if (item != null) {
                    inv.setItem(9 * line + slot, item, handler);
                }
            }
        }
    }
}
