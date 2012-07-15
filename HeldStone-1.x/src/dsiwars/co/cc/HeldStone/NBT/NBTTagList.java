package dsiwars.co.cc.HeldStone.NBT;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NBTTagList extends NBTBase {

	private List<NBTBase> values = new ArrayList<NBTBase>();
	private byte type;

	@Override
	void save(DataOutput Output) {
		try {
			if (this.values.size() > 0) {
				this.type = this.values.get(0).getTypeID();
			} else {
				this.type = 1;
			}
			Output.writeByte(this.type);
			Output.writeInt(this.values.size());
			for (int i = 0; i < this.values.size(); i++) {
				this.values.get(i).save(Output);
			}
		} catch (IOException ex) {
			Logger.getLogger(NBTTagList.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	void load(DataInput Input) {
		try {
			this.type = Input.readByte();
			int i = Input.readInt();
			this.values = new ArrayList<NBTBase>();
			for (int j = 0; j < i; j++) {
				NBTBase localNBTBase = NBTBase.getTagByID(this.type);
				localNBTBase.load(Input);
				this.values.add(localNBTBase);
			}
		} catch (IOException ex) {
			Logger.getLogger(NBTTagList.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public byte getTypeID() {
		return 9;
	}

	@Override
	public String toString() {
		return "" + this.values.size() + " entries of type " + NBTBase.getNameByID(this.type);
	}

	public void insert(NBTBase value) {
		this.type = value.getTypeID();
		this.values.add(value);
	}

	public NBTBase get(int number) {
		return this.values.get(number);
	}

	public int size() {
		return this.values.size();
	}
}