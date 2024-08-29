package dev.subscripted.survivalsystem;

import dev.subscripted.survivalsystem.modules.api.CooldownManager;
import dev.subscripted.survivalsystem.modules.api.LuckpermsService;
import dev.subscripted.survivalsystem.modules.api.cooldownmanager.Events;
import dev.subscripted.survivalsystem.modules.chat.ChatFilter;
import dev.subscripted.survivalsystem.modules.chat.Format;
import dev.subscripted.survivalsystem.modules.chat.WrongSyntax;
import dev.subscripted.survivalsystem.modules.clans.commands.ClanCommand;
import dev.subscripted.survivalsystem.modules.clans.events.ClanMenuInteractions;
import dev.subscripted.survivalsystem.modules.clans.events.EntityDeath;
import dev.subscripted.survivalsystem.modules.clans.events.LevelAbs;
import dev.subscripted.survivalsystem.modules.clans.gui.ClanMenus;
import dev.subscripted.survivalsystem.modules.clans.manager.ClanManager;
import dev.subscripted.survivalsystem.modules.connect.JoinQuit;
import dev.subscripted.survivalsystem.modules.database.MySQL;
import dev.subscripted.survivalsystem.modules.database.connections.Coins;
import dev.subscripted.survivalsystem.modules.death.PlayerDeathService;
import dev.subscripted.survivalsystem.modules.economy.BankCommand;
import dev.subscripted.survivalsystem.modules.economy.BankUIListener;
import dev.subscripted.survivalsystem.modules.economy.MarketCommand;
import dev.subscripted.survivalsystem.modules.economy.MarketListener;
import dev.subscripted.survivalsystem.modules.fly.FlyCommand;
import dev.subscripted.survivalsystem.modules.fly.FlyService;
import dev.subscripted.survivalsystem.modules.gamemode.GamemodeSwitcher;
import dev.subscripted.survivalsystem.modules.give.GiveCommand;
import dev.subscripted.survivalsystem.modules.msg.MsgCommand;
import dev.subscripted.survivalsystem.modules.msg.ReplyCommand;
import dev.subscripted.survivalsystem.modules.playertime.PlayerTimeCommand;
import dev.subscripted.survivalsystem.modules.playertime.PlaytimeListener;
import dev.subscripted.survivalsystem.modules.playertime.PlaytimeManager;
import dev.subscripted.survivalsystem.modules.scoreboard.PlayerScoreboard;
import dev.subscripted.survivalsystem.modules.spawn.SetSpawnCommand;
import dev.subscripted.survivalsystem.modules.tablist.TablistService;
import dev.subscripted.survivalsystem.modules.teleport.TeleportCommand;
import dev.subscripted.survivalsystem.modules.utilcommands.CraftCommand;
import dev.subscripted.survivalsystem.modules.utilcommands.EnderChestCommand;
import dev.subscripted.survivalsystem.modules.utilcommands.SeeInventory;
import dev.subscripted.survivalsystem.modules.vanish.VanishCommand;
import dev.subscripted.survivalsystem.modules.vanish.VanishService;
import dev.subscripted.survivalsystem.utils.BankPaymentSerivce;
import dev.subscripted.survivalsystem.utils.SoundLibrary;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    @Getter
    private static Main instance;

    @Getter
    private TablistService tablistService;
    @Getter
    private LuckpermsService lpservice;
    @Getter
    private FlyService flyService;
    @Getter
    private MySQL mySQL;
    @Getter
    private Coins coins;
    @Getter
    private SoundLibrary library;
    @Getter
    private BankPaymentSerivce bankPaymentService;
    @Getter
    private String prefix = "§8» §8| §x§B§F§A§3§B§A§lN§x§B§0§9§3§B§2§lo§x§A§1§8§3§A§B§lv§x§9§2§7§3§A§3§li§x§8§3§6§2§9§B§lb§x§7§4§5§2§9§4§le§x§6§5§4§2§8§C§ls §8» §r";
    @Getter
    private VanishService vanishService;
    @Getter
    private CooldownManager cooldownManager;
    @Getter
    private ClanManager clanManager;
    @Getter
    PlayerScoreboard playerScoreboard;
    @Getter
    PlaytimeManager playtimeManager;

    @Override
    public void onEnable() {


        getLogger().info("\n" +
                "[]=====================================================[] \n" +
                "    _   __           _ __                   __   \n" +
                "   / | / /___ _   __(_) /_  ___  _____ ____/ /__ \n" +
                "  /  |/ / __ \\ | / / / __ \\/ _ \\/ ___// __  / _ \\\n" +
                " / /|  / /_/ / |/ / / /_/ /  __(__  )/ /_/ /  __/\n" +
                "/_/ |_/\\____/|___/_/_.___/\\___/____(_)__,_/\\___/ \n" +
                "                                                 \n" +
                "[]=====================================================[] \n");

        instance = this;
        saveDefaultConfig();
        playtimeManager = new PlaytimeManager(getDataFolder());
        new PlaytimeListener(this, playtimeManager);
        mySQL = new MySQL();
        coins = new Coins(mySQL);
        library = new SoundLibrary();
        bankPaymentService = new BankPaymentSerivce();
        flyService = new FlyService();
        vanishService = new VanishService();
        lpservice = new LuckpermsService();
        cooldownManager = new CooldownManager(instance);
        clanManager = new ClanManager(mySQL);

        tablistService = new TablistService(instance, clanManager);
        playerScoreboard = new PlayerScoreboard(lpservice, clanManager, playtimeManager);


        for (Player p : getServer().getOnlinePlayers()) {
            playtimeManager.loadPlaytime(p);
        }

        getLogger().info("\n" +
                "[]=====================================================[] \n" +
                "  _____                _ _               _____                                          _     \n" +
                " |  __ \\              (_) |             / ____|                                        | |    \n" +
                " | |__) |___  __ _ ___ _| |_ ___ _ __  | |     ___  _ __ ___  _ __ ___   __ _ _ __   __| |___ \n" +
                " |  _  // _ \\/ _` / __| | __/ _ \\ '__| | |    / _ \\| '_ ` _ \\| '_ ` _ \\ / _` | '_ \\ / _` / __|\n" +
                " | | \\ \\  __/ (_| \\__ \\ | ||  __/ |    | |___| (_) | | | | | | | | | | | (_| | | | | (_| \\__ \\\n" +
                " |_|  \\_\\___|\\__, |___/_|\\__\\___|_|     \\_____\\___/|_| |_| |_|_| |_| |_|\\__,_|_| |_|\\__,_|___/\n" +
                "              __/ |                                                                           \n" +
                "             |___/                                                                            \n" +
                "[]=====================================================[]\n");

        getCommand("ec").setExecutor(new EnderChestCommand(library));
        getCommand("tpa").setExecutor(new TeleportCommand(instance, library));
        getCommand("tp").setExecutor(new TeleportCommand(instance, library));
        getCommand("tpahere").setExecutor(new TeleportCommand(instance, library));
        getCommand("tphere").setExecutor(new TeleportCommand(instance, library));
        getCommand("tpaaccept").setExecutor(new TeleportCommand(instance, library));
        getCommand("tpadeny").setExecutor(new TeleportCommand(instance, library));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(instance));
        getCommand("gm").setExecutor(new GamemodeSwitcher(library));
        getCommand("craft").setExecutor(new CraftCommand(library));
        getCommand("seeinventory").setExecutor(new SeeInventory(library));
        getCommand("tpa").setTabCompleter(new TeleportCommand(instance, library));
        getCommand("tp").setTabCompleter(new TeleportCommand(instance, library));
        getCommand("tpahere").setTabCompleter(new TeleportCommand(instance, library));
        getCommand("tphere").setTabCompleter(new TeleportCommand(instance, library));
        getCommand("tpaaccept").setTabCompleter(new TeleportCommand(instance, library));
        getCommand("tpadeny").setTabCompleter(new TeleportCommand(instance, library));
        getCommand("give").setTabCompleter(new GiveCommand());
        getCommand("give").setExecutor(new GiveCommand());
        getCommand("gm").setTabCompleter(new GamemodeSwitcher(library));
        getCommand("market").setExecutor(new MarketCommand(library));
        getCommand("bank").setExecutor(new BankCommand(coins));
        getCommand("vanish").setExecutor(new VanishCommand());
        getCommand("v").setExecutor(new VanishCommand());
        getCommand("fly").setExecutor(new FlyCommand(flyService, library));
        getCommand("msg").setExecutor(new MsgCommand());
        getCommand("r").setExecutor(new ReplyCommand(instance));
        getCommand("command").setExecutor(new TestCommand(library));
        getCommand("playtime").setExecutor(new PlayerTimeCommand(playtimeManager));
        getCommand("clan").setExecutor(new ClanCommand(clanManager, new ClanMenus(mySQL, clanManager), library));

        getLogger().info("\n" +
                "[]=====================================================[] \n" +
                "  _____                _ _              _      _     _                       \n" +
                " |  __ \\              (_) |            | |    (_)   | |                      \n" +
                " | |__) |___  __ _ ___ _| |_ ___ _ __  | |     _ ___| |_ ___ _ __   ___ _ __ \n" +
                " |  _  // _ \\/ _` / __| | __/ _ \\ '__| | |    | / __| __/ _ \\ '_ \\ / _ \\ '__|\n" +
                " | | \\ \\  __/ (_| \\__ \\ | ||  __/ |    | |____| \\__ \\ ||  __/ | | |  __/ |   \n" +
                " |_|  \\_\\___|\\__, |___/_|\\__\\___|_|    |______|_|___/\\__\\___|_| |_|\\___|_|   \n" +
                "              __/ |                                                          \n" +
                "             |___/                                                           \n" +
                "[]=====================================================[] \n");

        getServer().getPluginManager().registerEvents(new MarketListener(library), instance);
        getServer().getPluginManager().registerEvents(new BankUIListener(bankPaymentService, library, clanManager, new ClanMenus(mySQL, clanManager)), instance);
        getServer().getPluginManager().registerEvents(new Format(clanManager), instance);
        getServer().getPluginManager().registerEvents(new JoinQuit(vanishService, tablistService, lpservice, clanManager, playtimeManager), instance);
        getServer().getPluginManager().registerEvents(new WrongSyntax(library), instance);
        getServer().getPluginManager().registerEvents(new PlayerDeathService(library, coins), instance);
        getServer().getPluginManager().registerEvents(new ChatFilter(library), instance);
        getServer().getPluginManager().registerEvents(new Events(cooldownManager), instance);
        getServer().getPluginManager().registerEvents(new ClanMenuInteractions(clanManager, library, new ClanMenus(mySQL, clanManager)), instance);
        getServer().getPluginManager().registerEvents(new EntityDeath(library), instance);
        getServer().getPluginManager().registerEvents(new LevelAbs(library, clanManager), instance);

    }

    @Override
    public void onDisable() {
        for (Player p : getServer().getOnlinePlayers()) {
            try {
                playtimeManager.savePlaytime(p); // Speichere die Spielzeit
            } catch (Exception e) {
                getLogger().severe("Fehler beim Speichern der Spielzeit für " + p.getName() + ": " + e.getMessage());
            }

            if (!p.isOp()) {
                p.kickPlayer(Main.getInstance().getPrefix() + "§eSurvival Reload\n" +
                        " \n" +
                        "§7Webseite: §eNovibes.de §8| §eNovibes Netzwerk");
            }
        }
        mySQL.close(); // Schließe die Datenbankverbindung
    }
}

