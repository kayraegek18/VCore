package net.kayega.core;

public enum PluginVersion {
    STABLE,
    DEBUG,
    BETA,
    ALPHA;

    public static String getVersionString(PluginVersion arg0) {
       switch (arg0) {
           case BETA:
               return "§6Beta";
           case ALPHA:
               return "§bAlpha";
           case DEBUG:
               return "§cDebug";
           case STABLE:
               return "§aStable";
           default:
               return "§4Unknown";
       }
    }

    public static boolean match(PluginVersion arg0, PluginVersion arg1) {
        return arg0 == arg1;
    }
}
