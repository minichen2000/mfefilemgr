package com.mfe.qnmgr;

import com.mfe.qnmgr.constants.ConfigKey;
import com.mfe.qnmgr.exception.QnMgrException;

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

	public void loadConf(String confFile) throws QnMgrException {
		loadOneConfFile(confFile);
		loadReferenceConfFiles(new File(confFile).getParentFile());
	}

	public String getConf(String name) throws QnMgrException {
		String value = System.getProperty(name);
		if (null == value) {
			value = conf.getProperty(name);
			if (null == value)
				throw new QnMgrException(-1,
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

	public int getInt(String name) throws QnMgrException {
		String val = getConf(name);
		try {
			return Integer.parseInt(val);
		} catch (NumberFormatException e) {
			throw new QnMgrException(-1,
					"Illegal int format: '" + val + "' for: " + name, e);
		}
	}

	public int getInt(String name, int defaultValue) {
		try {
			return getInt(name);
		} catch (QnMgrException e) {
			return defaultValue;
		}
	}

	public boolean getBoolean(String name) throws QnMgrException {
		String value = System.getProperty(name);
		if (null == value) {
			value = conf.getProperty(name);
		}

		if ("TRUE".equalsIgnoreCase(value))
			return true;
		if ("FALSE".equalsIgnoreCase(value))
			return false;
		throw new QnMgrException(-1,
				"Illegal boolean format: '" + value + "' for: " + name);
	}

	public boolean getBoolean(String name, boolean defaultValue) {
		try {
			return getBoolean(name);
		} catch (QnMgrException e) {
			return defaultValue;
		}
	}

	private void loadOneConfFile(String file) throws QnMgrException {
		try {
			FileInputStream fin = new FileInputStream(file);
			conf.load(fin);
			fin.close();
		} catch (IOException e) {
			throw new QnMgrException(-1, e.getMessage(), e);
		}
	}

	private void loadReferenceConfFiles(File dir) throws QnMgrException {
		String referenceConfFiles = null;
		try {
			referenceConfFiles = getConf(ConfigKey.REFERENCE_CONF_FILES);
		} catch (QnMgrException e) {
			return;
		}
		String[] files = referenceConfFiles.split("\\s*,\\s*");
		for (String confFile : files) {
			loadOneConfFile(new File(dir, confFile).getAbsolutePath());
		}
	}
}