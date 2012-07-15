package dsiwars.co.cc.HeldStone.NBT;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NBTTagCompound extends NBTBase {

	private Map<String, NBTBase> values = new HashMap<String, NBTBase>();

	@Override
	void save(DataOutput Output) {
		try {
			Iterator<NBTBase> i = this.values.values().iterator();
			while (i.hasNext()) {
				NBTBase localNBTBase = i.next();
				NBTBase.saveStructure(localNBTBase, Output);
			}
			Output.writeByte(0);
		} catch (IOException ex) {
			Logger.getLogger(NBTTagCompound.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	void load(DataInput Input) {
		this.values.clear();
		NBTBase localNBTBase;
		while ((localNBTBase = NBTBase.loadStructure(Input)).getTypeID() != 0) {
			this.values.put(localNBTBase.getName(), localNBTBase);
		}
	}

	public Collection<NBTBase> getCollection() {
		return this.values.values();
	}

	@Override
	public byte getTypeID() {
		return 10;
	}

	public void insert(String name, NBTBase Tag) {
		this.values.put(name, Tag.setNameAndGet(name));
	}

	public void insert(String name, byte value) {
		this.values.put(name, new NBTTagByte(value).setNameAndGet(name));
	}

	public void insert(String name, short value) {
		this.values.put(name, new NBTTagShort(value).setNameAndGet(name));
	}

	public void insert(String name, int value) {
		this.values.put(name, new NBTTagInt(value).setNameAndGet(name));
	}

	public void insert(String name, long value) {
		this.values.put(name, new NBTTagLong(value).setNameAndGet(name));
	}

	public void insert(String name, float value) {
		this.values.put(name, new NBTTagFloat(value).setNameAndGet(name));
	}

	public void insert(String name, double value) {
		this.values.put(name, new NBTTagDouble(value).setNameAndGet(name));
	}

	public void insert(String name, String value) {
		this.values.put(name, new NBTTagString(value).setNameAndGet(name));
	}

	public void insert(String name, byte[] value) {
		this.values.put(name, new NBTTagByteArray(value).setNameAndGet(name));
	}

	public void insertCompound(String name, NBTTagCompound tag) {
		this.values.put(name, tag.setNameAndGet(name));
	}

	public void insert(String name, boolean value) {
		insert(name, value ? 1 : 0);
	}

	public boolean hasKey(String name) {
		return this.values.containsKey(name);
	}

	public byte getByte(String name) {
		if (!this.values.containsKey(name)) {
			return 0;
		}
		return ((NBTTagByte) this.values.get(name)).value;
	}

	public short getShort(String name) {
		if (!this.values.containsKey(name)) {
			return 0;
		}
		return ((NBTTagShort) this.values.get(name)).value;
	}

	public int getInt(String name) {
		if (!this.values.containsKey(name)) {
			return 0;
		}
		return ((NBTTagInt) this.values.get(name)).value;
	}

	public long getLong(String name) {
		if (!this.values.containsKey(name)) {
			return 0L;
		}
		return ((NBTTagLong) this.values.get(name)).value;
	}

	public float getFloat(String name) {
		if (!this.values.containsKey(name)) {
			return 0.0F;
		}
		return ((NBTTagFloat) this.values.get(name)).value;
	}

	public double getDouble(String name) {
		if (!this.values.containsKey(name)) {
			return 0.0D;
		}
		return ((NBTTagDouble) this.values.get(name)).value;
	}

	public String getString(String name) {
		if (!this.values.containsKey(name)) {
			return "";
		}
		return ((NBTTagString) this.values.get(name)).value;
	}

	public byte[] getByteArray(String name) {
		if (!this.values.containsKey(name)) {
			return new byte[0];
		}
		return ((NBTTagByteArray) this.values.get(name)).values;
	}

	public NBTTagCompound getCompound(String name) {
		if (!this.values.containsKey(name)) {
			return new NBTTagCompound();
		}
		return (NBTTagCompound) this.values.get(name);
	}

	public NBTTagList getList(String name) {
		if (!this.values.containsKey(name)) {
			return new NBTTagList();
		}
		return (NBTTagList) this.values.get(name);
	}

	public NBTBase getTag(String name) {
		if (!this.values.containsKey(name)) {
			return null;
		}
		return this.values.get(name);
	}

	public boolean getBoolean(String name) {
		return getInt(name) != 0;
	}

	@Override
	public String toString() {
		return "" + this.values.size() + " entries";
	}
}