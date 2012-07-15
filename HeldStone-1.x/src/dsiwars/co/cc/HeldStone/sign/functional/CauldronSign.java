package dsiwars.co.cc.HeldStone.sign.functional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

import dsiwars.co.cc.HeldStone.HeldPlayer;
import dsiwars.co.cc.HeldStone.Items;
import dsiwars.co.cc.HeldStone.RecipeFile.Recipe;
import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class CauldronSign extends HeldSign {

	private boolean lastState = false;

	@Override
	protected void triggersign(TriggerType type, Object args) {
		InputState is = this.getInput(1, (BlockRedstoneEvent) args);

		if (is != InputState.HIGH) {
			this.lastState = false;
			return;
		} else {
			if (this.lastState == true) {
				return;
			}
			this.lastState = true;
		}

		int x = this.glassLocation.getBlockX();
		int y = this.glassLocation.getBlockY();
		int z = this.glassLocation.getBlockZ();

		boolean error = false;

		if (getWorld().getBlockAt(x, y, z).getTypeId() != 20) {
			error = true;
		}
		if (getWorld().getBlockAt(x, y + 1, z).getTypeId() != 9) {
			error = true;
		}
		if (getWorld().getBlockAt(x, y - 1, z).getTypeId() != 11) {
			error = true;
		}

		if (getWorld().getBlockAt(x + 1, y, z).getTypeId() != 1) {
			error = true;
		}
		if (getWorld().getBlockAt(x + 1, y + 1, z).getTypeId() != 1) {
			error = true;
		}
		if (getWorld().getBlockAt(x + 1, y - 1, z).getTypeId() != 1) {
			error = true;
		}

		if (getWorld().getBlockAt(x - 1, y, z).getTypeId() != 1) {
			error = true;
		}
		if (getWorld().getBlockAt(x - 1, y + 1, z).getTypeId() != 1) {
			error = true;
		}
		if (getWorld().getBlockAt(x - 1, y - 1, z).getTypeId() != 1) {
			error = true;
		}

		if (getWorld().getBlockAt(x, y, z + 1).getTypeId() != 1) {
			error = true;
		}
		if (getWorld().getBlockAt(x, y + 1, z + 1).getTypeId() != 1) {
			error = true;
		}
		if (getWorld().getBlockAt(x, y - 1, z + 1).getTypeId() != 1) {
			error = true;
		}

		if (getWorld().getBlockAt(x, y, z - 1).getTypeId() != 1) {
			error = true;
		}
		if (getWorld().getBlockAt(x, y + 1, z - 1).getTypeId() != 1) {
			error = true;
		}
		if (getWorld().getBlockAt(x, y - 1, z - 1).getTypeId() != 1) {
			error = true;
		}

		if (error) {
			this.main.sgc.invalidate(this, "Cauldron got damaged or removed.");
			this.main.alert(getOwnerName(), "Your cauldron is no longer valid.", ChatColor.RED);
			return;
		}

		//main.d("=========== START ===========");

		Location containerLocation = this.glassLocation.clone();

		containerLocation.add(0, 1, 0);

		List<Entity> entities = getWorld().getEntities();

		HashSet<ItemStack> items = new HashSet<ItemStack>();

		Iterator<Entity> entityIterator = entities.iterator();

		Items insertedItems = new Items(items);

		while (entityIterator.hasNext()) {
			Entity entity = entityIterator.next();

			if (entity.getLocation().getBlockX() == containerLocation.getBlockX() && entity.getLocation().getBlockY() == containerLocation.getBlockY() && entity.getLocation().getBlockZ() == containerLocation.getBlockZ()) {

				if (entity instanceof Item) {
					//main.d("Item detected!");

					insertedItems.add(((Item) entity).getItemStack());
				}
			}
		}

		//main.d("=========== INSERT ==========");

		ArrayList<Recipe> recipies = this.main.recipes.keys;

		Iterator<Recipe> i = recipies.iterator();

		//main.d("========== RECIPIES =========");

		while (i.hasNext()) {
			Recipe CurrentRecipe = i.next();

			Items recipeItems = CurrentRecipe.getItems(true);

			if (insertedItems.matches(recipeItems)) {
				//main.d("=========== VALID ===========");

				entityIterator = entities.iterator();

				while (entityIterator.hasNext()) {
					Entity currentEntity = entityIterator.next();

					if (currentEntity.getLocation().getBlockX() == containerLocation.getBlockX() && currentEntity.getLocation().getBlockY() == containerLocation.getBlockY() && currentEntity.getLocation().getBlockZ() == containerLocation.getBlockZ()) {
						if (currentEntity instanceof Player) {
							Player currentPlayer = (Player) currentEntity;

							currentPlayer.damage(20);
						} else {
							currentEntity.remove();
						}
					}
				}

				Items resultItems = CurrentRecipe.getItems(false);

				for (int j = 0; j < resultItems.items.size(); j++) {
					dsiwars.co.cc.HeldStone.Items.Item resultItem = resultItems.items.get(j);
					getWorld().dropItemNaturally(containerLocation, new ItemStack(resultItem.itemID, 1, resultItem.damage));
				}

				setOutput(false);

				return;
			} else {
				//main.d("========== INVALID ==========");
			}
		}

		//main.d("============ END ============");

		setOutput(true);
		return;
	}

	@Override
	protected void setNBTData(NBTBase tag) {
	}

	@Override
	public NBTBase getNBTData() {
		return new NBTTagInt(0);
	}

	Location glassLocation;

	@Override
	protected boolean declare(boolean reload, SignChangeEvent event) {
		int x = 0, y = 0, z = 0;

		if (this.getLines(event)[1].equals("")) {
			if (this.main.players.exists(getOwnerName())) {
				HeldPlayer p = this.main.players.get(getOwnerName());

				if (p.loc3 != null) {
					x = p.loc3.getBlockX() - getHostLocation().getBlockX();
					y = p.loc3.getBlockY() - getHostLocation().getBlockY();
					z = p.loc3.getBlockZ() - getHostLocation().getBlockZ();
				} else {
					this.main.alert(getOwnerName(), "You did not supply any arguments. You must either designate the cauldron glass block on line two, or set the block by right clicking with a piece of gunpowder.", ChatColor.RED);
					if (!reload) {
						event.setCancelled(true);
					}
					return false;
				}
			} else {
				this.main.alert(getOwnerName(), "You did not supply any arguments. You must either designate the cauldron glass block on line two, or set the block by right clicking with a piece of gunpowder.", ChatColor.RED);
				if (!reload) {
					event.setCancelled(true);
				}
				return false;
			}
		} else {
			String[] split;
			split = this.getLines(event)[1].split(" ");

			try {
				x = Integer.parseInt(split[0]);
				y = Integer.parseInt(split[1]);
				z = Integer.parseInt(split[2]);
			} catch (Exception e) {
				this.main.alert(getOwnerName(), "The coordinates you specified are either invalid or not formatted properly.", ChatColor.RED);
				if (!reload) {
					event.setCancelled(true);
				}
				return false;
			}
		}

		x += getHostLocation().getBlockX();
		y += getHostLocation().getBlockY();
		z += getHostLocation().getBlockZ();

		this.glassLocation = getWorld().getBlockAt(x, y, z).getLocation();

		boolean error = false;

		if (getWorld().getBlockAt(x, y, z).getTypeId() != 20) {
			error = true;
		}
		if (getWorld().getBlockAt(x, y + 1, z).getTypeId() != 9) {
			error = true;
		}
		if (getWorld().getBlockAt(x, y - 1, z).getTypeId() != 11) {
			error = true;
		}

		if (getWorld().getBlockAt(x + 1, y, z).getTypeId() != 1) {
			error = true;
		}
		if (getWorld().getBlockAt(x + 1, y + 1, z).getTypeId() != 1) {
			error = true;
		}
		if (getWorld().getBlockAt(x + 1, y - 1, z).getTypeId() != 1) {
			error = true;
		}

		if (getWorld().getBlockAt(x - 1, y, z).getTypeId() != 1) {
			error = true;
		}
		if (getWorld().getBlockAt(x - 1, y + 1, z).getTypeId() != 1) {
			error = true;
		}
		if (getWorld().getBlockAt(x - 1, y - 1, z).getTypeId() != 1) {
			error = true;
		}

		if (getWorld().getBlockAt(x, y, z + 1).getTypeId() != 1) {
			error = true;
		}
		if (getWorld().getBlockAt(x, y + 1, z + 1).getTypeId() != 1) {
			error = true;
		}
		if (getWorld().getBlockAt(x, y - 1, z + 1).getTypeId() != 1) {
			error = true;
		}

		if (getWorld().getBlockAt(x, y, z - 1).getTypeId() != 1) {
			error = true;
		}
		if (getWorld().getBlockAt(x, y + 1, z - 1).getTypeId() != 1) {
			error = true;
		}
		if (getWorld().getBlockAt(x, y - 1, z - 1).getTypeId() != 1) {
			error = true;
		}

		x -= getHostLocation().getBlockX();
		y -= getHostLocation().getBlockY();
		z -= getHostLocation().getBlockZ();

		if (!reload) {
			this.setLine(1, x + " " + y + " " + z, event);
		}

		if (error) {
			this.main.alert(getOwnerName(), "Your cauldron was not created properly.", ChatColor.RED);
			if (!reload) {
				event.setCancelled(true);
			}
			return false;
		}

		this.main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
		if (!reload) {
			init("Cauldron sign accepted.");
		}
		return true;
	}

	@Override
	public void invalidate() {
	}

	@Override
	public String getTriggerTypesString() {
		return TriggerType.REDSTONE_CHANGE.name();
	}
}