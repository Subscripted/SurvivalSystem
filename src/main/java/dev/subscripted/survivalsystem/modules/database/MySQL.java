package dev.subscripted.survivalsystem.modules.database;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MySQL {

    static Connection connection;

    String username = "subscripted";
    String password = "yj$EY8lvbHA";
    String database = "survival";
    String host = "5.253.246.115";
    String port = "3306";

    public CompletableFuture<Connection> getConnection() {
        return CompletableFuture.supplyAsync(() -> {
            if (!isConnected()) {
                connect();
            }
            return connection;
        });
    }

    private boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @SneakyThrows
    private void connect() {
        if (isConnected()) {
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://5.253.246.115:3306/survival?user=subscripted&password=yj$EY8lvbHA");
            Bukkit.getServer().getLogger().severe("MySQL connection established!");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void close() {
        if (isConnected()) {
            connection.close();
        }
    }
}