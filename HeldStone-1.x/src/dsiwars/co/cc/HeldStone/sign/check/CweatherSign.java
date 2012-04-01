package dsiwars.co.cc.HeldStone.sign.check;

import org.bukkit.World;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class CweatherSign extends HeldSign {

	protected void triggersign(TriggerType type, Object args) {
		boolean state = false;
		World w = this.getWorld();
		switch (this.type) {
		case SUNNY:
		case CLEAR:
			state = !(w.hasStorm());
		break;
		case RAINY:
		case SNOWY:
		case STORMY:
			state = w.hasStorm();
		break;
		}
		this.setOutput(state);
	}

	boolean autoTrigger = false;

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

	private WeatherType type = WeatherType.SUNNY;

	protected boolean declare(boolean reload, SignChangeEvent event) {
		String typeLine = this.getLines(event)[1];
		String autoTriggerline = this.getLines(event)[2];

		for (WeatherType t : WeatherType.values()) {
			if (t.name().toUpperCase().equals(typeLine.toUpperCase())) {
				type = t;
			}
		}

		if (autoTriggerline.equalsIgnoreCase("FALSE")) {
			autoTriggerline = "FALSE";
			autoTrigger = false;
		} else {
			autoTriggerline = "TRUE";
			autoTrigger = true;
		}

		if (autoTrigger) {
			main.sgc.register(this, TriggerType.TIMER_SECOND);
		} else {
			main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
		}

		if (!reload) {
			this.setLine(1, type.name(), event);
			this.setLine(2, autoTriggerline, event);
		}

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
		if (autoTrigger) {
			return TriggerType.TIMER_SECOND.name();
		} else {
			return TriggerType.REDSTONE_CHANGE.name();
		}
	}
}