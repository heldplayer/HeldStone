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

	protected void triggersign(TriggerType type, Object args) {
		Integer loggedplayers = main.getServer().getOnlinePlayers().length;
		boolean enough = loggedplayers >= playerAmount ? true : false;

		if (lastState != enough || isNew) {
			isNew = false;
			lastState = enough;
			this.setOutput(enough);
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

	protected boolean declare(boolean reload, SignChangeEvent event) {
		Integer playerAmount = 10;

		try {
			playerAmount = Integer.parseInt(this.getLines(event)[1]);
		} catch (Exception ex) {
			main.alert(this.getOwnerName(), "Invalid player count", ChatColor.RED);
		}

		if (playerAmount <= 0) {
			playerAmount = 10;
		}

		if (!reload) {
			this.clearArgLines(event);
			this.setLine(1, playerAmount.toString(), event);
		}

		this.playerAmount = playerAmount;

		main.sgc.register(this, TriggerType.TIMER_SECOND);

		if (!reload) {
			this.init("Players sign accepted.");
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