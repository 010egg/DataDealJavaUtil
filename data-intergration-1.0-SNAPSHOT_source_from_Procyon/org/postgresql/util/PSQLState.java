// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

import java.io.Serializable;

public class PSQLState implements Serializable
{
    private String state;
    public static final PSQLState UNKNOWN_STATE;
    public static final PSQLState TOO_MANY_RESULTS;
    public static final PSQLState NO_DATA;
    public static final PSQLState INVALID_PARAMETER_TYPE;
    public static final PSQLState CONNECTION_UNABLE_TO_CONNECT;
    public static final PSQLState CONNECTION_DOES_NOT_EXIST;
    public static final PSQLState CONNECTION_REJECTED;
    public static final PSQLState CONNECTION_FAILURE;
    public static final PSQLState CONNECTION_FAILURE_DURING_TRANSACTION;
    public static final PSQLState PROTOCOL_VIOLATION;
    public static final PSQLState COMMUNICATION_ERROR;
    public static final PSQLState NOT_IMPLEMENTED;
    public static final PSQLState DATA_ERROR;
    public static final PSQLState NUMERIC_VALUE_OUT_OF_RANGE;
    public static final PSQLState BAD_DATETIME_FORMAT;
    public static final PSQLState DATETIME_OVERFLOW;
    public static final PSQLState MOST_SPECIFIC_TYPE_DOES_NOT_MATCH;
    public static final PSQLState INVALID_PARAMETER_VALUE;
    public static final PSQLState INVALID_CURSOR_STATE;
    public static final PSQLState TRANSACTION_STATE_INVALID;
    public static final PSQLState ACTIVE_SQL_TRANSACTION;
    public static final PSQLState NO_ACTIVE_SQL_TRANSACTION;
    public static final PSQLState STATEMENT_NOT_ALLOWED_IN_FUNCTION_CALL;
    public static final PSQLState INVALID_SAVEPOINT_SPECIFICATION;
    public static final PSQLState SYNTAX_ERROR;
    public static final PSQLState UNDEFINED_COLUMN;
    public static final PSQLState UNDEFINED_OBJECT;
    public static final PSQLState WRONG_OBJECT_TYPE;
    public static final PSQLState NUMERIC_CONSTANT_OUT_OF_RANGE;
    public static final PSQLState DATA_TYPE_MISMATCH;
    public static final PSQLState UNDEFINED_FUNCTION;
    public static final PSQLState INVALID_NAME;
    public static final PSQLState OUT_OF_MEMORY;
    public static final PSQLState OBJECT_NOT_IN_STATE;
    public static final PSQLState SYSTEM_ERROR;
    public static final PSQLState IO_ERROR;
    public static final PSQLState UNEXPECTED_ERROR;
    
    public String getState() {
        return this.state;
    }
    
    public PSQLState(final String state) {
        this.state = state;
    }
    
    static {
        UNKNOWN_STATE = new PSQLState("");
        TOO_MANY_RESULTS = new PSQLState("0100E");
        NO_DATA = new PSQLState("02000");
        INVALID_PARAMETER_TYPE = new PSQLState("07006");
        CONNECTION_UNABLE_TO_CONNECT = new PSQLState("08001");
        CONNECTION_DOES_NOT_EXIST = new PSQLState("08003");
        CONNECTION_REJECTED = new PSQLState("08004");
        CONNECTION_FAILURE = new PSQLState("08006");
        CONNECTION_FAILURE_DURING_TRANSACTION = new PSQLState("08007");
        PROTOCOL_VIOLATION = new PSQLState("08P01");
        COMMUNICATION_ERROR = new PSQLState("08S01");
        NOT_IMPLEMENTED = new PSQLState("0A000");
        DATA_ERROR = new PSQLState("22000");
        NUMERIC_VALUE_OUT_OF_RANGE = new PSQLState("22003");
        BAD_DATETIME_FORMAT = new PSQLState("22007");
        DATETIME_OVERFLOW = new PSQLState("22008");
        MOST_SPECIFIC_TYPE_DOES_NOT_MATCH = new PSQLState("2200G");
        INVALID_PARAMETER_VALUE = new PSQLState("22023");
        INVALID_CURSOR_STATE = new PSQLState("24000");
        TRANSACTION_STATE_INVALID = new PSQLState("25000");
        ACTIVE_SQL_TRANSACTION = new PSQLState("25001");
        NO_ACTIVE_SQL_TRANSACTION = new PSQLState("25P01");
        STATEMENT_NOT_ALLOWED_IN_FUNCTION_CALL = new PSQLState("2F003");
        INVALID_SAVEPOINT_SPECIFICATION = new PSQLState("3B000");
        SYNTAX_ERROR = new PSQLState("42601");
        UNDEFINED_COLUMN = new PSQLState("42703");
        UNDEFINED_OBJECT = new PSQLState("42704");
        WRONG_OBJECT_TYPE = new PSQLState("42809");
        NUMERIC_CONSTANT_OUT_OF_RANGE = new PSQLState("42820");
        DATA_TYPE_MISMATCH = new PSQLState("42821");
        UNDEFINED_FUNCTION = new PSQLState("42883");
        INVALID_NAME = new PSQLState("42602");
        OUT_OF_MEMORY = new PSQLState("53200");
        OBJECT_NOT_IN_STATE = new PSQLState("55000");
        SYSTEM_ERROR = new PSQLState("60000");
        IO_ERROR = new PSQLState("58030");
        UNEXPECTED_ERROR = new PSQLState("99999");
    }
}
