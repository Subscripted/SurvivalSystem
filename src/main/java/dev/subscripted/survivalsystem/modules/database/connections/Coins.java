package dev.subscripted.survivalsystem.modules.database.connections;

import dev.subscripted.survivalsystem.modules.database.MySQL;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Coins {

    final MySQL mySQL;

    public Coins(MySQL mySQL) {
        this.mySQL = mySQL;
    }

    @SneakyThrows
    public int getCoins(UUID uuid) {
        String query = "SELECT coins FROM player_coins WHERE uuid = ?";
        try (PreparedStatement ps = mySQL.getConnection().get().prepareStatement(query)) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("coins");
            }
        } catch (SQLException e) {
            Bukkit.getServer().getLogger().severe("Error fetching coins for UUID: " + uuid);
            e.printStackTrace();
        }
        return 0;
    }

    @SneakyThrows
    public void setCoins(UUID uuid, int coins) {
        String query = "UPDATE player_coins SET coins = ? WHERE uuid = ?";
        try (PreparedStatement ps = mySQL.getConnection().get().prepareStatement(query)) {
            ps.setInt(1, coins);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getServer().getLogger().severe("Error setting coins for UUID: " + uuid);
            e.printStackTrace();
        }
    }

    public void addCoins(UUID uuid, int amount) {
        int currentCoins = getCoins(uuid);
        setCoins(uuid, currentCoins + amount);
    }

    public void removeCoins(UUID uuid, int amount) {
        int currentCoins = getCoins(uuid);
        setCoins(uuid, Math.max(currentCoins - amount, 0));
    }

    @SneakyThrows
    public void depositToBank(UUID uuid, int amount) {
        int currentBankCoins = getBankCoins(uuid);
        setBankCoins(uuid, currentBankCoins + amount);
    }

    @SneakyThrows
    public void withdrawFromBank(UUID uuid, int amount) {
        int currentBankCoins = getBankCoins(uuid);
        setBankCoins(uuid, Math.max(currentBankCoins - amount, 0));
    }

    @SneakyThrows
    public int getBankCoins(UUID uuid) {
        String query = "SELECT bankcoins FROM player_coins WHERE uuid = ?";
        try (PreparedStatement ps = mySQL.getConnection().get().prepareStatement(query)) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("bankcoins");
            }
        } catch (SQLException e) {
            Bukkit.getServer().getLogger().severe("Error fetching bank coins for UUID: " + uuid);
            e.printStackTrace();
        }
        return 0;
    }

    @SneakyThrows
    public void setBankCoins(UUID uuid, int coins) {
        String query = "UPDATE player_coins SET bankcoins = ? WHERE uuid = ?";
        try (PreparedStatement ps = mySQL.getConnection().get().prepareStatement(query)) {
            ps.setInt(1, coins);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getServer().getLogger().severe("Error setting bank coins for UUID: " + uuid);
            e.printStackTrace();
        }
    }
}
