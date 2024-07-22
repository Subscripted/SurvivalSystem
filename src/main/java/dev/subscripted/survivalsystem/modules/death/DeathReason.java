package dev.subscripted.survivalsystem.modules.death;

public enum DeathReason {
    FALL_DAMAGE("§cist durch einen Sturz gestorben"),
    FIRE("§cist verbrannt"),
    DROWNING("§cist ertrunken"),
    MOB_ATTACK("§cwurde von einem Mob getötet"),
    PLAYER_ATTACK("§cwurde von einem anderen Spieler getötet"),
    SUFFOCATION("§cist erstickt"),
    VOID("§cist in die Leere gefallen"),
    MAGIC("§cwurde von Magie getötet"),
    BURNED_IN_LAVA("§cist in Lava verbrannt"),
    UNKNOWN("§cist gestorben");

    private final String message;

    DeathReason(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
