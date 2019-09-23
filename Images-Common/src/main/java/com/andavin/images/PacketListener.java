package com.andavin.images;

import com.andavin.images.image.CustomImage;
import com.andavin.images.image.CustomImageSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Supplier;

/**
 * @since September 21, 2019
 * @author Andavin
 */
public abstract class PacketListener implements Versioned {

    static Supplier<List<CustomImage>> getImages;

    /**
     * Set a new entity listener to the given player's
     * packet listener.
     *
     * @param player The player to set the listener for.
     * @param listener The listener to set to.
     */
    public abstract void setEntityListener(Player player, EntityListener listener);

    /**
     * Call the given listener for the player.
     *
     * @param player The player that interacted.
     * @param entityId The entity the player interacted with.
     * @param action The action that the player used to interact.
     * @param hand The hand the player used if applicable.
     * @param listener The listener to call.
     */
    public static void call(Player player, int entityId, InteractType action, Hand hand, EntityListener listener) {

        if (entityId < CustomImageSection.DEFAULT_STARTING_ID) {
            return;
        }

        List<CustomImage> images = getImages.get();
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (images) {

            for (CustomImage image : images) {

                CustomImageSection section = image.getSection(entityId);
                if (section != null) {
                    listener.click(player, image, section, action, hand);
                    return;
                }
            }
        }
    }

    public enum Hand {
        OFF_HAND, MAIN_HAND
    }

    public enum InteractType {
        LEFT_CLICK, RIGHT_CLICK
    }

    public interface EntityListener {

        /**
         * Accept and interaction with and entity.
         *
         * @param player The player that interacted.
         * @param image The image the player interacted with.
         * @param section The specific section that the player interacted with.
         * @param action The action that the player took when interacted.
         * @param hand The hand the player used to interact with.
         */
        void click(Player player, CustomImage image, CustomImageSection section,
                   InteractType action, Hand hand);
    }
}