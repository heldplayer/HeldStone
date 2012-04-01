package dsiwars.co.cc.HeldStone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.inventory.ItemStack;

public class CraftingRecipeFile {

	private final HeldStone main;
	private File cfg;
	public ArrayList<CraftingRecipe> keys = new ArrayList<CraftingRecipe>();

	public CraftingRecipeFile(HeldStone main, File cfg) {
		this.main = main;
		this.cfg = cfg;
	}

	public void load() {
		keys.clear();

		if (cfg.isDirectory()) {
			cfg = new File(cfg.getAbsolutePath() + "craftingrecipes.txt");
		}

		if (!cfg.exists()) {
			try {
				cfg.createNewFile();
			} catch (IOException e) {
				this.main.e("Error while creating crafting recipe file.");
				this.main.e("File path: " + cfg.getAbsolutePath());
				e.printStackTrace();
			}
		} else {
			try {
				BufferedReader in = new BufferedReader(new FileReader(cfg));
				String line;
				while ((line = in.readLine()) != null) {
					line = line.trim();
					if (line.charAt(0) != '#') {
						String[] args = line.split(":");
						if (args.length == 3) {
							this.main.d("Adding crafting recipe " + args[0]);
							keys.add(new CraftingRecipe(args));
						} else {
							this.main.d("Error while adding " + args[0] + ": Invalid amount of parameters!");
						}
					}
				}
			} catch (Exception e) {
				this.main.e("Error while updating crafting recipe file.");
				e.printStackTrace();
			}
		}
	}

	public static class ItemType {

		private Integer itemID = 0;
		private Short damage = 0;
		private Integer amount = 0;

		public ItemType(Integer itemID, Short damage, Integer amount) {
			this.itemID = itemID;
			this.damage = damage;
			this.amount = amount;
		}

		public Short getDamage() {
			return this.damage;
		}

		public Integer getID() {
			return this.itemID;
		}

		public Integer getAmount() {
			return this.amount;
		}
	}

	public static class CraftingRecipe {

		private String name = "";
		private ItemType[] Requirements;
		private ItemType Result;

		public CraftingRecipe(String[] args) {
			name = args[0];

			String[] RequirementItems = args[1].split("~");

			Requirements = new ItemType[RequirementItems.length];

			for (int i = 0; i < RequirementItems.length; i++) {
				String[] item = RequirementItems[i].split(",");
				try {
					Requirements[i] = new ItemType(Integer.parseInt(item[0]), Short.parseShort(item[1]), Integer.parseInt(item[2]));
				} catch (Exception ex) {
				}
			}

			String[] item = args[2].split(",");

			try {
				Result = new ItemType(Integer.parseInt(item[0]), Short.parseShort(item[1]), Integer.parseInt(item[2]));
			} catch (Exception ex) {
			}
		}

		public String getName() {
			return this.name;
		}

		public ItemType[] getRequirements() {
			return Requirements;
		}

		public ItemType getResults() {
			return Result;
		}

		public Items getResultItem() {
			HashSet<ItemStack> items = new HashSet<ItemStack>();

			items.add(new ItemStack(Result.getID(), Result.getAmount(), Result.getDamage()));

			return new Items(items);
		}

		public ItemStack getResultItemStack() {
			return new ItemStack(Result.getID(), Result.getAmount(), Result.getDamage());
		}

		public Items getItems() {
			HashSet<ItemStack> items = new HashSet<ItemStack>();

			for (int i = 0; i < this.Requirements.length; i++) {
				ItemStack IS = new ItemStack(Requirements[i].getID(), Requirements[i].getAmount(), Requirements[i].getDamage());

				items.add(IS);
			}

			Items returned = new Items(items);

			return returned;
		}

		public HashSet<ItemStack> getItemStacks() {
			HashSet<ItemStack> items = new HashSet<ItemStack>();

			for (int i = 0; i < this.Requirements.length; i++) {
				ItemStack IS = new ItemStack(Requirements[i].getID(), Requirements[i].getAmount(), Requirements[i].getDamage());

				items.add(IS);
			}

			return items;
		}
	}
}