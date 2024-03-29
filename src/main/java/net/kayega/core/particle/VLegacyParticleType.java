package net.kayega.core.particle;

import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("deprecation")
final class VLegacyParticleType implements VParticleType {
    private static final Class<?> ENUM_PARTICLE = VReflection.nmsOptionalClass("EnumParticle").orElse(null);
    private static final Map<String, String> LEGACY_NAMES = Collections.unmodifiableMap(getLegacyParticleNames());
    private static final int[] EMPTY_DATA = new int[0];
    private static final boolean IS_1_8 = ENUM_PARTICLE != null;

    private static final Method WORLD_GET_HANDLE;
    private static final Method WORLD_SEND_PARTICLE;
    private static final Constructor<?> PACKET_PARTICLE;
    private static final Method PLAYER_GET_HANDLE;
    private static final Field PLAYER_CONNECTION;
    private static final Method SEND_PACKET;

    static {
        try {
            Class<?> packetParticleClass = VReflection.nmsClass("PacketPlayOutWorldParticles");
            Class<?> playerClass = VReflection.nmsClass("EntityPlayer");
            Class<?> playerConnectionClass = VReflection.nmsClass("PlayerConnection");
            Class<?> worldClass = VReflection.nmsClass("WorldServer");
            Class<?> entityPlayerClass = VReflection.nmsClass("EntityPlayer");
            Class<?> craftPlayerClass = VReflection.obcClass("entity.CraftPlayer");
            Class<?> craftWorldClass = VReflection.obcClass("CraftWorld");

            if (IS_1_8) {
                PACKET_PARTICLE = packetParticleClass.getConstructor(ENUM_PARTICLE, boolean.class, float.class,
                        float.class, float.class, float.class, float.class, float.class, float.class, int.class,
                        int[].class);
                WORLD_SEND_PARTICLE = worldClass.getDeclaredMethod("sendParticles", entityPlayerClass, ENUM_PARTICLE,
                        boolean.class, double.class, double.class, double.class, int.class, double.class, double.class,
                        double.class, double.class, int[].class);
            } else {
                PACKET_PARTICLE = packetParticleClass.getConstructor(String.class, float.class, float.class, float.class,
                        float.class, float.class, float.class, float.class, int.class);
                WORLD_SEND_PARTICLE = worldClass.getDeclaredMethod("a", String.class, double.class, double.class,
                        double.class, int.class, double.class, double.class, double.class, double.class);
            }

            WORLD_GET_HANDLE = craftWorldClass.getDeclaredMethod("getHandle");
            PLAYER_GET_HANDLE = craftPlayerClass.getDeclaredMethod("getHandle");
            PLAYER_CONNECTION = playerClass.getField("playerConnection");
            SEND_PACKET = playerConnectionClass.getMethod("sendPacket", VReflection.nmsClass("Packet"));
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final String name;
    private final Object particle; // String on 1.7.10 and EnumParticle on 1.8.8

    private VLegacyParticleType(String name, Object particle) {
        this.name = Objects.requireNonNull(name, "name");
        this.particle = Objects.requireNonNull(particle, "particle");
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Class<?> getRawDataType() {
        switch (this.name) {
            case "ITEM_CRACK":
                return ItemStack.class;
            case "BLOCK_CRACK":
            case "BLOCK_DUST":
            case "FALLING_DUST":
                return MaterialData.class;
            case "REDSTONE":
                return Color.class;
            default:
                return Void.class;
        }
    }

    @Override
    public void spawn(World world, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, Object rawData) {
        try {
            if (rawData instanceof VParticleData.AbstractParticleData) {
                rawData = ((VParticleData.AbstractParticleData) rawData).data;
            }

            int[] data = toData(rawData);
            Object worldServer = WORLD_GET_HANDLE.invoke(world);

            if (rawData instanceof Color && getRawDataType() == Color.class) {
                Color color = (Color) rawData;
                count = 0;
                offsetX = VParticleTypes.color(color.getRed());
                offsetY = VParticleTypes.color(color.getGreen());
                offsetZ = VParticleTypes.color(color.getBlue());
                extra = 1.0;
            }

            if (IS_1_8) {
                WORLD_SEND_PARTICLE.invoke(worldServer, null, this.particle, true, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
            } else {
                String particleName = this.particle + (data.length != 2 ? "" : "_" + data[0] + "_" + data[1]);
                WORLD_SEND_PARTICLE.invoke(worldServer, particleName, x, y, z, count, offsetX, offsetY, offsetZ, extra);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void spawn(Player player, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, Object rawData) {
        try {
            if (rawData instanceof VParticleData.AbstractParticleData) {
                rawData = ((VParticleData.AbstractParticleData) rawData).data;
            }

            int[] data = toData(rawData);
            Object packet;

            if (rawData instanceof Color && getRawDataType() == Color.class) {
                Color color = (Color) rawData;
                count = 0;
                offsetX = VParticleTypes.color(color.getRed());
                offsetY = VParticleTypes.color(color.getGreen());
                offsetZ = VParticleTypes.color(color.getBlue());
                extra = 1.0;
            }

            if (IS_1_8) {
                packet = PACKET_PARTICLE.newInstance(this.particle, true, (float) x, (float) y,
                        (float) z, (float) offsetX, (float) offsetY, (float) offsetZ, (float) extra, count, data);
            } else {
                String particleName = this.particle + (data.length != 2 ? "" : "_" + data[0] + "_" + data[1]);
                packet = PACKET_PARTICLE.newInstance(particleName, (float) x, (float) y, (float) z,
                        (float) offsetX, (float) offsetY, (float) offsetZ, (float) extra, count);
            }

            Object entityPlayer = PLAYER_GET_HANDLE.invoke(player);
            Object playerConnection = PLAYER_CONNECTION.get(entityPlayer);
            SEND_PACKET.invoke(playerConnection, packet);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private int[] toData(Object data) {
        Class<?> dataType = getRawDataType();

        if (dataType == ItemStack.class) {
            if (!(data instanceof ItemStack)) {
                return IS_1_8 ? new int[2] : new int[]{1, 0};
            }

            ItemStack itemStack = (ItemStack) data;
            return new int[]{itemStack.getType().getId(), itemStack.getDurability()};
        }

        if (dataType == MaterialData.class) {
            if (!(data instanceof MaterialData)) {
                return IS_1_8 ? new int[1] : new int[]{1, 0};
            }

            MaterialData materialData = (MaterialData) data;
            if (IS_1_8) {
                return new int[]{materialData.getItemType().getId() + (materialData.getData() << 12)};
            } else {
                return new int[]{materialData.getItemType().getId(), materialData.getData()};
            }
        }

        return EMPTY_DATA;
    }

    static VParticleType of(String name) {
        if (IS_1_8) {
            return new VLegacyParticleType(name, VReflection.enumValueOf(ENUM_PARTICLE, name));
        }

        String legacyName = LEGACY_NAMES.get(name);

        if (legacyName == null) {
            throw new IllegalArgumentException("Invalid legacy particle: " + name);
        }

        return new VLegacyParticleType(name, legacyName);
    }

    private static Map<String, String> getLegacyParticleNames() {
        Map<String, String> legacyNames = new HashMap<>(64);

        legacyNames.put("EXPLOSION_NORMAL", "explode");
        legacyNames.put("EXPLOSION_LARGE", "largeexplode");
        legacyNames.put("EXPLOSION_HUGE", "hugeexplosion");
        legacyNames.put("FIREWORKS_SPARK", "fireworksSpark");
        legacyNames.put("WATER_BUBBLE", "bubble");
        legacyNames.put("WATER_SPLASH", "splash");
        legacyNames.put("WATER_WAKE", "wake");
        legacyNames.put("SUSPENDED", "suspended");
        legacyNames.put("SUSPENDED_DEPTH", "depthsuspend");
        legacyNames.put("CRIT", "crit");
        legacyNames.put("CRIT_MAGIC", "magicCrit");
        legacyNames.put("SMOKE_NORMAL", "smoke");
        legacyNames.put("SMOKE_LARGE", "largesmoke");
        legacyNames.put("SPELL", "spell");
        legacyNames.put("SPELL_INSTANT", "instantSpell");
        legacyNames.put("SPELL_MOB", "mobSpell");
        legacyNames.put("SPELL_MOB_AMBIENT", "mobSpellAmbient");
        legacyNames.put("SPELL_WITCH", "witchMagic");
        legacyNames.put("DRIP_WATER", "dripWater");
        legacyNames.put("DRIP_LAVA", "dripLava");
        legacyNames.put("VILLAGER_ANGRY", "angryVillager");
        legacyNames.put("VILLAGER_HAPPY", "happyVillager");
        legacyNames.put("TOWN_AURA", "townaura");
        legacyNames.put("NOTE", "note");
        legacyNames.put("PORTAL", "portal");
        legacyNames.put("ENCHANTMENT_TABLE", "enchantmenttable");
        legacyNames.put("FLAME", "flame");
        legacyNames.put("LAVA", "lava");
        legacyNames.put("CLOUD", "cloud");
        legacyNames.put("REDSTONE", "reddust");
        legacyNames.put("SNOWBALL", "snowballpoof");
        legacyNames.put("SNOW_SHOVEL", "snowshovel");
        legacyNames.put("SLIME", "slime");
        legacyNames.put("HEART", "heart");
        legacyNames.put("ITEM_CRACK", "iconcrack");
        legacyNames.put("BLOCK_CRACK", "blockcrack");
        legacyNames.put("BLOCK_DUST", "blockdust");

        return legacyNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VLegacyParticleType)) {
            return false;
        }
        VLegacyParticleType particleType = (VLegacyParticleType) o;
        return this.particle == particleType.particle;
    }

    @Override
    public int hashCode() {
        return this.particle.hashCode();
    }

    @Override
    public String toString() {
        return "LegacyParticleType{particle=" + this.particle + '}';
    }
}
