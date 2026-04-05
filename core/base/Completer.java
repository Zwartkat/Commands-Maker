package fr.zwartkat.commandsmaker.core.base;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class Completer {

    public static List<String> getPlayers(boolean online){
        List<String> list = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            list.add(p.getName());
        }
        if(!online){
            for(OfflinePlayer p : Bukkit.getOfflinePlayers()){
                list.add(p.getName());
            }
        }
        return list;
    }

    public static List<String> getBoolean(){
        return List.of("true","false");
    }

    public static List<String> getAllItems(){
        return Arrays.stream(Material.values())
                .map(Material::name).collect(Collectors.toList());
    }

    public static List<String> generatePowersOf(int base, int maxExponent) {
        List<String> powers = new ArrayList<>(maxExponent);
        for (int i = 0; i <= maxExponent; i++) {
            powers.add(String.valueOf((int) Math.pow(base, i)));
        }
        powers.sort(Comparator.comparingInt(Integer::parseInt));
        return powers;
    }

    public static List<String> generateRange(int start, int end) {
        List<String> range = new ArrayList<>(end - start + 1);
        for (int i = start; i <= end; i++) {
            range.add(String.valueOf(i));
        }
        range.sort(Comparator.comparingInt(Integer::parseInt));
        return range;
    }

    public static List<String> getAllWorlds(){
        List<String> worldNames = new ArrayList<>();

        for(World world : Bukkit.getWorlds()){
            worldNames.add(world.getName());
        }
        return worldNames;
    }
}
