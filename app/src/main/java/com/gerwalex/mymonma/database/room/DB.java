package com.gerwalex.mymonma.database.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.gerwalex.monmang.database.room.Migration_49_50;
import com.gerwalex.mymonma.R;
import com.gerwalex.mymonma.database.tables.Account;
import com.gerwalex.mymonma.database.tables.CashTrx;
import com.gerwalex.mymonma.database.tables.Cat;
import com.gerwalex.mymonma.database.tables.CatClass;
import com.gerwalex.mymonma.database.tables.Partnerstamm;
import com.gerwalex.mymonma.database.tables.TrxRegelm;
import com.gerwalex.mymonma.database.tables.WPKurs;
import com.gerwalex.mymonma.database.tables.WPStamm;
import com.gerwalex.mymonma.database.tables.WPTrx;


@Database(entities = {Account.class, CashTrx.class, Cat.class, CatClass.class,
        TrxRegelm.class, Partnerstamm.class, WPStamm.class, WPKurs.class, WPTrx.class},
        //
        version = 1,
        //
        views = {})
@TypeConverters({MonMaConverter.class, MyConverter.class})
public abstract class DB extends RoomDatabase {
    public static String DBNAME;
    public static Dao dao;
    public static WPDao wpdao;
    /**
     * Instance der RoomDatabase
     */
    private static volatile DB INSTANCE;

    public static DB createInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DB.class) {
                if (INSTANCE == null) {
                    DBCreateCallback callback = new DBCreateCallback(context);
                    DBNAME = context.getString(R.string.dbname);
                    INSTANCE = Room
                            .databaseBuilder(context.getApplicationContext(), DB.class, DBNAME)
                            .addCallback(callback)
                            .addMigrations(new Migration_49_50(49, 50))
                            //
                            .build();
                    dao = INSTANCE.getDao();
                    wpdao = INSTANCE.getWPDao();
                }
            }
        }
        return INSTANCE;
    }

    public static RoomDatabase get() {
        return INSTANCE;
    }

    @Override
    public void close() {
        super.close();
        INSTANCE = null;
    }

    public abstract Dao getDao();

    public abstract WPDao getWPDao();
}