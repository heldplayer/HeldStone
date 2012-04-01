package dsiwars.co.cc.HeldStone.NBT;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NBTTagString extends NBTBase {

	public String value;

	public NBTTagString() {
	}

	public NBTTagString(String value) {
		this.value = value;
		if (value == null) {
			throw new IllegalArgumentException("Empty string not allowed");
		}
	}

	void save(DataOutput Output) {
		try {
			Output.writeUTF(this.value);
		} catch (IOException ex) {
			Logger.getLogger(NBTTagString.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	void load(DataInput Input) {
		try {
			this.value = Input.readUTF();
		} catch (IOException ex) {
			Logger.getLogger(NBTTagString.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public byte getTypeID() {
		return 8;
	}

	@Override
	public String toString() {
		return "" + this.value;
	}
}