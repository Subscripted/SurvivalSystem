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
            "Assi", "NIGGER", "nigger", "Nigger", "Neger", "NEGER", "neger", "Heil Hitler",
            "Arschloch", "arschloch", "Arsch", "arsch", "Bastard", "bastard", "Bummsfehler",
            "bummsfehler", "Ballsack", "ballsack", "Dildo", "dildo", "Fuck You", "fuck You",
            "FuckYou", "fuckYou", "Giganigga", "giganigga", "Geplatzteskondom", "geplatzteskondom",
            "Hure", "hure", "Hurensohn", "Mongolischeraffenzchtverein", "mongolischeraffenzchtverein",
            "nigger", "NIGGER", "Nigger", "Niger", "NIGER", "dreckssau", "für AFD", "Karbonaterol",
            "hurensohn", "Karbonat erol", "karbonat erol", "Leck mich", "leck mich", "Opfer",
            "opfer", "Titten", "titten", "Wixe", "wixe", "Wixer", "wixer", "wixxer", "Wixxer",
            "WIXXER", "sex", "SEX", "Sex", "✡", "☭", "✯", "☮", "Ⓐ", "卐", "卍", "✙", "ᛋᛋ", "ꖦ"
    );

    public ChatFilter(SoundLibrary library) {
        this.library = library;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {


        final Player player = event.getPlayer();

        String messageContent = PlainTextComponentSerializer.plainText().serialize(event.message());

        List<String> triggeredBadWords = BAD_WORDS.stream()
                .filter(badWord -> {
                    Pattern pattern = Pattern.compile("\\b" + Pattern.quote(badWord) + "\\b", Pattern.CASE_INSENSITIVE);
                    return pattern.matcher(messageContent).find();
                })
                .collect(Collectors.toList());

        if (!triggeredBadWords.isEmpty()) {
            event.setCancelled(true);
            player.sendMessage(TextComponent.fromLegacyText("§cDeine letzte Nachricht enthielt dieses blockierte Wort: §e" + triggeredBadWords.get(0) + "§c!"));
            player.sendMessage(TextComponent.fromLegacyText("§cWir werden diese Nachricht direkt an das Server-Team weiterleiten!"));
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

