package dev.subscripted.survivalsystem.modules.chat;

import dev.subscripted.survivalsystem.utils.CustomSound;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatFilter implements Listener {
    final SoundLibrary library;

    final List<String> BAD_WORDS = Arrays.asList(
            "Assi", "nigger", "Neger", "arschloch", "Arsch", "Bastard", "bummsfehler",
            "Ballsack", "dildo", "fuckYou", "giganigga", "geplatzteskondom", "Hure",
            "hurensohn", "mongolischeraffenzchtverein", "Niger", "dreckssau", "für AFD",
            "Karbonaterol", "leck mich", "Opfer", "Titten", "wixe", "Wixer", "sex",
            "✡", "☭", "✯", "☮", "Ⓐ", "卐", "卍", "✙", "ᛋᛋ", "ꖦ"
    );

    public ChatFilter(SoundLibrary library) {
        this.library = library;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String messageContent = PlainTextComponentSerializer.plainText().serialize(event.message());

        List<String> triggeredBadWords = BAD_WORDS.stream()
                .filter(badWord -> Pattern.compile("\\b" + Pattern.quote(badWord) +
                        "\\b", Pattern.CASE_INSENSITIVE).matcher(messageContent).find())
                .collect(Collectors.toList());

        if (!triggeredBadWords.isEmpty()) {
            event.setCancelled(true);
            player.sendMessage(TextComponent.fromLegacyText("§cDeine letzte Nachricht enthielt dieses blockierte Wort: §e" + triggeredBadWords.get(0) + "§c!"));
            player.sendMessage(TextComponent.fromLegacyText("§cGanze Nachricht:\n §7" + messageContent));

            String notificationMessage = "§cDer Spieler " + player.getName() + " §chat ein blockiertes Wort verwendet: §e" + triggeredBadWords.get(0) + "§c. Nachricht: §7" + messageContent;

            Bukkit.getOnlinePlayers().stream()
                    .filter(onlinePlayer -> onlinePlayer.hasPermission("survival.chatfilter.see"))
                    .forEach(onlinePlayer -> {
                        onlinePlayer.sendMessage(TextComponent.fromLegacyText(notificationMessage));
                        library.playLibrarySound(onlinePlayer, CustomSound.WARNING, 1f, 2f);
                    });
        }
    }
}
