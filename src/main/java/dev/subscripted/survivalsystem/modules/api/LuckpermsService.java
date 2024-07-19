package dev.subscripted.survivalsystem.modules.api;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class LuckpermsService {

    final LuckPerms api = LuckPermsProvider.get();


    public String getPlayerRang(UUID uuid) {
        User user = api.getUserManager().getUser(uuid);
        if (user.equals(null)) {
            return "User not found";
        }
        ContextSet contexts = api.getContextManager().getContext(user).orElse(null);
        if (contexts == null) {
            return "No context";
        }
        String prefix = user.getCachedData().getMetaData(QueryOptions.contextual(contexts)).getPrefix();
        if (prefix == null) {
            return "No Preifx";
        }
        return prefix;

    }
}
