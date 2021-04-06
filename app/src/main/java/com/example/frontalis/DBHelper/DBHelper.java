package com.example.frontalis.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.frontalis.Model.Category;
import com.example.frontalis.Model.Question;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteAssetHelper {
    private static final String DB_NAME = "EDMTQuiz2019.db";
    private static final int DB_VER = 1;

    private static DBHelper instance;

    public static synchronized DBHelper getInstance(Context context){
        if (instance == null){
            instance = new DBHelper(context);
        }
        return instance;
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Category> getAllCategories(){
        SQLiteDatabase db = instance.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Category;", null);
        List<Category> categories = new ArrayList<>();
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                Category category = new Category(cursor.getInt(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex("Name")));
                categories.add(category);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return categories;
    }

    public List<Question> getQuestionByCategory(int category){
        SQLiteDatabase db = instance.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Question WHERE CategoryID = " + category + " ORDER BY RANDOM() LIMIT 30;", null);
        List<Question> questions = new ArrayList<>();
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                Question question = new Question(
                        cursor.getInt(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex("QuestionText")),
                        cursor.getString(cursor.getColumnIndex("AnswerA")),
                        cursor.getString(cursor.getColumnIndex("AnswerB")),
                        cursor.getString(cursor.getColumnIndex("AnswerC")),
                        cursor.getString(cursor.getColumnIndex("AnswerD")),
                        cursor.getString(cursor.getColumnIndex("CorrectAnswer")),
                        cursor.getInt(cursor.getColumnIndex("CategoryID")));
                questions.add(question);
                Log.d("MAIN", question.getQuestionText());
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return questions;
    }


}
