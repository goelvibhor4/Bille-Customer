package in.bille.app.Database;



import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import in.bille.app.CardFunctions;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CardsManager";
    private static final String TABLE_CARDS = "MyCards";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_CARD_NO = "card_no";
    private static final String KEY_CARD_MONTH = "card_month";
    private static final String KEY_CARD_YEAR = "card_year";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CARDS_TABLE = "CREATE TABLE " + TABLE_CARDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"+ KEY_PH_NO + " TEXT," + KEY_CARD_NO + " TEXT," + KEY_CARD_MONTH + " TEXT," + KEY_CARD_YEAR + " TEXT" + ")";
        db.execSQL(CREATE_CARDS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);

        // Create tables again
        onCreate(db);
    }

    // code to add the new card
    public void addCard(CardFunctions card) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, card.getName()); // card Name
        values.put(KEY_PH_NO, card.getPhoneNumber()); // card Phone
        values.put(KEY_CARD_NO, card.getCardNumber()); // card no
        values.put(KEY_CARD_MONTH, card.getCardMonth()); // card MONTH
        values.put(KEY_CARD_YEAR, card.getCardYear()); // card year


        // Inserting Row
        db.insert(TABLE_CARDS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single card
    CardFunctions getCard(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CARDS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO,KEY_CARD_NO,KEY_CARD_MONTH,KEY_CARD_YEAR }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        CardFunctions card = new CardFunctions(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        // return card
        return card;
    }

    // code to get all card in a list view
    public List<CardFunctions> getAllCards() {
        List<CardFunctions> cardList = new ArrayList<CardFunctions>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CARDS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CardFunctions card = new CardFunctions();
                card.setID(Integer.parseInt(cursor.getString(0)));
                card.setName(cursor.getString(1));
                card.setPhoneNumber(cursor.getString(2));
                card.setCardNumber(cursor.getString(3));
                card.setCardMonth(cursor.getString(4));
                card.setCardYear(cursor.getString(5));

                // Adding card to list
                cardList.add(card);
            } while (cursor.moveToNext());
        }

        // return card list
        return cardList;
    }

    // code to update the single card
    public int updateCard(CardFunctions card) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, card.getName());
        values.put(KEY_PH_NO, card.getPhoneNumber());

        // updating row
        return db.update(TABLE_CARDS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(card.getID()) });
    }

    // Deleting single card
    public void deleteCard(CardFunctions card) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CARDS, KEY_ID + " = ?",
                new String[]{String.valueOf(card.getID()) });
        db.close();
    }

    // Getting card Count
    public int getCardsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CARDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}