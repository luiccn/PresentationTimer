package ciafrino.presentationtimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

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
        private static final String PRESENTATION_TABLE_COLUMN_ID = "id";     // a column named "_id" is required for cursor
        private static final String PRESENTATION_TABLE_COLUMN_PRESENTATION_NAME = "presentation_name";
        private static final String PRESENTATION_TABLE_COLUMN_STEP_NAME = "step_name";
        private static final String PRESENTATION_TABLE_COLUMN_STEP = "step_id";
        private static final String PRESENTATION_TABLE_COLUMN_DURATION = "duration";
        private static final String PRESENTATION_TABLE_COLUMN_ANNOTATION = "annotation";
        private static final String PRESENTATION_TABLE_COLUMN_COLOR = "color";



    private DatabaseOpenHelper openHelper;
    private SQLiteDatabase database;
    private int lastRow = -1;

        // this is a wrapper class. that means, from outside world, anyone will communicate with PersonDatabaseHelper,
        // but under the hood actually DatabaseOpenHelper class will perform database CRUD operations
        public PresentationDatabaseHelper(Context aContext) {

            openHelper = new DatabaseOpenHelper(aContext);
            database = openHelper.getWritableDatabase();

            lastRow = getLastRow();

        }
        public int getLastRow(){
            String getIdQuery = "SELECT MAX("+PRESENTATION_TABLE_COLUMN_ID+") FROM " + TABLE_NAME;
            Cursor c = database.rawQuery(getIdQuery, null);
            if (c.getCount() != 0){
                c.moveToNext();

                return c.getInt(0);
            }
            return -1;
        }

        private void dropTable(){
            String buildSQL = "DROP TABLE IF EXISTS " + TABLE_NAME;
            database.execSQL(buildSQL);       // drop previous table
        }
        private void resetTable(){
            dropTable();
            createTable();
        }
        private void createTable(){
            String buildSQL = "CREATE TABLE " + TABLE_NAME + "( " + PRESENTATION_TABLE_COLUMN_ID + " INTEGER NOT NULL, " +
                    PRESENTATION_TABLE_COLUMN_PRESENTATION_NAME + " TEXT, " + PRESENTATION_TABLE_COLUMN_STEP_NAME + " TEXT," +
                    PRESENTATION_TABLE_COLUMN_STEP+" INTEGER NOT NULL, " + PRESENTATION_TABLE_COLUMN_DURATION + " INTEGER, " +
                    PRESENTATION_TABLE_COLUMN_ANNOTATION + " TEXT, " + PRESENTATION_TABLE_COLUMN_COLOR + " INTEGER, "+
                    "PRIMARY KEY ("+ PRESENTATION_TABLE_COLUMN_ID +"," +PRESENTATION_TABLE_COLUMN_STEP + "))";

            Log.d(TAG, "onCreate SQL: " + buildSQL);

            database.execSQL(buildSQL);
        }
        public int insertPresentation(Presentation p){
            ContentValues contentValues = new ContentValues();

            contentValues.put(PRESENTATION_TABLE_COLUMN_ID,++lastRow);
            contentValues.put(PRESENTATION_TABLE_COLUMN_PRESENTATION_NAME, p.getName());
            contentValues.put(PRESENTATION_TABLE_COLUMN_STEP_NAME, "");
            contentValues.put(PRESENTATION_TABLE_COLUMN_STEP, 1);
            contentValues.put(PRESENTATION_TABLE_COLUMN_DURATION, 0);
            contentValues.put(PRESENTATION_TABLE_COLUMN_ANNOTATION, "");
            contentValues.put(PRESENTATION_TABLE_COLUMN_COLOR, "");

            database.insert(TABLE_NAME, null, contentValues);
            return lastRow;
        }
        public void insertPresentation (int id,String presentationName, String stepName, int step,int duration,
                                        String annotation,int color) {

            // we are using ContentValues to avoid sql format errors

            ContentValues contentValues = new ContentValues();

            contentValues.put(PRESENTATION_TABLE_COLUMN_ID,id == -1?++lastRow:id);
            contentValues.put(PRESENTATION_TABLE_COLUMN_PRESENTATION_NAME, presentationName);
            contentValues.put(PRESENTATION_TABLE_COLUMN_STEP_NAME, stepName);
            contentValues.put(PRESENTATION_TABLE_COLUMN_STEP, step);
            contentValues.put(PRESENTATION_TABLE_COLUMN_DURATION, duration);
            contentValues.put(PRESENTATION_TABLE_COLUMN_ANNOTATION, annotation);
            contentValues.put(PRESENTATION_TABLE_COLUMN_COLOR, color);

            boolean insert = false;
            while (!insert){
                try{
                    database.insert(TABLE_NAME, null, contentValues);
                    insert = true;
                }
                catch(SQLiteConstraintException e){
                    step++;
                    contentValues.put(PRESENTATION_TABLE_COLUMN_STEP, step);
                }
            }
        }
        public void updatePresentation(int presentationId ,String presentationName, String stepName, int step,int duration,
                                       String annotation,int color){
            ContentValues values = new ContentValues();
            values.put(PRESENTATION_TABLE_COLUMN_PRESENTATION_NAME, presentationName);
            values.put(PRESENTATION_TABLE_COLUMN_STEP_NAME, stepName);
            values.put(PRESENTATION_TABLE_COLUMN_STEP, step);
            values.put(PRESENTATION_TABLE_COLUMN_DURATION, duration);
            values.put(PRESENTATION_TABLE_COLUMN_ANNOTATION, annotation);
            values.put(PRESENTATION_TABLE_COLUMN_COLOR, color);

            database.update(TABLE_NAME,values,PRESENTATION_TABLE_COLUMN_ID + " = ?", new String[] {String.valueOf(presentationId)});
        }
         public void deletePresentation(String id) {
             String deleteQuery = "DELETE FROM "+ TABLE_NAME + " where " + PRESENTATION_TABLE_COLUMN_ID + "= '" + id+"';";
             database.execSQL(deleteQuery);
         }
        public void deleteStep(Presentation p, Step s) {
            String deleteQuery = "DELETE FROM "+ TABLE_NAME + " where " + PRESENTATION_TABLE_COLUMN_ID + "= '" + p.getId()+"' AND " +
                    PRESENTATION_TABLE_COLUMN_STEP +"= '"+s.getId()+"';";
            database.execSQL(deleteQuery);
        }

        public Presentation getPresentaitonByID(int id){
            String buildSQL = "SELECT DISTINCT " +PRESENTATION_TABLE_COLUMN_PRESENTATION_NAME +
                    " FROM " + TABLE_NAME + " WHERE "+PRESENTATION_TABLE_COLUMN_ID +"='" + id + "';";
            Cursor c = database.rawQuery(buildSQL, null);
            if(c.getCount() != 0){
                c.moveToNext();

                Presentation p = new Presentation(c.getString(0),id);
                p.setSteps_list(getPresentationSteps(id));

                return p;
            }
            else{
                return null;
            }

        }

        public Cursor getPresentationList () {
            String buildSQL = "SELECT DISTINCT "+PRESENTATION_TABLE_COLUMN_ID +"," +PRESENTATION_TABLE_COLUMN_PRESENTATION_NAME +
                    " FROM " + TABLE_NAME;

            Log.d(TAG, "getAllData SQL: " + buildSQL);

            return database.rawQuery(buildSQL, null);
        }
        public ArrayList<Step> getPresentationSteps(int id){
            String buildSQL = "SELECT "+PRESENTATION_TABLE_COLUMN_STEP +"," +PRESENTATION_TABLE_COLUMN_STEP_NAME +
                    ","+PRESENTATION_TABLE_COLUMN_ANNOTATION +","+ PRESENTATION_TABLE_COLUMN_COLOR +","+ PRESENTATION_TABLE_COLUMN_DURATION+
                    " FROM " + TABLE_NAME +" WHERE "+ PRESENTATION_TABLE_COLUMN_ID +"= '" + id +"'; ORDER BY " +
                    PRESENTATION_TABLE_COLUMN_STEP +";";

           Cursor c =  database.rawQuery(buildSQL, null);
           ArrayList<Step> steps = new ArrayList<Step>();
           if(c.getCount() != 0){

               while(c.moveToNext()){
                   steps.add(new Step(c.getInt(0),c.getString(1),c.getString(2),c.getInt(3),c.getInt(4)));

               }
           }
           return steps;
        }
        public void insertNewStep(Presentation p, Step s){
            insertPresentation(p.getId(),p.getName(),s.getName(),s.getId(),s.getDuration(),s.getText(),s.getColor());
        }


        // this DatabaseOpenHelper class will actually be used to perform database related operation

        private class DatabaseOpenHelper extends SQLiteOpenHelper {

            public DatabaseOpenHelper(Context aContext) {
                super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                // Create your tables here
                createTable();

            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
                // Database schema upgrade code goes here
                dropTable();

                onCreate(sqLiteDatabase);               // create the table from the beginning
            }
        }

}
