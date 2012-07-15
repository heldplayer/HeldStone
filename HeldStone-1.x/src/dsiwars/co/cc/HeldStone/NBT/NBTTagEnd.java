package dsiwars.co.cc.HeldStone.NBT;

import java.io.DataInput;
import java.io.DataOutput;

public class NBTTagEnd extends NBTBase {

	@Override
	void save(DataOutput Output) {
	}

	@Override
	void load(DataInput Input) {
	}

	@Override
	public byte getTypeID() {
		return 0;
	}

	@Override
	public String toString() {
		return "END";
	}
}