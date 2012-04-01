package me.heldplayer.HeldStone;

public abstract class ScheduledAction {
	public int delay;
	private int counted;
	public boolean repetitive;

	public ScheduledAction(int delay, boolean repeating) {
		this.delay = delay;
		this.counted = 0;
		this.repetitive = repeating;
	}

	public boolean tick(HeldStone plugin) {
		counted++;

		if (counted >= delay) {
			trigger(plugin);

			if (repetitive) {
				counted = 0;

				return false;
			}
			return true;
		}

		return false;
	}

	public abstract void trigger(HeldStone main);

	public abstract void cancel();
}
