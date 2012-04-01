package me.heldplayer.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;

public class Functions {
	public static void sendMessage(CommandSender target, String message, String color) {
		message = message.replaceAll("" + ChatColor.RESET, color);

		if (target == null) {
			Bukkit.getServer().broadcastMessage(color + "[HS] " + message);
		} else {
			target.sendMessage(color + "[HS] " + message);
		}
	}

	public static String combine(String[] args, int from) {
		String out = "";
		for (int i = from; i < args.length; i++) {
			if (i != from) {
				out += " ";
			}
			out += args[i];
		}
		return out;
	}

	public static String combinate(String[] array, char delimiter) {
		String result = "";
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				result += delimiter;
			}

			result += array[i].replaceAll(("" + delimiter), "");
		}
		return result;
	}

	public static String combinate(String[] array, char delimiter, String defaultOnFail) {
		String result = "";
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				result += delimiter;
			}

			try {
				result += array[i].replaceAll(("" + delimiter), "");
			} catch (Exception ex) {
				result += defaultOnFail;
			}
		}
		return result;
	}

	public static String[] decombinate(String string, char delimiter) {
		String[] result = new String[count(string, delimiter) + 1];
		int pos = 0;

		for (int i = 0; i < result.length; i++) {
			result[i] = "";
		}

		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == delimiter) {
				pos++;
			} else {
				result[pos] += string.charAt(i);
			}
		}
		return result;
	}

	public static int count(String source, char toCount) {
		int count = 0;
		for (int i = 0; i < source.length(); i++) {
			if (source.charAt(i) == toCount) {
				count++;
			}
		}
		return count;
	}

	public static String[] formatLines(String[] lines) {
		String[] newLines = new String[4];
		for (int i = 0; i < 4; i++) {
			if (lines.length > i) {
				newLines[i] = org.bukkit.ChatColor.stripColor(lines[i]);
			} else {
				newLines[i] = "";
			}
		}
		return newLines;
	}

	public static void drawCuboid(int materialID, World world, int x1, int y1, int z1, int x2, int y2, int z2) {
		drawCuboid(materialID, (byte) 0, world, x1, y1, z1, x2, y2, z2);
	}

	public static void drawCuboid(int materialID, byte data, World world, int x1, int y1, int z1, int x2, int y2, int z2) {
		drawCuboid(materialID, data, world, x1, y1, z1, x2, y2, z2, false);
	}

	public static void drawCuboid(int materialID, byte data, World world, int x1, int y1, int z1, int x2, int y2, int z2, boolean makeEffect) {
		if (x1 > x2) {
			int x3 = x1;
			x1 = x2;
			x2 = x3;
		}
		if (y1 > y2) {
			int y3 = y1;
			y1 = y2;
			y2 = y3;
		}
		if (z1 > z2) {
			int z3 = z1;
			z1 = z2;
			z2 = z3;
		}
		Block b;
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				for (int z = z1; z <= z2; z++) {
					b = world.getBlockAt(x, y, z);
					if (b.getTypeId() != materialID || b.getData() != data) {
						if (materialID == -1) {
							if (data == -1) {
							} else {
								if (makeEffect) {
									b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getTypeId(), b.getData());
								}

								b.setTypeIdAndData(b.getTypeId(), data, false);
							}
						} else {
							if (data == -1) {
								if (makeEffect) {
									b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getTypeId(), b.getData());
								}

								b.setTypeIdAndData(materialID, b.getData(), false);
							} else {
								if (makeEffect) {
									b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getTypeId(), b.getData());
								}

								b.setTypeIdAndData(materialID, data, false);
							}
						}
					}
				}
			}
		}
	}

	public static void drawSafeCuboid(int materialID, World world, int x1, int y1, int z1, int x2, int y2, int z2) {
		drawSafeCuboid(materialID, (byte) 0, world, x1, y1, z1, x2, y2, z2);
	}

	public static void drawSafeCuboid(int materialID, byte data, World world, int x1, int y1, int z1, int x2, int y2, int z2) {
		drawSafeCuboid(materialID, data, world, x1, y1, z1, x2, y2, z2, false);
	}

	public static void drawSafeCuboid(int materialID, byte data, World world, int x1, int y1, int z1, int x2, int y2, int z2, boolean makeEffect) {
		if (x1 > x2) {
			int x3 = x1;
			x1 = x2;
			x2 = x3;
		}
		if (y1 > y2) {
			int y3 = y1;
			y1 = y2;
			y2 = y3;
		}
		if (z1 > z2) {
			int z3 = z1;
			z1 = z2;
			z2 = z3;
		}
		Block b;
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				for (int z = z1; z <= z2; z++) {
					b = world.getBlockAt(x, y, z);
					if (b.getTypeId() != materialID || b.getData() != data) {
						if (isSafeToRemove(b.getTypeId())) {
							if (materialID == -1) {
								if (data == -1) {
								} else {
									if (makeEffect) {
										b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getTypeId(), b.getData());
									}

									b.setTypeIdAndData(b.getTypeId(), data, false);
								}
							} else {
								if (data == -1) {
									if (makeEffect) {
										b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getTypeId(), b.getData());
									}

									b.setTypeIdAndData(materialID, b.getData(), false);
								} else {
									if (makeEffect) {
										b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getTypeId(), b.getData());
									}

									b.setTypeIdAndData(materialID, data, false);
								}
							}
						}
					}
				}
			}
		}
	}

	public static boolean isSafeToRemove(int blockID) {
		switch (blockID) {
		case 6: // Sapling
		case 12: // Sand
		case 13: // Gravel
		case 23: // Dispenser
		case 25: // Note block
		case 26: // bed block
		case 27: // Powered rail
		case 28: // Detector rail
		case 31: // Tall grass
		case 32: // Dead bush
		case 36: // Block moved by piston
		case 37: // Dandelion
		case 38: // Rose
		case 39: // Brown mushroom
		case 40: // Red mushroom
		case 46: // TNT
		case 50: // Torch
		case 51: // Fire
		case 52: // Monster spawner
		case 54: // Chest
		case 55: // Redstone wire
		case 59: // Wheat block
		case 61: // Furnace
		case 62: // Burning furnace
		case 63: // Sign post
		case 64: // Wood door
		case 65: // Ladders
		case 66: // Rails
		case 68: // Wall sign
		case 69: // Lever
		case 70: // Stone pressure plate
		case 71: // Iron door
		case 72: // Wood pressure plate
		case 75: // Redstone torch off
		case 76: // Redstone torch on
		case 77: // Stone button
		case 78: // Snow cover
		case 83: // Sugar cane block
		case 84: // Jukebox
		case 92: // Cake block
		case 93: // Redstone repeater off
		case 94: // Redstone repeater on
		case 96: // Trapdoor
		case 104: // Pumpkin stem
		case 105: // Melon stem
		case 111: // Lily pad
		case 115: // Nether wart
		case 116: // Enchantment table
		case 117: // Brewing stand
		case 118: // Cauldron
		case 119: // End portal
		case 120: // End portal frame
		case 122: // Dragon egg
			return false;
		}

		return true;
	}

	public static EntityType getCreature(Entity e) {
		if (e instanceof Blaze) {
			return EntityType.BLAZE;
		}
		if (e instanceof CaveSpider) {
			return EntityType.CAVE_SPIDER;
		}
		if (e instanceof Chicken) {
			return EntityType.CHICKEN;
		}
		if (e instanceof Cow) {
			return EntityType.COW;
		}
		if (e instanceof Creeper) {
			return EntityType.CREEPER;
		}
		if (e instanceof EnderDragon) {
			return EntityType.ENDER_DRAGON;
		}
		if (e instanceof Enderman) {
			return EntityType.ENDERMAN;
		}
		if (e instanceof Ghast) {
			return EntityType.GHAST;
		}
		if (e instanceof Giant) {
			return EntityType.GIANT;
		}
		if (e instanceof IronGolem) {
			return EntityType.IRON_GOLEM;
		}
		if (e instanceof MagmaCube) {
			return EntityType.MAGMA_CUBE;
		}
		if (e instanceof MushroomCow) {
			return EntityType.MUSHROOM_COW;
		}
		if (e instanceof Ocelot) {
			return EntityType.OCELOT;
		}
		if (e instanceof Pig) {
			return EntityType.PIG;
		}
		if (e instanceof PigZombie) {
			return EntityType.PIG_ZOMBIE;
		}
		if (e instanceof Sheep) {
			return EntityType.SHEEP;
		}
		if (e instanceof Silverfish) {
			return EntityType.SILVERFISH;
		}
		if (e instanceof Skeleton) {
			return EntityType.SKELETON;
		}
		if (e instanceof Slime) {
			return EntityType.SLIME;
		}
		if (e instanceof Snowman) {
			return EntityType.SNOWMAN;
		}
		if (e instanceof Spider) {
			return EntityType.SPIDER;
		}
		if (e instanceof Squid) {
			return EntityType.SQUID;
		}
		if (e instanceof Villager) {
			return EntityType.VILLAGER;
		}
		if (e instanceof Wolf) {
			return EntityType.WOLF;
		}
		if (e instanceof Zombie) {
			return EntityType.ZOMBIE;
		}
		return null;
	}

	public static EntityType getCreature(String str) {
		if (str.equalsIgnoreCase("BLAZE")) {
			return EntityType.BLAZE;
		}
		if (str.equalsIgnoreCase("CAVE_SPIDER")) {
			return EntityType.CAVE_SPIDER;
		}
		if (str.equalsIgnoreCase("CAVESPIDER")) {
			return EntityType.CAVE_SPIDER;
		}
		if (str.equalsIgnoreCase("CHICKEN")) {
			return EntityType.CHICKEN;
		}
		if (str.equalsIgnoreCase("COW")) {
			return EntityType.COW;
		}
		if (str.equalsIgnoreCase("CREEPER")) {
			return EntityType.CREEPER;
		}
		if (str.equalsIgnoreCase("ENDER_DRAGON")) {
			return EntityType.ENDER_DRAGON;
		}
		if (str.equalsIgnoreCase("ENDERDRAGON")) {
			return EntityType.ENDER_DRAGON;
		}
		if (str.equalsIgnoreCase("ENDERMAN")) {
			return EntityType.ENDERMAN;
		}
		if (str.equalsIgnoreCase("GHAST")) {
			return EntityType.GHAST;
		}
		if (str.equalsIgnoreCase("GIANT")) {
			return EntityType.GIANT;
		}
		if (str.equalsIgnoreCase("IRON_GOLEM")) {
			return EntityType.IRON_GOLEM;
		}
		if (str.equalsIgnoreCase("MAGMA_CUBE")) {
			return EntityType.MAGMA_CUBE;
		}
		if (str.equalsIgnoreCase("MAGMACUBE")) {
			return EntityType.MAGMA_CUBE;
		}
		if (str.equalsIgnoreCase("MUSHROOM_COW")) {
			return EntityType.MUSHROOM_COW;
		}
		if (str.equalsIgnoreCase("MUSHROOMCOW")) {
			return EntityType.MUSHROOM_COW;
		}
		if (str.equalsIgnoreCase("MOOSHROOM")) {
			return EntityType.MUSHROOM_COW;
		}
		if (str.equalsIgnoreCase("OCELOT")) {
			return EntityType.OCELOT;
		}
		if (str.equalsIgnoreCase("PIG")) {
			return EntityType.PIG;
		}
		if (str.equalsIgnoreCase("PIG_ZOMBIE")) {
			return EntityType.PIG_ZOMBIE;
		}
		if (str.equalsIgnoreCase("PIGZOMBIE")) {
			return EntityType.PIG_ZOMBIE;
		}
		if (str.equalsIgnoreCase("SHEEP")) {
			return EntityType.SHEEP;
		}
		if (str.equalsIgnoreCase("SILVERFISH")) {
			return EntityType.SILVERFISH;
		}
		if (str.equalsIgnoreCase("SKELETON")) {
			return EntityType.SKELETON;
		}
		if (str.equalsIgnoreCase("SLIME")) {
			return EntityType.SLIME;
		}
		if (str.equalsIgnoreCase("SNOWMAN")) {
			return EntityType.SNOWMAN;
		}
		if (str.equalsIgnoreCase("SPIDER")) {
			return EntityType.SPIDER;
		}
		if (str.equalsIgnoreCase("SQUID")) {
			return EntityType.SQUID;
		}
		if (str.equalsIgnoreCase("VILLAGER")) {
			return EntityType.VILLAGER;
		}
		if (str.equalsIgnoreCase("WOLF")) {
			return EntityType.WOLF;
		}
		if (str.equalsIgnoreCase("ZOMBIE")) {
			return EntityType.ZOMBIE;
		}
		return null;
	}

	public static boolean validBiome(Biome biome) {
		switch (biome) {
		case OCEAN:
		case PLAINS:
		case DESERT:
		case EXTREME_HILLS:
		case FOREST:
		case TAIGA:
		case SWAMPLAND:
		case RIVER:
		case HELL:
		case SKY:
		case FROZEN_OCEAN:
		case FROZEN_RIVER:
		case ICE_PLAINS:
		case ICE_MOUNTAINS:
		case MUSHROOM_ISLAND:
		case MUSHROOM_SHORE:
		case BEACH:
		case DESERT_HILLS:
		case FOREST_HILLS:
		case TAIGA_HILLS:
		case SMALL_MOUNTAINS:
		case JUNGLE:
		case JUNGLE_HILLS:
			return true;
		}
		return false;
	}
}