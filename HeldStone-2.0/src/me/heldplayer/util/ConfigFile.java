
package me.heldplayer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import me.heldplayer.HeldStone.HeldStone;

public class ConfigFile {
    private File dataFolder;
    ArrayList<ConfigKey> keys = new ArrayList<ConfigKey>();
    private final String separator = System.getProperty("line.separator");

    public ConfigFile(File cfg) {
        this.dataFolder = cfg;
    }

    public void load() {
        keys.clear();

        if (dataFolder.isDirectory()) {
            dataFolder = new File(dataFolder.getAbsolutePath() + "config.txt");
        }

        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            }
            catch (IOException e) {
                HeldStone.warning("Error while creating config file.");
                HeldStone.warning("File path: " + dataFolder.getAbsolutePath());
                e.printStackTrace();
            }
        }
        else {
            BufferedReader in = null;
            try {
                in = new BufferedReader(new FileReader(dataFolder));
                String line;
                while ((line = in.readLine()) != null) {
                    line = line.trim();
                    if (line.charAt(0) != '#') {
                        if (line.contains("=")) {
                            String[] args = line.split("=");
                            String key = args[0].trim();
                            String value = args[1].trim();
                            keys.add(new ConfigKey(key, value));
                        }
                    }
                }
            }
            catch (Exception e) {
                HeldStone.warning("Error while updating config file.");
                e.printStackTrace();
            }
            finally {
                try {
                    in.close();
                }
                catch (IOException e) {}
            }
        }
    }

    public void save() {
        boolean changes = false;
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).isChanged()) {
                changes = true;
            }
        }
        if (changes) {
            try {
                PrintWriter out = new PrintWriter(new FileOutputStream(dataFolder));
                for (int i = 0; i < keys.size(); i++) {
                    String line = "";
                    ConfigKey cc = keys.get(i);
                    line += cc.key;
                    line += " = ";
                    line += cc.value;
                    out.write(line + separator);
                }
                out.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                HeldStone.warning("Error while updating config file.");
            }
        }
    }

    public String getString(String key, String defaultValue) {
        for (int i = 0; i < keys.size(); i++) {
            ConfigKey k = keys.get(i);
            if (key.equals(k.getKey())) {
                return k.getValue();
            }
        }
        ConfigKey nk = new ConfigKey(key, defaultValue);
        nk.dirty();
        keys.add(nk);
        return nk.getValue();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String val = this.getString(key, Boolean.toString(defaultValue));
        return Boolean.parseBoolean(val);
    }

    public int getInt(String key, int defaultValue) {
        String val = this.getString(key, Integer.toString(defaultValue));
        return Integer.parseInt(val);
    }

    private static class ConfigKey {

        private final String key;
        private String value;
        private boolean changed;

        public ConfigKey(String key, String value) {
            this.key = key;
            this.value = value;
            changed = false;
        }

        public void dirty() {
            changed = true;
        }

        public String getValue() {
            return value;
        }

        public String getKey() {
            return key;
        }

        public boolean isChanged() {
            return changed;
        }
    }
}
