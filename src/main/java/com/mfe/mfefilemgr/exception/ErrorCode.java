package com.mfe.mfefilemgr.exception;

public enum ErrorCode {
    // ------------------------------------
    // Common
	FAIL_ILLEGAL_ARGUMENT("Illegal Argument"),
	FAIL_OPERATE_ERROR("The operation failure"),
    FAIL_NOT_OPERABLE("Not operable"),
    FAIL_INVALID_USER_LABEL("Invalid User Label"),
    FAIL_USER_LABEL_EXISTED("User Label is existed"),
    FAIL_USER_SNMPIP_EXISTED("User Addresses's snmpAddres's ip is existed"),
    FAIL_INVALID_LOCATION_NAME("Invalid location name"),
    FAIL_INVALID_IP_ADDRESS("Invalid IP address"),
    FAIL_INVALID_PORT("Invalid Port"),
    FAIL_INVALID_HOSTNE("Invalid HostNe"),
    FAIL_INVALID_Q3_ADDRESS("Invalid Q3 address"),
    FAIL_INVALID_COORDINATE("Invalid coordinate"),
    FAIL_INVALID_GEO_COORDINATE("Invalid geography coordinate"),
    FAIL_OBJ_NOT_EXIST("Operation Object doesn't exist"),
    FAIL_HAVE_TRAFFIC("Must remove all traffic before delete it"),
    FAIL_DB_UNREACHABLE("Database is not working"),
    FAIL_DB_MAX_CONNECTION("Reached the maximum of database connection number"),
    FAIL_DB_OPERATION("Database operation occur error"),
    FAIL_ADDRESS_EXISTED("Address existed"),
    FAIL_INVALID_ID("Invalid ID"),
    FAIL_ID_EXISTED("ID was existed"),
    FAIL_INVALID_SNMP_ADDRESS("Invalid snmp address"),
    FAIL_INVALID_IMPLEMENT_STATE("Invalid ImplementState"),
    FAIL_INVALID_PARAMETER("Invalid Parameter"),
    FAIL_GET_VALUES_BY_SNMP("Failed to get values by snmp"),
    FAIL_NOT_SUPPORT_FUNCTION("Not Support Function"),
    
    // ------------------------------------
    // NE
    FAIL_NO_FREE_ADAPTER("No free Adapter for the NE"),
    FAIL_CANNOT_INIT_ADAPTER("Failed to setup new adapter for NE"),
    FAIL_INVALID_Q3_MAIN_ADDRESS("Invalid Q3 NE main management address"),
    FAIL_INVALID_Q3_SPARE_ADDRESS("Invalid Q3 NE spare management address"),
    FAIL_EMLIM_1_NOT_WORK("Q3 Adapter allocator (EMLIM_1) is not working"),
    FAIL_LOST_NE_CONNECTION("Failed to connect to NE"),
    FAIL_CREATE_NE_BY_EMLIM("Failed to create NE by EML_IM"),
    FAIL_DELETE_NE_BY_EMLIM("Failed to delete NE by EML_IM"),
    FAIL_OPERATIONAL_IDLE("Failed to operational NE on IDEL"),
    FAIL_GET_NE_BY_EMLIM("Failed to get NE by EML_IM"),
    FAIL_SUPERVISE_NE_BY_EMLIM("Failed to supervise NE by EML_IM"),
    FAIL_UNSUPERVISE_NE_BY_EMLIM("Failed to stop supervise NE by EML_IM"),
    FAIL_SET_NE_ADDRESS_BY_EMLIM("Failed to set NE address by EML_IM"),
    FAIL_SET_MANAGER_ADDRESS_BY_EMLIM("Failed to set manager address by EML_IM"),
    FAIL_NE_IS_NOT_SUPERVISED("Failed to operate ne due to ne is not supervised"),
    FAIL_SET_NE_BY_SNMP("Failed to set ne by snmp"),
    
    // -----------------------------------
    // XC
    FAIL_CREATE_XC_BY_EMLIM("Failed to create XC by EML_IM"),
    FAIL_CREATE_XC_BY_TP_NOT_FREE("Failed to create XC due to TP is not free"),
    FAIL_CREATE_XC_BY_INVALID_PARAM("Failed to create XC due to parameter is invalid"),
    FAIL_CREATE_XC_BY_TP_NOT_EXISTED("Failed to create XC due to TP is not existed"),
    FAIL_DELETE_XC_BY_EMLIM("Failed to delete XC by EML_IM"),
    FAIL_DELETE_XC_BY_BUSY("Failed to delete XC by busy"),
    FAIL_GET_XC_BY_EMLIM("Failed to get XC by EML_IM"),
    FAIL_CREATE_XC("Failed to create XC"),
    FAIL_DELETE_XC("Failed to delete XC"),
    FAIL_GET_XC_BY_ID("Failed to get XC by XC id"),
    FAIL_SET_XC_BY_SNMP("Failed to set XC by snmp"),
    FAIL_GET_XC_BY_SNMP("Failed to get XC by snmp"),
    
