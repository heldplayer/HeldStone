package dsiwars.co.cc.HeldStone.sign.spawn;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Ocelot.Type;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Wolf;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.Colorable;

import dsiwars.co.cc.HeldStone.Direction;
import dsiwars.co.cc.HeldStone.Functions;
import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class SpawnSign extends HeldSign {

	protected void triggersign(TriggerType type, Object args) {
		if (args == null) {
			return;
		}
		if (this.getInputId((BlockRedstoneEvent) args) != 1 || this.getInput(1, (BlockRedstoneEvent) args) != InputState.HIGH) {
			return;
		}

		Location spawnLoc = null;

		for (int i = 1; i <= 10; i++) {
			spawnLoc = Direction.shift(this.getHostLocation(), spawnDir, i);
			if ((this.getWorld().getBlockAt(spawnLoc).getType() == Material.AIR) && (this.getWorld().isChunkLoaded(this.getWorld().getBlockAt(spawnLoc).getChunk()))) {

				spawnLoc.setX(spawnLoc.getX() + .5);
				spawnLoc.setZ(spawnLoc.getZ() + .5);

				for (int i2 = 0; i2 < amount; i2++) {
					LivingEntity c = this.getWorld().spawnCreature(spawnLoc, creature);

					if (c instanceof Wolf) {
						if (data.equalsIgnoreCase("angry")) {
							Wolf wolf = (Wolf) c;
							wolf.setAngry(true);
						}
						if (data.startsWith("@")) {
							Wolf wolf = (Wolf) c;

							wolf.setOwner(main.getServer().getPlayer(data.substring(1)));
						}
					} else if (c instanceof Ocelot) {
						if (data.startsWith("tabby@")) {
							Ocelot ocelot = (Ocelot) c;
							ocelot.setCatType(Type.RED_CAT);
							ocelot.setOwner(main.getServer().getPlayer(data.substring(6)));
						}
						if (data.startsWith("tuxedo@")) {
							Ocelot ocelot = (Ocelot) c;
							ocelot.setCatType(Type.BLACK_CAT);
							ocelot.setOwner(main.getServer().getPlayer(data.substring(7)));
						}
						if (data.startsWith("siamese@")) {
							Ocelot ocelot = (Ocelot) c;
							ocelot.setCatType(Type.SIAMESE_CAT);
							ocelot.setOwner(main.getServer().getPlayer(data.substring(8)));
						}
					} else if (c instanceof Colorable) {
						boolean flag = false;
						if (c instanceof Sheep) {
							if (data.equalsIgnoreCase("sheared")) {
								flag = true;
							}
						}
						if (flag) {
							Sheep sheep = (Sheep) c;

							sheep.setSheared(true);
						} else {
							byte color = (byte) 0;
							try {
								color = (byte) Math.min(Integer.parseInt(data), 16);
							} catch (Exception e) {
								color = (byte) 0;
							}

							Colorable cle = (Colorable) c;
							cle.setColor(DyeColor.getByData(color));
						}
					} else if (c instanceof Slime) {
						Slime s = (Slime) c;
						int size = 0;
						try {
							size = Math.min(Integer.parseInt(data), 16);
						} catch (Exception e) {
							size = 1;
						}
						s.setSize(size);
					} else if (c instanceof Creeper) {
						if (data.equalsIgnoreCase("powered")) {
							Creeper creeper = (Creeper) c;

							creeper.setPowered(true);
						}
					} else if (c instanceof PigZombie) {
						if (data.equalsIgnoreCase("angry")) {
							PigZombie pigzombie = (PigZombie) c;

							pigzombie.setAngry(true);
						} else {
							int level = 0;
							try {
								level = Integer.parseInt(data);
							} catch (Exception e) {
								level = 0;
							}

							PigZombie pigzombie = (PigZombie) c;
							pigzombie.setAnger(level);
						}
					} else if (c instanceof Ageable) {
						if (data.equalsIgnoreCase("baby")) {
							Ageable ageable = (Ageable) c;

							ageable.setBaby();
						}
					} else if (c instanceof Pig) {
						if (data.equalsIgnoreCase("saddled")) {
							Pig pig = (Pig) c;

							pig.setSaddle(true);
						}
					} else if (data.equalsIgnoreCase("ignite")) {
						c.setFireTicks(100);
					}
				}
				break;
			}
		}
	}

	@Override
	protected void setNBTData(NBTBase tag) {
	}

	@Override
	public NBTBase getNBTData() {
		return new NBTTagInt(0);
	}

	private EntityType creature;
	private Direction spawnDir;
	private Integer amount = 1;
	private String data = "";

	protected boolean declare(boolean reload, SignChangeEvent event) {

		boolean ctypeError = false;

		String cline = this.getLines(event)[1];

		String[] dline = this.getLines(event)[2].split(" ");

		try {
			creature = Functions.getCreature(cline);
		} catch (Exception e) {
			ctypeError = true;
			if (!reload) {
				this.main.alert(this.getOwnerName(), "Unknown creature type. Please try again.", ChatColor.RED);
			}
			return false;
		}

		if (ctypeError || (creature == null)) {
			if (!reload) {
				this.main.alert(this.getOwnerName(), "Unknown creature type. Please try again.", ChatColor.RED);
			}
			event.setCancelled(true);
			return false;
		}

		String amountLine = dline[0];

		try {
			amount = Integer.parseInt(amountLine);
		} catch (Exception ex) {
			amount = 1;
		}

		if (amount <= 0) {
			amount = 1;
		}

		if (amount >= 50) {
			amount = 50;
		}

		if (dline.length < 2) {
			data = "";
		} else {
			data = dline[1];
		}

		if (data.equalsIgnoreCase("")) {
			data = "0";
		}

		String dirLine = this.getLines(event)[3];

		if (dirLine.equals("") || (dirLine == null)) {
			spawnDir = Direction.UP;
		} else {
			spawnDir = Direction.fromString(dirLine.toUpperCase());
			if ((spawnDir == Direction.ERROR) || (spawnDir == null)) {
				spawnDir = Direction.UP;
			}
		}

		if (!reload) {
			this.clearArgLines();
		}

		String ncline = creature.getTypeId() + "";

		if (!reload) {
			this.setLine(1, ncline, event);
			this.setLine(2, (amount + " " + data).substring(0, Math.min(16, (amount + " " + data).length())), event);
			this.setLine(3, spawnDir.toString(), event);
		}

		main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
		if (!reload) {
			this.init("Spawn sign accepted.");
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