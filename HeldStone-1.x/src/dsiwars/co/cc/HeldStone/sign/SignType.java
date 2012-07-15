package dsiwars.co.cc.HeldStone.sign;

public enum SignType {

	// Chat
	ANNOUNCE("chat"),
	DISP("chat"),
	GlANNOUNCE("chat"),
	// Check
	CTIME("check"),
	CWEATHER("check"),
	// Functional
	AreaToggle("functional"),
	BLACKHOLE("functional"),
	BOLT("functional"),
	CAULDRON("functional"),
	EFFECT("functional"),
	// Logic
	CLOCK("logic"),
	DELAY("logic"),
	LOGIC("logic"),
	RAND("logic"),
	TOGGLE("logic"),
	TRIGGER("logic"),
	// Minecart
	SPEED("speed"),
	// Sensor
	ISENSOR("sensor"),
	LaSENSOR("sensor"),
	LiSENSOR("sensor"),
	LOGGED("sensor"),
	MSENSOR("sensor"),
	PLAYERS("sensor"),
	PSENSOR("sensor"),
	SENSOR("sensor"),
	WaSENSOR("sensor"),
	// Set
	STIME("set"),
	SWEATHER("set"),
	// Spawn
	CUBOID("spawn"),
	ITEM("spawn"),
	SPAWN("spawn"),
	// Wireless
	SEND("wireless"),
	RECV("wireless");
	public final String groupName;

	private SignType(String groupName) {
		this.groupName = groupName;
	}
}