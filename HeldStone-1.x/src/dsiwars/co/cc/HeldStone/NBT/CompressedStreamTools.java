
package dsiwars.co.cc.HeldStone.NBT;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressedStreamTools {

    public static NBTTagCompound load(InputStream InputStream) {
        try {
            DataInputStream GZIPInputStream = new DataInputStream(new GZIPInputStream(InputStream));
            try {
                NBTTagCompound localNBTTagCompound = loadStructure(GZIPInputStream);
                return localNBTTagCompound;
            }
            finally {
                GZIPInputStream.close();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void save(NBTTagCompound Compound, OutputStream OutputStream) {
        try {
            DataOutputStream GZIPOutputStream = new DataOutputStream(new GZIPOutputStream(OutputStream));
            try {
                saveStructure(Compound, GZIPOutputStream);
            }
            finally {
                GZIPOutputStream.close();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static NBTTagCompound loadStructure(DataInput DataInput) {
        try {
            NBTBase Base = NBTBase.loadStructure(DataInput);
            if ((Base instanceof NBTTagCompound)) {
                return (NBTTagCompound) Base;
            }
            throw new IOException("Root tag must be a named compound tag");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void saveStructure(NBTTagCompound compound, DataOutput DataOutput) {
        NBTBase.saveStructure(compound, DataOutput);
    }
}
