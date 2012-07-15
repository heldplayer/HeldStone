package dsiwars.co.cc.HeldStone;

/*
 * This code is Copyright (C) 2011 Chris Bode, Some Rights Reserved.
 * Copyright (C) 1999-2002 Technical Pursuit Inc., All Rights Reserved. Patent
 * Pending, Technical Pursuit Inc.
 * Unless explicitly acquired and licensed from Licensor under the Technical
 * Pursuit License ("TPL") Version 1.0 or greater, the contents of this file are
 * subject to the Reciprocal Public License ("RPL") Version 1.1, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in
 * either source code or executable form, except in compliance with the terms
 * and conditions of the RPL.
 * You may obtain a copy of both the TPL and the RPL (the "Licenses") from
 * Technical Pursuit Inc. at http://www.technicalpursuit.com.
 * All software distributed under the Licenses is provided strictly on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND
 * TECHNICAL PURSUIT INC. HEREBY DISCLAIMS ALL SUCH WARRANTIES, INCLUDING
 * WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the Licenses
 * for specific language governing rights and limitations under the Licenses.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ConfigFile {

	private final HeldStone main;
	private File cfg;
	ArrayList<ConfigKey> keys = new ArrayList<ConfigKey>();
	private final String separator = System.getProperty("line.separator");

	public ConfigFile(HeldStone main, File cfg) {
		this.main = main;
		this.cfg = cfg;
	}

	public void load() {
		this.keys.clear();

		if (this.cfg.isDirectory()) {
			this.cfg = new File(this.cfg.getAbsolutePath() + "config.txt");
		}

		if (!this.cfg.exists()) {
			try {
				this.cfg.createNewFile();
			} catch (IOException e) {
				this.main.e("Error while creating config file.");
				this.main.e("File path: " + this.cfg.getAbsolutePath());
				e.printStackTrace();
			}
		} else {
			try {
				BufferedReader in = new BufferedReader(new FileReader(this.cfg));
				String line;
				while ((line = in.readLine()) != null) {
					line = line.trim();
					if (line.charAt(0) != '#') {
						if (line.contains("=")) {
							String[] args = line.split("=");
							String key = args[0].trim();
							String value = args[1].trim();
							this.keys.add(new ConfigKey(key, value));
						}
					}
				}
			} catch (Exception e) {
				this.main.e("Error while updating config file.");
				e.printStackTrace();
			}
		}
	}

	public void save() {
		boolean changes = false;
		for (int i = 0; i < this.keys.size(); i++) {
			if (this.keys.get(i).isChanged()) {
				changes = true;
			}
		}
		if (changes) {
			try {
				PrintWriter out = new PrintWriter(new FileOutputStream(this.cfg));
				for (int i = 0; i < this.keys.size(); i++) {
					String line = "";
					ConfigKey cc = this.keys.get(i);
					line += cc.key;
					line += " = ";
					line += cc.value;
					out.write(line + this.separator);
				}
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
				this.main.e("Error while updating config file.");
			}
		}
	}

	public void announce() {
		for (int i = 0; i < this.keys.size(); i++) {
			ConfigKey k = this.keys.get(i);
			this.main.d(k.getKey() + " is set to '" + k.getValue() + "'");
		}
	}

	public String getString(String key, String defaultValue) {
		for (int i = 0; i < this.keys.size(); i++) {
			ConfigKey k = this.keys.get(i);
			if (key.equals(k.getKey())) {
				return k.getValue();
			}
		}
		ConfigKey nk = new ConfigKey(key, defaultValue);
		nk.dirty();
		this.keys.add(nk);
		return nk.getValue();
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		String val = getString(key, Boolean.toString(defaultValue));
		return Boolean.parseBoolean(val);
	}

	public int getInt(String key, int defaultValue) {
		String val = getString(key, Integer.toString(defaultValue));
		return Integer.parseInt(val);
	}

	private static class ConfigKey {

		private final String key;
		private String value;
		private boolean changed;

		public ConfigKey(String key, String value) {
			this.key = key;
			this.value = value;
			this.changed = false;
		}

		public void dirty() {
			this.changed = true;
		}

		public String getValue() {
			return this.value;
		}

		public String getKey() {
			return this.key;
		}

		public boolean isChanged() {
			return this.changed;
		}
	}
}