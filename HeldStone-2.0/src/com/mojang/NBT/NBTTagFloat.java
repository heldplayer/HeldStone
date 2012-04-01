package com.mojang.NBT;

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

	void save(DataOutput Output) {
		try {
			Output.writeFloat(this.value);
		} catch (IOException ex) {
			Logger.getLogger(NBTTagFloat.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	void load(DataInput Input) {
		try {
			this.value = Input.readFloat();
		} catch (IOException ex) {
			Logger.getLogger(NBTTagFloat.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public byte getTypeID() {
		return 5;
	}

	public String toString() {
		return "" + this.value;
	}
}