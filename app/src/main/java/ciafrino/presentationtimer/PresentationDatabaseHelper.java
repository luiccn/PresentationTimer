package ciafrino.presentationtimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by lrmneves on 11/8/14.
 */
public class PresentationDatabaseHelper {





        private static final String TAG = PresentationDatabaseHelper.class.getSimpleName();

        // database configuration
        // if you want the onUpgrade to run then change the database_version
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "presentation_database.db";

        // table configuration
        private static final String TABLE_NAME = "presentation_table";         // Table name
        private static final String PRESENTATION_TABLE_COLUMN_ID = "_id";     // a column named "_id" is required for cursor
        private static final String PRESENTATION_TABLE_COLUMN_PRESENTATION_NAME = "presentation_name";
        private static final String PRESENTATION_TABLE_COLUMN_STEP_NAME = "step_name";
        private static final String PRESENTATION_TABLE_COLUMN_STEP = "step_id";
        private static final String PRESENTATION_TABLE_COLUMN_DURATION = "duration";
        private static final String PRESENTATION_TABLE_COLUMN_ANNOTATION = "annotation";
        private static final String PRESENTATION_TABLE_COLUMN_COLOR = "color";



    private DatabaseOpenHelper openHelper;
        private SQLiteDatabase database;

        // this is a wrapper class. that means, from outside world, anyone will communicate with PersonDatabaseHelper,
        // but under the hood actually DatabaseOpenHelper class will perform database CRUD operations
        public PresentationDatabaseHelper(Context aContext) {

            openHelper = new DatabaseOpenHelper(aContext);
            database = openHelper.getWritableDatabase();
        }

        public void insertPresentation (String presentationName, String stepName, int step,int duration,
                                        String annotation,int color) {

            // we are using ContentValues to avoid sql format errors

            ContentValues contentValues = new ContentValues();

            contentValues.put(PRESENTATION_TABLE_COLUMN_PRESENTATION_NAME, presentationName);
            contentValues.put(PRESENTATION_TABLE_COLUMN_STEP_NAME, stepName);
            contentValues.put(PRESENTATION_TABLE_COLUMN_STEP, step);
            contentValues.put(PRESENTATION_TABLE_COLUMN_DURATION, duration);
            contentValues.put(PRESENTATION_TABLE_COLUMN_ANNOTATION, annotation);
            contentValues.put(PRESENTATION_TABLE_COLUMN_COLOR, color);


            database.insert(TABLE_NAME, null, contentValues);
        }

        public Cursor getAllData () {

            String buildSQL = "SELECT * FROM " + TABLE_NAME;

            Log.d(TAG, "getAllData SQL: " + buildSQL);

            return database.rawQuery(buildSQL, null);
        }

        // this DatabaseOpenHelper class will actually be used to perform database related operation

        private class DatabaseOpenHelper extends SQLiteOpenHelper {

            public DatabaseOpenHelper(Context aContext) {
                super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                // Create your tables here

                String buildSQL = "CREATE TABLE " + TABLE_NAME + "( " + PRESENTATION_TABLE_COLUMN_ID + " INTEGER, " +
                        PRESENTATION_TABLE_COLUMN_PRESENTATION_NAME + " TEXT, " + PRESENTATION_TABLE_COLUMN_STEP_NAME + " TEXT," +
                        PRESENTATION_TABLE_COLUMN_STEP+" INTEGER, " + PRESENTATION_TABLE_COLUMN_DURATION + " INTEGER, " +
                        PRESENTATION_TABLE_COLUMN_ANNOTATION + " TEXT, " + PRESENTATION_TABLE_COLUMN_COLOR + " INTEGER, "+
                        "PRIMARY KEY ("+ PRESENTATION_TABLE_COLUMN_ID +"," +PRESENTATION_TABLE_COLUMN_STEP + "))";

                Log.d(TAG, "onCreate SQL: " + buildSQL);

                sqLiteDatabase.execSQL(buildSQL);
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
                // Database schema upgrade code goes here

                String buildSQL = "DROP TABLE IF EXISTS " + TABLE_NAME;

                Log.d(TAG, "onUpgrade SQL: " + buildSQL);

                sqLiteDatabase.execSQL(buildSQL);       // drop previous table

                onCreate(sqLiteDatabase);               // create the table from the beginning
            }
        }

}
