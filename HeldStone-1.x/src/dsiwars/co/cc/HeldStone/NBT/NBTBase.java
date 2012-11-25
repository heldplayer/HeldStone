
package dsiwars.co.cc.HeldStone.NBT;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class NBTBase {

    private String name = null;

    abstract void save(DataOutput Output);

    abstract void load(DataInput Input);

    public abstract byte getTypeID();

    public String getName() {
        if (this.name == null) {
            return "";
        }
        return this.name;
    }

    public NBTBase setNameAndGet(String newName) {
        this.name = newName;
        return this;
    }

    public static NBTBase loadStructure(DataInput Input) {
        try {
            byte b = Input.readByte();
            if (b == 0) {
                return new NBTTagEnd();
            }
            NBTBase localNBTBase = getTagByID(b);
            localNBTBase.name = Input.readUTF();
            localNBTBase.load(Input);
            return localNBTBase;
        }
        catch (IOException ex) {
            Logger.getLogger(NBTBase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void saveStructure(NBTBase Tag, DataOutput Output) {
        try {
            Output.writeByte(Tag.getTypeID());
            if (Tag.getTypeID() == 0) {
                return;
            }
            Output.writeUTF(Tag.getName());
            Tag.save(Output);
        }
        catch (IOException ex) {
            Logger.getLogger(NBTBase.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
    }

    public static NBTBase getTagByID(byte ID) {
        switch (ID) {
        case 0:
            return new NBTTagEnd();
        case 1:
            return new NBTTagByte();
        case 2:
            return new NBTTagShort();
        case 3:
            return new NBTTagInt();
        case 4:
            return new NBTTagLong();
        case 5:
            return new NBTTagFloat();
        case 6:
            return new NBTTagDouble();
        case 7:
            return new NBTTagByteArray();
        case 8:
            return new NBTTagString();
        case 9:
            return new NBTTagList();
        case 10:
            return new NBTTagCompound();
        }
        return null;
    }

    public static String getNameByID(byte ID) {
        switch (ID) {
        case 0:
            return "TAG_End";
        case 1:
            return "TAG_Byte";
        case 2:
            return "TAG_Short";
        case 3:
            return "TAG_Int";
        case 4:
            return "TAG_Long";
        case 5:
            return "TAG_Float";
        case 6:
            return "TAG_Double";
        case 7:
            return "TAG_Byte_Array";
        case 8:
            return "TAG_String";
        case 9:
            return "TAG_List";
        case 10:
            return "TAG_Compound";
        }
        return "UNKNOWN";
    }
}
