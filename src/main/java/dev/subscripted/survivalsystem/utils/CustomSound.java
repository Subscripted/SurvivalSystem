package dev.subscripted.survivalsystem.utils;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;

@Getter
public enum CustomSound {

    SUCCESSFULL(Sound.ENTITY_PLAYER_LEVELUP),
    NOT_ALLOWED(Sound.ENTITY_VILLAGER_NO),
    NO_PERMISSION(Sound.BLOCK_ANVIL_PLACE),
    ACTIVATED(Sound.BLOCK_BEACON_ACTIVATE),
    DEACTIVATED(Sound.BLOCK_BEACON_DEACTIVATE),
    GUI_SOUND(Sound.UI_BUTTON_CLICK),
    WRONG_USAGE(Sound.UI_TOAST_IN),
    QUESTION(Sound.ENTITY_VILLAGER_TRADE),
    GLASS_GUI_BUILD(Sound.BLOCK_GLASS_PLACE),
    LOADING_FINISHED(Sound.BLOCK_LAVA_EXTINGUISH),
    LOBBY_HIDER_SWITCH(Sound.ENTITY_ARROW_HIT),
    GUI_OPEN(Sound.BLOCK_CHEST_OPEN);

    private final Sound sound;

    CustomSound(Sound sound) {
        this.sound = sound;
    }
}

