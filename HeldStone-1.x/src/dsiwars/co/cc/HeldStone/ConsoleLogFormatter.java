package dsiwars.co.cc.HeldStone;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

final class ConsoleLogFormatter extends Formatter {

	private SimpleDateFormat dateFormat;
	private final String separator = System.getProperty("line.separator");

	ConsoleLogFormatter() {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public String format(LogRecord logrecord) {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append(dateFormat.format(Long.valueOf(logrecord.getMillis())));
		Level level = logrecord.getLevel();
		if (level == Level.FINEST) {
			stringbuilder.append(" [FINEST] ");
		} else if (level == Level.FINER) {
			stringbuilder.append(" [FINER] ");
		} else if (level == Level.FINE) {
			stringbuilder.append(" [FINE] ");
		} else if (level == Level.INFO) {
			stringbuilder.append(" [INFO] ");
		} else if (level == Level.WARNING) {
			stringbuilder.append(" [WARNING] ");
		} else if (level == Level.SEVERE) {
			stringbuilder.append(" [SEVERE] ");
		} else {
			stringbuilder.append((new StringBuilder()).append(" [").append(level.getLocalizedName()).append("] ").toString());
		}
		stringbuilder.append(logrecord.getMessage());
		stringbuilder.append(separator);
		Throwable throwable = logrecord.getThrown();
		if (throwable != null) {
			StringWriter stringwriter = new StringWriter();
			throwable.printStackTrace(new PrintWriter(stringwriter));
			stringbuilder.append(stringwriter.toString());
		}
		return stringbuilder.toString();
	}
}
