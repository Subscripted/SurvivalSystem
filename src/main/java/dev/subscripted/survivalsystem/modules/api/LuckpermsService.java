package dev.subscripted.survivalsystem.modules.api;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.entity.Player;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class LuckpermsService {

    final LuckPerms api = LuckPermsProvider.get();

    public String getPlayerRang(UUID uuid) {
        User user = api.getUserManager().getUser(uuid);

        if (user == null) {
            return "User not found";
        }

        ContextSet contexts = api.getContextManager().getContext(user).orElse(null);

        if (contexts == null) {
            return "No context";
        }

        String prefix = user.getCachedData().getMetaData(QueryOptions.contextual(contexts)).getPrefix();

        if (prefix == null) {
            return "No Prefix";
        }

        return prefix;
    }

    public void setDefaultGroup(Player player) {
        User user = api.getUserManager().getUser(player.getUniqueId());

        if (user != null) {
            user.setPrimaryGroup("default");
            api.getUserManager().saveUser(user);
        }
    }

    public boolean hasDefaultGroup(Player player) {
        User user = api.getUserManager().getUser(player.getUniqueId());

        return user != null && "default".equals(user.getPrimaryGroup());
    }
}