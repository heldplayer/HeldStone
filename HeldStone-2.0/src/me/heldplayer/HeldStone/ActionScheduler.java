package me.heldplayer.HeldStone;

import java.util.ArrayList;

public class ActionScheduler implements Runnable {
	private final HeldStone main;
	private final ArrayList<ScheduledAction> scheduledActions;

	public ActionScheduler(HeldStone plugin) {
		main = plugin;
		scheduledActions = new ArrayList<ScheduledAction>();
	}

	public void schedule(ScheduledAction action) {
		scheduledActions.add(action);
	}

	public void cancel(ScheduledAction action) {
		if (scheduledActions.contains(action)) {
			scheduledActions.remove(action);
		}

		action.cancel();
	}

	public void run() {
		ArrayList<ScheduledAction> copy = new ArrayList<ScheduledAction>(scheduledActions);

		for (ScheduledAction action : copy) {
			if (action.tick(main)) {
				scheduledActions.remove(action);
			}
		}
	}
}
