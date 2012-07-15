package dsiwars.co.cc.HeldStone.NBT;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NBTTagDouble extends NBTBase {

	public double value;

	public NBTTagDouble() {
	}

	public NBTTagDouble(double value) {
		this.value = value;
	}

	@Override
	void save(DataOutput Output) {
		try {
			Output.writeDouble(this.value);
		} catch (IOException ex) {
			Logger.getLogger(NBTTagDouble.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	void load(DataInput Input) {
		try {
			this.value = Input.readDouble();
		} catch (IOException ex) {
			Logger.getLogger(NBTTagDouble.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public byte getTypeID() {
		return 6;
	}

	@Override
	public String toString() {
		return "" + this.value;
	}
}