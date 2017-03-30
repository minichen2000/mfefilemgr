package com.mfe.qnmgr.constants;

public class ConfigKey {
	public static final String REFERENCE_CONF_FILES = "REFERENCE_CONF_FILES";

	public static final String RI_FILE_PATH = "RI_FILE_PATH";

	public static final String ADP_PING_INTERVAL = "ADP_PING_INTERVAL";

	public static final int DEFAULT_ADP_PING_INTERVAL = 60 * 1000;

	public static final String ADP_PING_MAX_NUM = "ADP_PING_MAX_NUM";

	public static final int DEFAULT_ADP_PING_MAX_NUM = 10;

	public static final String NOTIF_SERVER_PORT = "NOTIF_SERVER_PORT";

	public static final int DEFAULT_NOTIF_SERVER_PORT = 19000;

	public static final String CTRL_URL = "CTRL_URL";

	public static final String ADP_ID = "ADP_ID";

	public static final String ADP_IP = "ADP_IP";

	public static final String ADP_ADDRESS = "ADP_ADDRESS";

	public static final String QNMGR_PORT = "QNMGR_PORT";

	public static final String ADP_SNMP_TRAP_PORT = "ADP_SNMP_TRAP_PORT";

	public static final int DEFAULT_QNMGR_PORT = 80;

	public static final String REG_PERIOD = "REG_PERIOD";

	public static final int DEFAULT_REG_PERIOD = 60 * 1000;

	public static final int DEFAULT_SNMP_TRAP_PORT = 162;

	public static final String MAX_SYSUP_TIME = "MAX_SYSUP_TIME";

	public static final int DEFAULT_MAX_SYSUP_TIME = 50;

	public static final String MAX_CLEAN_TIME = "MAX_CLEAN_TIME";

	public static final int DEFAULT_MAX_CLEAN_TIME = 5 * 1000;

	public static final String NOTIF_CLEAN_PERIOD = "NOTIF_CLEAN_PERIOD";

	public static final int DEFAULT_NOTIF_CLEAN_PERIOD = 1 * 1000;

	public static final String DB_IP = "DB_IP";

	public static final String DEFAULT_DB_IP = "localhost";

	public static final String DB_PORT = "DB_PORT";

	public static final int DEFAULT_DB_PORT = 27017;

	public static final String DB_NAME = "DB_NAME";

	public static final String DEFAULT_DB_NAME = "eNMS";

	public static final String ADP_SETTER_TIMER_INTERVAL = "ADP_SETTER_TIMER_INTERVAL";

	public static final int DEFAULT_ADP_SETTER_TIMER_INTERVAL = 2 * 1000;

	public static final String ADP_SETTER_EXPIRED_INTERVAL = "ADP_SETTER_EXPIRED_INTERVAL";

	public static final int DEFAULT_ADP_SETTER_EXPIRED_INTERVAL = 2 * 1000;

	public static final String MAX_CLEAN_DEBOCUING_TIME = "MAX_CLEAN_DEBOCUING_TIME";

	public static final int DEFAULT_MAX_CLEAN_DEBOCUING_TIME = 500;

	public static final String CLEAN_DEBOCUING_PERIOD = "CLEAN_DEBOCUING_PERIOD";

	public static final int DEFAULT_CLEAN_DEBOCUING_PERIOD = 1 * 1000;

	public static final String MAX_NOTIF_SYSUP_SPACE_TIME = "MAX_NOTIF_SYSUP_SPACE_TIME";

	public static final int DEFAULT_MAX_NOTIF_SYSUP_SPACE_TIME = 50;
		
	public static final String MAX_SNMP_OIDS_PREFIX = "MAX_SNMP_OIDS_";
}