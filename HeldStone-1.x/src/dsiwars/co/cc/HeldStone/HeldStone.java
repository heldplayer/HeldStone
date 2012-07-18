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
	private ListenerPlayer playerListener = new ListenerPlayer(this, this.players);
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
	static Logger log;
	// Update manager
	Update upd = new Update(this);
	public String address = "";
	public String versionaddress = "";
	public String updatereasonaddress = "";
	public static String updatepath = "plugins" + File.separator + "HeldStone.jar";
	public static String version;

	@Override
	public void onDisable() {
		this.sgc.saveNBT();
		this.cfg.save();
		i("Heldstone is now disabled.");
		this.enabled = false;

		ConsoleLogManager.exit();
	}

	@Override
	public void onEnable() {
		log = getLogger();

		ConsoleLogManager.init();

		this.dataPath = getDataFolder().getAbsolutePath();

		this.explodingList = new HashSet<Entity>();

		this.pdf = getDescription();

		this.pm = getServer().getPluginManager();

		File dataFolder = getDataFolder();

		if (!dataFolder.exists()) {
			dataFolder.mkdir();
		}

		version = this.pdf.getVersion();

		this.configFile = new File(this.dataPath + "/config.txt");

		this.cfg = new ConfigFile(this, this.configFile);
		this.cfg.load();

		this.cfgWipeProtection = this.cfg.getBoolean("wipe-protection", true);
		this.cfgAddsCraftingRecipes = this.cfg.getBoolean("add-crafting-recipes", true);
		this.cfgMaxCuboidBlocks = this.cfg.getInt("max-cuboid-blocks", 200);
		this.cfgAppleDropChance = this.cfg.getInt("apple-drop-chance", 1);
		this.cfgDebug = this.cfg.getBoolean("debug", false);
		this.cfgRedstoneCheck = this.cfg.getBoolean("enabke-redstone-toggles", false);

		this.cfg.save();

		setupRecipes();

		this.recipeFile = new File(this.dataPath + "/recipes.txt");

		this.recipes = new RecipeFile(this, this.recipeFile);
		this.recipes.load();

		this.sgc = new SignController(this);
		this.bcr = new BlockController(this);
		this.tickctrl = new TickControl(this);

		this.sgc.load();
		getServer().getPluginManager().registerEvents(this.blockListener, this);
		getServer().getPluginManager().registerEvents(this.cuboidListener, this);
		getServer().getPluginManager().registerEvents(this.playerListener, this);
		getServer().getPluginManager().registerEvents(this.entityListener, this);

		getServer().getScheduler().scheduleSyncRepeatingTask(this, new TickUpdate(TriggerType.TIMER_SECOND, this.sgc), 10, 20);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new TickUpdate(TriggerType.TIMER_HALF_SECOND, this.sgc), 10, 10);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new TickUpdate(TriggerType.VALID_CHECK, this.sgc), 10, 600);

		getServer().getScheduler().scheduleSyncRepeatingTask(this, this.tickctrl, 10, 1);

		getCommand("trigger").setExecutor(new SignCommand(this.sgc, TriggerType.TRIGGER_COMMAND));
		getCommand("t").setExecutor(new SignCommand(this.sgc, TriggerType.TRIGGER_COMMAND));
		getCommand("heldstone").setExecutor(new HSCommand(this));
		getCommand("hs").setExecutor(new HSCommand(this));

		i(this.pdf.getName() + " version " + this.pdf.getVersion() + " is now enabled.");

		this.enabled = true;
	}

	public void i(String message) {
		log.log(Level.INFO, message);
	}

	public void e(String message) {
		log.log(Level.WARNING, message);
	}

	public void d(String message) {
		if (this.cfgDebug) {
			i("[DEBUG] " + message);
		}
	}

	public boolean alert(String playerName, String message, ChatColor color) {
		Player p = getServer().getPlayer(playerName);
		if (p != null && p.isOnline()) {
			p.sendMessage(color + "[" + this.pdf.getName() + "] " + message);
			return true;
		}
		return false;
	}

	public boolean alert(String playerName, String message, ChatColor color, String prefix) {
		Player p = getServer().getPlayer(playerName);
		if (p != null && p.isOnline()) {
			p.sendMessage(color + prefix + " " + message);
			return true;
		}
		return false;
	}

	public void setupRecipes() {
		if (this.cfgAddsCraftingRecipes) {
			i("Loading crafting recipes...");

			File CraftingRecipeFileFile = new File(this.dataPath + "/craftingrecipes.txt");

			if (!CraftingRecipeFileFile.exists()) {
				try {
					CraftingRecipeFileFile.createNewFile();
				} catch (Exception ex) {
					e("Unable to load cratfing recipes file:");

					e(ex.getMessage());

					return;
				}
			}

			this.craftingRecipes = new CraftingRecipeFile(this, CraftingRecipeFileFile);
			this.craftingRecipes.load();

			Iterator<CraftingRecipe> keys = this.craftingRecipes.keys.iterator();

			while (keys.hasNext()) {
				CraftingRecipe key = keys.next();

				ShapelessRecipe recipe = new ShapelessRecipe(key.getResultItemStack());

				HashSet<ItemStack> itemStacks = key.getItemStacks();

				Iterator<ItemStack> i = itemStacks.iterator();

				while (i.hasNext()) {
					ItemStack IS = i.next();

					recipe.addIngredient(IS.getAmount(), Material.getMaterial(IS.getTypeId()), IS.getDurability());
				}

				getServer().addRecipe(recipe);
			}
		} else {
			i("No custom crafting recipes will be loaded!");
		}
	}

	public boolean hasPermission(Player p, String permission) {
		return p.hasPermission(permission);
	}
}