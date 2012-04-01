package dsiwars.co.cc.HeldStone;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import dsiwars.co.cc.HeldStone.CraftingRecipeFile.CraftingRecipe;
import dsiwars.co.cc.HeldStone.sign.SignCommand;
import dsiwars.co.cc.HeldStone.sign.SignController;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class HeldStone extends JavaPlugin {

	public SignController sgc;
	public BlockController bcr;
	public TickControl tickctrl;
	public HeldPlayers players = new HeldPlayers();
	private ListenerBlock blockListener = new ListenerBlock(this);
	private ListenerCuboid cuboidListener = new ListenerCuboid(this);
	private ListenerEntity entityListener = new ListenerEntity(this);
	private ListenerPlayer playerListener = new ListenerPlayer(this, players);
	public String dataPath = "";
	public File configFile = null;
	public ConfigFile cfg;
	public File recipeFile = null;
	public RecipeFile recipes;
	public CraftingRecipeFile craftingRecipes;
	public PluginDescriptionFile pdf;
	public Random rand = new Random();
	public PluginManager pm;
	public boolean cfgWipeProtection;
	public boolean cfgAddsCraftingRecipes;
	public Integer cfgMaxCuboidBlocks;
	public Integer cfgAppleDropChance;
	public boolean cfgRedstoneCheck;
	public boolean cfgDebug;
	public boolean enabled = false;
	public HashSet<Entity> explodingList;
	static final Logger log = Logger.getLogger("HeldStone");
	// Update manager
	Update upd = new Update(this);
	public String address = "";
	public String versionaddress = "";
	public String updatereasonaddress = "";
	public static String updatepath = "plugins" + File.separator + "HeldStone.jar";
	public static String version;

	public void onDisable() {
		sgc.saveNBT();
		cfg.save();
		this.i("Heldstone is now disabled.");
		enabled = false;
	}

	public void onEnable() {
		ConsoleLogManager.init();

		dataPath = getDataFolder().getAbsolutePath();

		this.explodingList = new HashSet<Entity>();

		pdf = this.getDescription();

		this.i("Enabling " + pdf.getName());

		this.pm = this.getServer().getPluginManager();

		File dataFolder = getDataFolder();

		if (!dataFolder.exists()) {
			dataFolder.mkdir();
		}

		version = pdf.getVersion();

		configFile = new File(dataPath + "/config.txt");

		cfg = new ConfigFile(this, configFile);
		cfg.load();

		cfgWipeProtection = cfg.getBoolean("wipe-protection", true);
		cfgAddsCraftingRecipes = cfg.getBoolean("add-crafting-recipes", true);
		cfgMaxCuboidBlocks = cfg.getInt("max-cuboid-blocks", 200);
		cfgAppleDropChance = cfg.getInt("apple-drop-chance", 1);
		cfgDebug = cfg.getBoolean("debug", false);
		cfgRedstoneCheck = cfg.getBoolean("enabke-redstone-toggles", false);

		cfg.save();

		setupRecipes();

		recipeFile = new File(dataPath + "/recipes.txt");

		recipes = new RecipeFile(this, recipeFile);
		recipes.load();

		sgc = new SignController(this);
		bcr = new BlockController(this);
		tickctrl = new TickControl(this);

		sgc.load();
		getServer().getPluginManager().registerEvents(blockListener, this);
		getServer().getPluginManager().registerEvents(cuboidListener, this);
		getServer().getPluginManager().registerEvents(playerListener, this);
		getServer().getPluginManager().registerEvents(entityListener, this);

		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TickUpdate(TriggerType.TIMER_SECOND, sgc), 10, 20);
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TickUpdate(TriggerType.TIMER_HALF_SECOND, sgc), 10, 10);
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TickUpdate(TriggerType.VALID_CHECK, sgc), 10, 600);

		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, tickctrl, 10, 1);

		this.getCommand("trigger").setExecutor(new SignCommand(sgc, TriggerType.TRIGGER_COMMAND));
		this.getCommand("t").setExecutor(new SignCommand(sgc, TriggerType.TRIGGER_COMMAND));
		this.getCommand("heldstone").setExecutor(new HSCommand(this));
		this.getCommand("hs").setExecutor(new HSCommand(this));

		this.i(pdf.getName() + " version " + pdf.getVersion() + " is now enabled.");

		enabled = true;
	}

	public void i(String message) {
		log.log(Level.INFO, "[" + pdf.getName() + "] " + message);
	}

	public void e(String message) {
		log.log(Level.WARNING, "[" + pdf.getName() + "] " + message);
	}

	public void d(String message) {
		if (cfgDebug) {
			i("[DEBUG] " + message);
		}
	}

	public boolean alert(String playerName, String message, ChatColor color) {
		Player p = this.getServer().getPlayer(playerName);
		if (p != null && p.isOnline()) {
			p.sendMessage(color + "[" + pdf.getName() + "] " + message);
			return true;
		}
		return false;
	}

	public boolean alert(String playerName, String message, ChatColor color, String prefix) {
		Player p = this.getServer().getPlayer(playerName);
		if (p != null && p.isOnline()) {
			p.sendMessage(color + prefix + " " + message);
			return true;
		}
		return false;
	}

	public void setupRecipes() {
		if (cfgAddsCraftingRecipes) {
			this.i("Loading crafting recipes...");

			File CraftingRecipeFileFile = new File(dataPath + "/craftingrecipes.txt");

			if (!CraftingRecipeFileFile.exists()) {
				try {
					CraftingRecipeFileFile.createNewFile();
				} catch (Exception ex) {
					this.e("Unable to load cratfing recipes file:");

					this.e(ex.getMessage());

					return;
				}
			}

			craftingRecipes = new CraftingRecipeFile(this, CraftingRecipeFileFile);
			craftingRecipes.load();

			Iterator<CraftingRecipe> keys = craftingRecipes.keys.iterator();

			while (keys.hasNext()) {
				CraftingRecipe key = keys.next();

				ShapelessRecipe recipe = new ShapelessRecipe(key.getResultItemStack());

				HashSet<ItemStack> itemStacks = key.getItemStacks();

				Iterator<ItemStack> i = itemStacks.iterator();

				while (i.hasNext()) {
					ItemStack IS = i.next();

					recipe.addIngredient(IS.getAmount(), Material.getMaterial(IS.getTypeId()), IS.getDurability());
				}

				this.getServer().addRecipe(recipe);
			}
		} else {
			this.i("No custom crafting recipes will be loaded!");
		}
	}

	public boolean hasPermission(Player p, String permission) {
		return p.hasPermission(permission);
	}
}