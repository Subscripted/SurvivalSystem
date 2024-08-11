package dev.subscripted.survivalsystem;

import dev.subscripted.survivalsystem.utils.CustomSound;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class TestCommand implements CommandExecutor {

    final SoundLibrary library;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player player = (Player) commandSender;
        if (args[0].equals("test")){
            library.playLibrarySound(player, CustomSound.MEMORY, 1f, 0.5f);
            return true;
        }
        if (args[0].equals("test2")){
            library.playLibrarySound(player, CustomSound.MEMORY2, 1f, 2f);
            return true;
        }
        if (args[0].equals("test3")){
            library.playLibrarySound(player, CustomSound.MEMORY3, 1f, 0.5f);
            return true;
        }
        if (args[0].equals("test4")){
            library.playLibrarySound(player, CustomSound.BELL, 1f, 0.5f);
            return true;
        }
        if (args[0].equals("test5")){
            library.playLibrarySound(player, CustomSound.SUCCESSFULL, 1f, 0.5f);
            return true;
        }
        return false;
    }
}
