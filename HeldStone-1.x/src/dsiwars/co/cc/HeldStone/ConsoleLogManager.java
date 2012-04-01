package dsiwars.co.cc.HeldStone;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

public class ConsoleLogManager {

	public static void init() {
		ConsoleLogFormatter consolelogformatter = new ConsoleLogFormatter();
		try {
			FileHandler filehandler = new FileHandler("plugins/HeldStone/log.txt", true);
			filehandler.setFormatter(consolelogformatter);
			Handler[] handlers = HeldStone.log.getHandlers();
			for (Handler handler : handlers) {
				if (handler instanceof FileHandler) {
					if (handler.equals(filehandler)) {
						return;
					}
				}
			}
			HeldStone.log.addHandler(filehandler);
		} catch (Exception exception) {
			HeldStone.log.log(Level.WARNING, "Failed to log to log.txt", exception);
		}
	}
}