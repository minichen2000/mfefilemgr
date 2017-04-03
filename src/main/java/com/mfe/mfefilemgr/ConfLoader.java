package com.mfe.mfefilemgr;

import com.mfe.mfefilemgr.constants.ConfigKey;
import com.mfe.mfefilemgr.exception.MfeFileMgrException;
import com.mfe.mfefilemgr.restful.model.mfefilemgr.Provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfLoader {
	private Properties conf = new Properties();

	private static ConfLoader confLoader = new ConfLoader();

	private ConfLoader() {
	}

	public static ConfLoader getInstance() {
		return confLoader;
	}

	public void loadConf(String confFile) throws MfeFileMgrException {
		loadOneConfFile(confFile);
		loadReferenceConfFiles(new File(confFile).getParentFile());
	}

	public String getConf(String name) throws MfeFileMgrException {
		String value = System.getProperty(name);
		if (null == value) {
			value = conf.getProperty(name);
			if (null == value)
				throw new MfeFileMgrException(Provider.MFEFILEMGR, "-1",
						"No such configuration: '" + name + "'");
			return value.trim();
		} else {
			return value;
		}
	}

	public String getConf(String name, String defaultValue) {
		String value = System.getProperty(name);
		if (null == value) {
			return conf.getProperty(name, defaultValue);
		} else {
			return value;
		}
	}

	public boolean containsKey(String name) {
		return conf.containsKey(name);
	}

	public int getInt(String name) throws MfeFileMgrException {
		String val = getConf(name);
		try {
			return Integer.parseInt(val);
		} catch (NumberFormatException e) {
			throw new MfeFileMgrException(Provider.MFEFILEMGR, "-1",
					"Illegal int format: '" + val + "' for: " + name, e);
		}
	}

	public int getInt(String name, int defaultValue) {
		try {
			return getInt(name);
		} catch (MfeFileMgrException e) {
			return defaultValue;
		}
	}

	public boolean getBoolean(String name) throws MfeFileMgrException {
		String value = System.getProperty(name);
		if (null == value) {
			value = conf.getProperty(name);
		}

		if ("TRUE".equalsIgnoreCase(value))
			return true;
		if ("FALSE".equalsIgnoreCase(value))
			return false;
		throw new MfeFileMgrException(Provider.MFEFILEMGR, "-1",
				"Illegal boolean format: '" + value + "' for: " + name);
	}

	public boolean getBoolean(String name, boolean defaultValue) {
		try {
			return getBoolean(name);
		} catch (MfeFileMgrException e) {
			return defaultValue;
		}
	}

	private void loadOneConfFile(String file) throws MfeFileMgrException {
		try {
			FileInputStream fin = new FileInputStream(file);
			conf.load(fin);
			fin.close();
		} catch (IOException e) {
			throw new MfeFileMgrException(Provider.MFEFILEMGR, "-1", e.getMessage(), e);
		}
	}

	private void loadReferenceConfFiles(File dir) throws MfeFileMgrException {
		String referenceConfFiles = null;
		try {
			referenceConfFiles = getConf(ConfigKey.REFERENCE_CONF_FILES);
		} catch (MfeFileMgrException e) {
			return;
		}
		String[] files = referenceConfFiles.split("\\s*,\\s*");
		for (String confFile : files) {
			loadOneConfFile(new File(dir, confFile).getAbsolutePath());
		}
	}
}