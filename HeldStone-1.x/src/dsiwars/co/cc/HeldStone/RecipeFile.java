package dsiwars.co.cc.HeldStone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.inventory.ItemStack;

public class RecipeFile {

	private final HeldStone main;
	private File cfg;
	public ArrayList<Recipe> keys = new ArrayList<Recipe>();

	public RecipeFile(HeldStone main, File cfg) {
		this.main = main;
		this.cfg = cfg;
	}

	public void load() {
		keys.clear();

		if (cfg.isDirectory()) {
			cfg = new File(cfg.getAbsolutePath() + "recipes.txt");
		}

		if (!cfg.exists()) {
			try {
				cfg.createNewFile();
			} catch (IOException e) {
				this.main.e("Error while creating recipe file.");
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
						if (line.contains(":")) {
							String[] args = line.split(":");
							if (args.length == 3) {
								this.main.d("Adding recipe " + args[0]);
								keys.add(new Recipe(args));
							}
						}
					}
				}
			} catch (Exception e) {
				this.main.e("Error while updating recipe file.");
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

	public static class Recipe {

		private String name = "";
		private ItemType[] Requirements;
		private ItemType[] Results;

		public Recipe(String[] args) {
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

			String[] ResultItems = args[2].split("~");

			Results = new ItemType[ResultItems.length];

			for (int i = 0; i < ResultItems.length; i++) {
				String[] item = ResultItems[i].split(",");

				try {
					Results[i] = new ItemType(Integer.parseInt(item[0]), Short.parseShort(item[1]), Integer.parseInt(item[2]));
				} catch (Exception ex) {
				}
			}
		}

		public String getName() {
			return this.name;
		}

		public ItemType[] getRequirements() {
			return Requirements;
		}

		public ItemType[] getResults() {
			return Results;
		}

		public Items getItems(boolean fromReqs) {
			if (fromReqs) {
				HashSet<ItemStack> items = new HashSet<ItemStack>();

				for (int i = 0; i < this.Requirements.length; i++) {
					ItemStack IS = new ItemStack(Requirements[i].getID(), Requirements[i].getAmount(), Requirements[i].getDamage());

					items.add(IS);
				}

				Items returned = new Items(items);

				return returned;
			} else {
				HashSet<ItemStack> items = new HashSet<ItemStack>();

				for (int i = 0; i < this.Results.length; i++) {
					ItemStack IS = new ItemStack(Results[i].getID(), Results[i].getAmount(), Results[i].getDamage());

					items.add(IS);
				}

				Items returned = new Items(items);

				return returned;
			}
		}
	}
}