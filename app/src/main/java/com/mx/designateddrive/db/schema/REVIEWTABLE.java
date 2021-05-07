package com.mx.designateddrive.db.schema;

public class REVIEWTABLE {

    public static final String TABLENAME = "reviewTable";
    public static final String IDX = "idx";
    public static final String DRIVER_NAME = "driver_name";
    public static final String REVIEW_STAR = "review_star";
    public static final String USER_NAME = "user_name";
    public static final String REVIEW = "review";
    public static final String REG_DATE = "reg_date";

    public static final String CREATE_QUERY = "create table IF NOT EXISTS " + TABLENAME + "("
            + IDX + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DRIVER_NAME + " text,"
            + REVIEW_STAR + " text,"
            + USER_NAME + " text,"
            + REVIEW + " text,"
            + REG_DATE + " text);";

    public static final String DROP_QUERY = "drop table if exists " + TABLENAME;
}
