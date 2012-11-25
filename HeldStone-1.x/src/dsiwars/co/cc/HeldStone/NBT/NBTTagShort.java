
package dsiwars.co.cc.HeldStone.NBT;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NBTTagShort extends NBTBase {

    public short value;

    public NBTTagShort() {}

    public NBTTagShort(short value) {
        this.value = value;
    }

    @Override
    void save(DataOutput Output) {
        try {
            Output.writeShort(this.value);
        }
        catch (IOException ex) {
            Logger.getLogger(NBTTagShort.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    void load(DataInput Input) {
        try {
            this.value = Input.readShort();
        }
        catch (IOException ex) {
            Logger.getLogger(NBTTagShort.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public byte getTypeID() {
        return 2;
    }

    @Override
    public String toString() {
        return "" + this.value;
    }
}
