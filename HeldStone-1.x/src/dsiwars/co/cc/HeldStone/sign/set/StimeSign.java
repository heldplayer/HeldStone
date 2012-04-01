package dsiwars.co.cc.HeldStone.sign.set;

import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class StimeSign extends HeldSign {

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

		this.getWorld().setTime(time);

	}

	@Override
	protected void setNBTData(NBTBase tag) {
	}

	@Override
	public NBTBase getNBTData() {
		return new NBTTagInt(0);
	}

	int time;

	protected boolean declare(boolean reload, SignChangeEvent event) {
		int l1;

		String[] lines = this.getLines(event);

		if (lines[1].equals("")) {
			l1 = 0;
			time = l1;
		} else {
			l1 = fixTime(parseTime(lines[1]));
			time = l1;
		}

		if (!reload) {
			this.clearArgLines();
			this.setLine(1, "" + l1, event);
		}

		main.sgc.register(this, TriggerType.REDSTONE_CHANGE);

		if (!reload) {
			this.init("ctime sign accepted.");
		}

		this.triggersign(null, null);

		return true;
	}

	private int parseTime(String ts) {
		if (isInteger(ts)) {
			return Integer.parseInt(ts);
		} else {
			return 0;
		}
	}

	public boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private int fixTime(int time) {
		if (time > 24000) {
			time = 24000;
		}
		if (time < 0) {
			time = 0;
		}
		return time;
	}

	@Override
	public void invalidate() {
	}

	@Override
	public String getTriggerTypesString() {
		return TriggerType.REDSTONE_CHANGE.name();
	}
}