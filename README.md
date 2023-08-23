
# VCore

Bir çok proje için teker teker bu yardımcı öğeleri eklemek yerine sadece
bir maven kodu ekleyerek tüm bu özelliklere erişebilirsiniz.

Bu proje genel olarak kendi projelerimizi geliştirmek üzere yardımcı bir araç olması amacı ile geliştirdik.




## Özellikler

- Zırh giyme eventleri
- Dünya sınırını özelleştirme
- Özel komut oluşturma sistemi
- Özel config dosyası oluşturma sistemi
- Envanter oluşturma sistemi
- Particle effectleri olutuşturma
- Hologram oluşturma
- Ekrana veya action bar'a yazı gönderme
- Eşyalara nbt tagları ekleme

  
## Kurulum
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
```xml
<dependency>
    <groupId>com.github.kayraegek18</groupId>
    <artifactId>VCore</artifactId>
    <version>Tag</version>
</dependency>
```

## Kullanım/Örnekler

### Komut oluşturma
```java
@VCommandArguments(
  commandName = "test", <- required
  isPlayerCommand = true, <- optional 
  permission = "testperm", <- optional
  hasSubCommand = false <- required if have a sub command
)
public class TestKomut extends VCommand {
  public void run(Player player, String[] args) { 
    
  }

  public List<String> tabComplete(CommandSender player, String[] args) { 
    return null; 
  }
}
```
### Alt Komut oluşturma
```java
@VSubCommandArguments(
  commandName = "test", <- required
  commandDescription = "Test command", <- optional
  commandPermission = "testperm" <- required
)
public class TestKomut extends VSubCommand {
  public void run(Player player, String[] args) { 
    
  }

  public List<String> tabComplete(CommandSender player, String[] args) { 
    return null; 
  }
}
```
### Komut Sınıfındaki Ekstra Fonksiyonlar
```java
/* Verilen string listesi ve başlangıç 
 * indexini alarak başlangıç indexsinden itibaren
 * tüm string değerlerini tek bir string olarak döndürür 
 */
getFinalArgs(args, start)

/*
 * Sunucudaki tüm oyuncuların isimlerini
 * liste olarak döndürür
 */
getPlayerNicknames()

/*
 * Sunucudaki verilen değer ile başlayan 
 * oyuncuların isimlerini liste olarak döndürür
 */
getPlayerNicknames(startsWith)

/*
 * Verilen komutu sunucuya tanımlamak için kullanılır
 */
registerCommand(plugin, command)

/*
 * Verilen komutu sunucuya tanımlamak için kullanılır
 * + Tab tamamlamasını kullanmak için "withTabCompleter" değerini
 * "true" yapınız.
 */
registerCommand(plugin, command, withTabCompleter)
```

### Config Dosyası Oluşturma
```java
public class TestConfig extends VConfig {
  public TestConfig(JavaPlugin plugin) {
    super(plugin);
  }
}
```
Ardından main sınıfınızda şu çağırma işlemini yapabilirsiniz.
```java
...
@Override
public void onEnable() {
  TestConfig config = new TestConfig(this);
  // config.getConfig()
  // config.getConfig().addDefault("test", "test")
  // config.saveConfig()
  // config.reloadConfig()
}
...
```

### Envanter Arayüzü Oluşturma
```java
public class TestEnvater extends VInv {

    private boolean preventClose = false;

    public TestEnvater() {
        super(VInventorySize.Medium, ChatColor.GOLD + "Örnek envanter");

        // Just add a random item
        setItem(22, new ItemStack(Material.IRON_SWORD), e -> e.getWhoClicked().sendMessage("Kılıca tıkladınız!"));

        // Sınırlara block ekle
        setItems(getBorders(), new VItemBuilder(Material.LAPIS_BLOCK).name(" ").build());

        // Basit bir envanteri kapatma butonu ekleyelim
        setItem(34, new VItemBuilder(Material.BARRIER).name(ChatColor.RED + "Kapat").build(), e -> {
            this.preventClose = !this.preventClose;
        });

        // Eğer "preventClose" değeri false olursa envanteri kapatalım
        setCloseFilter(p -> this.preventClose);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        event.getPlayer().sendMessage(ChatColor.GOLD + "Envanteri açtınız!");
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        event.getPlayer().sendMessage(ChatColor.GOLD + "Envanteri kapattınız!");
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        // bişiler yapabilirsiniz.
    }
}
```
Şimdi oyuncuda envanteri açalım
```java
new TestEnvanter().open(player);
```
#### VInv örneğini nasıl alabilirim?
```java
if (inventory.getHolder() instanceof VInv) {
    VInv fastInv = (VInv) inventory.getHolder();
}
```

### Parçacık Efekti Oluşturma
```java
// Get a ParticleType
VParticleType flame = VParticleType.of("FLAME");
VParticleType redstone = VParticleType.of("REDSTONE");
VParticleType blockCrack = VParticleType.of("BLOCK_CRACK");

// Spawn particle for a player
flame.spawn(player, loc, 1);

// Spawn particle for all players in a world
flame.spawn(world, loc, 1);

// Spawn colored particle to a player
redstone.spawn(player, loc, 1, VParticleData.createDustOptions(Color.BLUE, 1));

// Spawn block crack particle to a player
blockCrack.spawn(player, loc, 1, VParticleData.createBlockData(Material.DIAMOND));
```
Çok miktarda parçacık oluşturmanız gerektiğinde, performansları biraz iyileştirmek için ``VParticleType`` ve ``VParticleData`` örneklerini önbelleğe alabilirsiniz.`

### Hologram Oluşturma
```java
// Lokasyon ve satırları yazın
VHologram hologram = new VHologram(loc, lines);

// Hologramları oluştur
hologram.spawn();

// Hologramları yok et
hologram.remove();
```

### Ekrana veya ActionBar'a Yazı Oluşturma
```java
// Ekrana yazı oluşturma
// fadeIn, stay ve fadeOut için 20 = 1 saniye, 10 = 0.5 saniye gibi yazmalısınız
VTitle.sendTitle(player, title, subTitle, fadeIn, stay, fadeOut);
// ActionBar'a yazı oluşturma
VTitle.sendActionbar(player, text)
```

### NBT Tagları Oluşturma
```java
VNbt nbt = new VNbt(item, key);
nbt.add(type, value); // Değer ekleme
nbt.has(type); // Değerin olup olmadığını kontrol etme
nbt.get(type); // Değeri okuma
nbt.getItemContainer();
nbt.getKey();
```
  
