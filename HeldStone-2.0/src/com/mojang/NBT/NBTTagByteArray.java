package com.mojang.NBT;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NBTTagByteArray extends NBTBase {

	public byte[] values;

	public NBTTagByteArray() {
	}

	public NBTTagByteArray(byte[] values) {
		this.values = values;
	}

	void save(DataOutput Output) {
		try {
			Output.writeInt(this.values.length);
			Output.write(this.values);
		} catch (IOException ex) {
			Logger.getLogger(NBTTagByteArray.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	void load(DataInput Input) {
		try {
			int i = Input.readInt();
			this.values = new byte[i];
			Input.readFully(this.values);
		} catch (IOException ex) {
			Logger.getLogger(NBTTagByteArray.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public byte getTypeID() {
		return 7;
	}

	public String toString() {
		return "[" + this.values.length + " bytes]";
	}
}