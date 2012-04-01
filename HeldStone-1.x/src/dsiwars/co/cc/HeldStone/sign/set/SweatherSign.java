package dsiwars.co.cc.HeldStone.sign.set;

import org.bukkit.World;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class SweatherSign extends HeldSign {

	private boolean lastState = false;

	protected void triggersign(TriggerType type, Object args) {
		InputState is = this.getInput(1, (BlockRedstoneEvent) args);

		if (is != InputState.HIGH) {
			lastState = false;
			return;
		} else {
			if (lastState == true) {
				return;
			}
			lastState = true;
		}

		World w = this.getWorld();
		switch (this.type) {
		case SUNNY:
		case CLEAR:
			w.setStorm(false);
		break;
		case RAINY:
		case SNOWY:
		case STORMY:
			w.setStorm(true);
		break;
		}
	}

	@Override
	protected void setNBTData(NBTBase tag) {
	}

	@Override
	public NBTBase getNBTData() {
		return new NBTTagInt(0);
	}

	private static enum WeatherType {

		SUNNY, CLEAR, RAINY, SNOWY, STORMY
	}

	private WeatherType type = WeatherType.STORMY;

	protected boolean declare(boolean reload, SignChangeEvent event) {
		String typeLine = this.getLines(event)[1];

		for (WeatherType t : WeatherType.values()) {
			if (t.name().toUpperCase().equals(typeLine.toUpperCase())) {
				type = t;
			}
		}

		if (!reload) {
			this.clearArgLines();
			this.setLine(1, type.name(), event);
		}

		main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
		if (!reload) {
			this.init("cweather sign accepted.");
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