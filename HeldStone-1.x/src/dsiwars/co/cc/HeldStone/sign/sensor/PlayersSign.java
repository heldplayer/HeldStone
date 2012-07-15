package dsiwars.co.cc.HeldStone.sign.sensor;

import org.bukkit.ChatColor;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class PlayersSign extends HeldSign {

	private boolean isNew = true;
	private boolean lastState = false;

	@Override
	protected void triggersign(TriggerType type, Object args) {
		Integer loggedplayers = this.main.getServer().getOnlinePlayers().length;
		boolean enough = loggedplayers >= this.playerAmount ? true : false;

		if (this.lastState != enough || this.isNew) {
			this.isNew = false;
			this.lastState = enough;
			setOutput(enough);
		}
	}

	@Override
	protected void setNBTData(NBTBase tag) {
	}

	@Override
	public NBTBase getNBTData() {
		return new NBTTagInt(0);
	}

	private int playerAmount;

	@Override
	protected boolean declare(boolean reload, SignChangeEvent event) {
		Integer playerAmount = 10;

		try {
			playerAmount = Integer.parseInt(this.getLines(event)[1]);
		} catch (Exception ex) {
			this.main.alert(getOwnerName(), "Invalid player count", ChatColor.RED);
		}

		if (playerAmount <= 0) {
			playerAmount = 10;
		}

		if (!reload) {
			this.clearArgLines(event);
			this.setLine(1, playerAmount.toString(), event);
		}

		this.playerAmount = playerAmount;

		this.main.sgc.register(this, TriggerType.TIMER_SECOND);

		if (!reload) {
			init("Players sign accepted.");
		}

		return true;
	}

	@Override
	public void invalidate() {
	}

	@Override
	public String getTriggerTypesString() {
		return TriggerType.TIMER_SECOND.name();
	}
}