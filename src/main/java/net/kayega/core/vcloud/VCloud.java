package net.kayega.core.vcloud;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginAwareness;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.File;
import java.util.Map;
import java.util.regex.Pattern;

public class VCloud {
    private static final Pattern VALID_NAME = Pattern.compile("^[A-Za-z0-9 _.-]+$");
    public static final ThreadLocal<Yaml> YAML = new ThreadLocal<Yaml>() {
        @Override
        @NotNull
        protected Yaml initialValue() {
            return new Yaml(new SafeConstructor() {
                {
                    yamlConstructors.put(null, new AbstractConstruct() {
                        @NotNull
                        @Override
                        public Object construct(@NotNull final Node node) {
                            if (!node.getTag().startsWith("!@")) {
                                // Unknown tag - will fail
                                return SafeConstructor.undefinedConstructor.construct(node);
                            }
                            // Unknown awareness - provide a graceful substitution
                            return new PluginAwareness() {
                                @Override
                                public String toString() {
                                    return node.toString();
                                }
                            };
                        }
                    });
                    for (final PluginAwareness.Flags flag : PluginAwareness.Flags.values()) {
                        yamlConstructors.put(new Tag("!@" + flag.name()), new AbstractConstruct() {
                            @NotNull
                            @Override
                            public PluginAwareness.Flags construct(@NotNull final Node node) {
                                return flag;
                            }
                        });
                    }
                }
            });
        }
    };
    private final String dataPath;
    public VCloud(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder() + "/vcloud");
        if (!file.exists()) {
            file.mkdirs();
        }
        dataPath = plugin.getDataFolder() + "/vcloud";
    }

    public String getDataPath() {
        return this.dataPath;
    }

    @NotNull
    public static Map<?, ?> asMap(@NotNull Object object) throws InvalidDescriptionException {
        if (object instanceof Map) {
            return (Map<?, ?>) object;
        }
        throw new InvalidDescriptionException("Plugin description file is empty or not properly structured. Is " + object + "but should be a map.");
    }
}
