package net.kayega.core.armorequip;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseArmorEvent;

public class DispenserArmorListener {
    @EventHandler
    public void dispenseArmorEvent(BlockDispenseArmorEvent event){
        ArmorType type = ArmorType.matchType(event.getItem());
        if(type != null){
            if(event.getTargetEntity() instanceof Player){
                Player p = (Player) event.getTargetEntity();
                ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.DISPENSER, type, null, event.getItem());
                Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                if(armorEquipEvent.isCancelled()){
                    event.setCancelled(true);
                }
            }
        }
    }
}
