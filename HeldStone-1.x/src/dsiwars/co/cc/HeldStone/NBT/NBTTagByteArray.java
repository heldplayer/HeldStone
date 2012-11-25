
package dsiwars.co.cc.HeldStone.NBT;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NBTTagByteArray extends NBTBase {

    public byte[] values;

    public NBTTagByteArray() {}

    public NBTTagByteArray(byte[] values) {
        this.values = values;
    }

    @Override
    void save(DataOutput Output) {
        try {
            Output.writeInt(this.values.length);
            Output.write(this.values);
        }
        catch (IOException ex) {
            Logger.getLogger(NBTTagByteArray.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    void load(DataInput Input) {
        try {
            int i = Input.readInt();
            this.values = new byte[i];
            Input.readFully(this.values);
        }
        catch (IOException ex) {
            Logger.getLogger(NBTTagByteArray.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public byte getTypeID() {
        return 7;
    }

    @Override
    public String toString() {
        return "[" + this.values.length + " bytes]";
    }
}
