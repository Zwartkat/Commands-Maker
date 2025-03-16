package fr.zwartkat.commandsmaker;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Completer {

    public static List<String> generatePowersOf(int base, int maxExponent) {
        List<String> powers = new ArrayList<>(maxExponent);
        for (int i = 0; i <= maxExponent; i++) {
            powers.add(String.valueOf((int) Math.pow(base, i)));
        }
        return powers;
    }

    public static List<String> generateRange(int start, int end) {
        List<String> range = new ArrayList<>(end - start + 1);
        for (int i = start; i <= end; i++) {
            range.add(String.valueOf(i));
        }
        return range;
    }

    public static List<String> getOnlinePlayers(){
        List<String> playersNames = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()){
            playersNames.add(player.getName());
        }
        Bukkit.broadcastMessage(playersNames.toString());
        return playersNames;
    }

    public static List<String> getAllPlayers(){
        List<String> playersNames = new ArrayList<>();

        playersNames.addAll(Completer.getOnlinePlayers());
        for(OfflinePlayer player : Bukkit.getOfflinePlayers()){
            playersNames.add(player.getName());
        }
        return playersNames;
    }

    public static List<String> getAllItems(){
        List<String> itemNames = new ArrayList<>();
        for(Material item : Material.values()){
            if(item.isItem()){
                itemNames.add(item.name());
            }
        }
        return itemNames;
    }

    public static List<String> getAllWorlds(){
        List<String> worldNames = new ArrayList<>();

        for(World world : Bukkit.getWorlds()){
            worldNames.add(world.getName());
        }
        return worldNames;
    }
}


