package dev.subscripted.survivalsystem.modules.scoreboard;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScoreboardBuilder {

    private Scoreboard scoreboard;
    private Objective objective;

    public ScoreboardBuilder(String title) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        this.scoreboard = manager.getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("dummy", "dummy", ChatColor.translateAlternateColorCodes('&', title));
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public ScoreboardBuilder addLine(String teamName, String prefix, String suffix, int score) {
        Team team = scoreboard.registerNewTeam(teamName);
        team.addEntry(ChatColor.values()[score].toString());
        team.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix));
        team.setSuffix(ChatColor.translateAlternateColorCodes('&', suffix));
        objective.getScore(ChatColor.values()[score].toString()).setScore(score);
        return this;
    }

    public Scoreboard build() {
        return this.scoreboard;
    }
}