    // -----------------------------------
    // TP
    FAIL_GET_TP_BY_EMLIM("Failed to get TP by EML_IM"),
    FAIL_TERMINATE_TP_BY_EMLIM("Failed to terminate TP by EML_IM"),
    FAIL_PORT_USED("port used"),
    FAIL_GET_TP("Failed to get TP"),
    FAIL_GET_TP_BY_ID("Failed to get TP by TP id"),
    FAIL_SET_TP_BY_SNMP("Failed to set port by snmp"),
    FAIL_SET_TP_INSERVICE("Failed to set port inservice"),
    FAIL_SET_TP_IMPLEMENTED("Failed to set port implemented"),
    FAIL_SET_TP_INSERVICE_BY_NO_SIGNALRATE("Failed to set port inservice as no signalrate was set"),
    FAIL_UPDATE_YP_FROM_ADPATRE("Failed update tp from adapter"),
    
    // -----------------------------------
    // Connection
    FAIL_INVALID_SERVER_CONNECTION("Invalid server connection"),
    FAIL_CONNECTION_EXISTED("connection is existed"),
    FAIL_CONNECTION_NOT_EXISTED("connection is not existed"),
    FAIL_CONNECTION_SNC_EXISTED("snc is existed"),
    FAIL_DELETE_CONNECTION("can't delete it because client connection is existed"),
    FAIL_UPDATE_CONNECTION_IMPLEMENTSTATE("can't update ImplementState because client connection is existed"),
    FAIL_INCLUDE_NO_FIXED_CONNECTION("included no fixed connection"),
    FAIL_PTP_NOT_FREE("ptp has no free resource"),
    FAIL_CTP_NOT_FREE("ctp has no free resource"),
    FAIL_NO_AVAILABLE_CTP("can't find ctp"),
    FAIL_NO_AVAILABLE_ROUTE("can't find or build route"),
    FAIL_INVALID_OTS_TL_PORT("Invalid OTS TL Port"),
    FAIL_INVALID_OMS_TL_PORT("Invalid OMS TL Port"),
    FAIL_INVALID_OS_TL_PORT("Invalid OS TL Port"),
    FAIL_INVALID_ROUTE("Invalid Route"),
    FAIL_INVALID_USAGE("UsageState not IDLE"),
    FAIL_INVALID_LAYERRATE("Invalid layerRate"),
    
    // -----------------------------------
    // Equipment
    FAIL_GET_EQUIPMENT_BY_EMLIM("Failed to get Equipment by EML_IM"),
    FAIL_GET_EQUIPMENT_BY_SNMP("Failed to get Equipment by SNMP"),
    FAIL_SET_EQUIPMENT_BY_SNMP("Failed to set Equipment by SNMP"),
    
    // Remote Inventory
    FAIL_GET_RI_BY_SNMP("Failed to get remote inventory by SNMP"),
    
    // -----------------------------------
    // User
    FAIL_NAME_EXISTED("Name is existed"),
    FAIL_DEFAULT_USER("The User is Default User"),
    FAIL_DEFAULT_USERGROUP("The UserGroup is Default UserGroup"),
    FAIL_USERS_NOTNULL("The UserGroup has Users"),

    // -----------------------------------
    // Tags
    FAIL_THE_TAG_HAS_BEEN("the tag has been."),
    FAIL_NOT_HAVE_THE_TAG("the tag not in the object"),
    FAIL_THE_TAG_IS_FULL("The tag is full"),
    FAIL_THE_TAG_FORMAT_IS_WRONG("The tag format is wrong"),
    
    
    // -----------------------------------
    // PG
    FAIL_CREATE_PG("Failed to create protection group"),
    FAIL_DELETE_PG("Failed to delete protection group"),
    FAIL_GET_PG_BY_SNMP("Failed to get protection group by snmp"),
    FAIL_CREATE_PG_BY_INVALID_PARAM("Failed to create protection group due to invalid parameter")
    ;
	
    
    private String desc = "";
    private ErrorCode(String desc) {
        this.desc = desc;
    }
    
    public String getDescription() {
        return this.desc;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s", this.name(), this.getDescription());
    }
    
    static public String toJSON() {
        String jsonstr = "{";
        ErrorCode ecs[] = ErrorCode.values();
        int size = ecs.length;
        for(int i = 0; i < size - 1; ++i) {
            jsonstr += String.format("\"%s\":\"%s\",", ecs[i].name(), ecs[i].getDescription());
        }
        jsonstr += String.format("\"%s\":\"%s\"}", ecs[size - 1].name(), ecs[size - 1].getDescription());
        return jsonstr;
    }
}
