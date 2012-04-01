package dsiwars.co.cc.HeldStone.NBT;

import java.io.DataInput;
import java.io.DataOutput;

public class NBTTagEnd extends NBTBase {

	void save(DataOutput Output) {
	}

	void load(DataInput Input) {
	}

	public byte getTypeID() {
		return 0;
	}

	@Override
	public String toString() {
		return "END";
	}
}