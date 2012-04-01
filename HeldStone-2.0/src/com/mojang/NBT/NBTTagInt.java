package com.mojang.NBT;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NBTTagInt extends NBTBase {

	public int value;

	public NBTTagInt() {
	}

	public NBTTagInt(int value) {
		this.value = value;
	}

	void save(DataOutput Output) {
		try {
			Output.writeInt(this.value);
		} catch (IOException ex) {
			Logger.getLogger(NBTTagInt.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	void load(DataInput Input) {
		try {
			this.value = Input.readInt();
		} catch (IOException ex) {
			Logger.getLogger(NBTTagInt.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public byte getTypeID() {
		return 3;
	}

	public String toString() {
		return "" + this.value;
	}
}