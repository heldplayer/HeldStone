package dsiwars.co.cc.HeldStone.NBT;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NBTTagByte extends NBTBase {

	public byte value;

	public NBTTagByte() {
	}

	public NBTTagByte(byte value) {
		this.value = value;
	}

	@Override
	void save(DataOutput Output) {
		try {
			Output.writeByte(this.value);
		} catch (IOException ex) {
			Logger.getLogger(NBTTagByte.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	void load(DataInput Input) {
		try {
			this.value = Input.readByte();
		} catch (IOException ex) {
			Logger.getLogger(NBTTagByte.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public byte getTypeID() {
		return 1;
	}

	@Override
	public String toString() {
		return "" + this.value;
	}
}