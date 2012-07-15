package dsiwars.co.cc.HeldStone.NBT;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NBTTagFloat extends NBTBase {

	public float value;

	public NBTTagFloat() {
	}

	public NBTTagFloat(float value) {
		this.value = value;
	}

	@Override
	void save(DataOutput Output) {
		try {
			Output.writeFloat(this.value);
		} catch (IOException ex) {
			Logger.getLogger(NBTTagFloat.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	void load(DataInput Input) {
		try {
			this.value = Input.readFloat();
		} catch (IOException ex) {
			Logger.getLogger(NBTTagFloat.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public byte getTypeID() {
		return 5;
	}

	@Override
	public String toString() {
		return "" + this.value;
	}
}