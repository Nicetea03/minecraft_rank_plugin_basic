import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RankPlugin extends JavaPlugin implements CommandExecutor {

    private Map<UUID, String> ranks;

    @Override
    public void onEnable() {
        // Ränge initialisieren
        ranks = new HashMap<>();
        // Hier kannst du weitere Initialisierungen vornehmen, z.B. Ränge aus einer Datenbank laden

        // Command registrieren
        getCommand("setrank").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setrank")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Dieser Befehl kann nur von Spielern verwendet werden!");
                return true;
            }

            Player player = (Player) sender;
            if (!player.hasPermission("rank.set")) {
                player.sendMessage(ChatColor.RED + "Du hast keine Berechtigung, um Ränge zu setzen!");
                return true;
            }

            if (args.length != 2) {
                player.sendMessage(ChatColor.RED + "Verwendung: /setrank <Spieler> <Rang>");
                return true;
            }

            String targetPlayerName = args[0];
            Player targetPlayer = getServer().getPlayer(targetPlayerName);
            if (targetPlayer == null) {
                player.sendMessage(ChatColor.RED + "Der Spieler ist nicht online!");
                return true;
            }

            String rank = args[1].toUpperCase();
            if (!isValidRank(rank)) {
                player.sendMessage(ChatColor.RED + "Ungültiger Rang!");
                return true;
            }

            ranks.put(targetPlayer.getUniqueId(), rank);
            player.sendMessage(ChatColor.GREEN + "Der Rang wurde erfolgreich gesetzt!");

            return true;
        }

        return false;
    }

    private boolean isValidRank(String rank) {
        return rank.equals("ADMIN") || rank.equals("MODERATOR") || rank.equals("SUPPORTER")
                || rank.equals("VIP+") || rank.equals("VIP") || rank.equals("SPIELER");
    }

    public String getPlayerRank(Player player) {
        return ranks.getOrDefault(player.getUniqueId(), "SPIELER");
    }
}
