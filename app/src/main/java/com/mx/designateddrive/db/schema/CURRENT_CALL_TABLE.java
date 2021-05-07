package com.mx.designateddrive.db.schema;

public class CURRENT_CALL_TABLE {

    public static final String TABLENAME = "current_call";
    public static final String IDX = "idx";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String USER_NAME = "user_name";
    public static final String START_POINT = "start_point";
    public static final String END_POINT = "end_point";
    public static final String LAYOVER = "layover";
    public static final String REG_DATE = "reg_date";

    public static final String CREATE_QUERY = "create table IF NOT EXISTS " + TABLENAME + "("
            + IDX + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PHONE_NUMBER + " text NOT NULL UNIQUE,"
            + USER_NAME + " text,"
            + START_POINT + " text,"
            + END_POINT + " text,"
            + LAYOVER + " text,"
            + REG_DATE + " text DEFAULT DATE DEFAULT (datetime('now','localtime')));";

    public static final String DROP_QUERY = "drop table if exists " + TABLENAME;
}
