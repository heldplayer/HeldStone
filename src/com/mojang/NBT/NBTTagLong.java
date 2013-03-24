
package com.mojang.NBT;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NBTTagLong extends NBTBase {

    public long value;

    public NBTTagLong() {}

    public NBTTagLong(long value) {
        this.value = value;
    }

    void save(DataOutput Output) {
        try {
            Output.writeLong(this.value);
        }
        catch (IOException ex) {
            Logger.getLogger(NBTTagLong.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void load(DataInput Input) {
        try {
            this.value = Input.readLong();
        }
        catch (IOException ex) {
            Logger.getLogger(NBTTagLong.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte getTypeID() {
        return 4;
    }

    public String toString() {
        return "" + this.value;
    }
}
