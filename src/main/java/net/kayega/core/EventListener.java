package net.kayega.core;

import net.kayega.core.armorequip.ArmorEquipEvent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EventListener implements Listener {
    VCore plugin;
    public EventListener(VCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void equip(ArmorEquipEvent event){
        if(PluginVersion.match(VCore.getVersionType(), PluginVersion.DEBUG)){
            VCore.sendMessage("ArmorEquipEvent - " + event.getMethod());
            VCore.sendMessage("Type: " + event.getType());
            VCore.sendMessage("New: " + (event.getNewArmorPiece() != null ? event.getNewArmorPiece().getType() : "null"));
            VCore.sendMessage("Old: " + (event.getOldArmorPiece() != null ? event.getOldArmorPiece().getType() : "null"));

            if(event.getOldArmorPiece() != null && event.getOldArmorPiece().getType().equals(Material.DIAMOND_HELMET)){
                event.getPlayer().setGameMode(event.getPlayer().getGameMode().equals(GameMode.ADVENTURE) ? GameMode.SURVIVAL : GameMode.ADVENTURE);
            }
            if(event.getNewArmorPiece() != null && event.getNewArmorPiece().getType().equals(Material.DIAMOND_HELMET)){
                event.getPlayer().setGameMode(event.getPlayer().getGameMode().equals(GameMode.ADVENTURE) ? GameMode.SURVIVAL : GameMode.ADVENTURE);
            }
            VCore.sendMessage("New Gamemode: " + event.getPlayer().getGameMode());
        }
    }
}
