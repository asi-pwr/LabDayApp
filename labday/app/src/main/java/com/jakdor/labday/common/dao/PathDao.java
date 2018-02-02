package com.jakdor.labday.common.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.auto.value.AutoValue;
import com.jakdor.labday.PathDbModel;
import com.jakdor.labday.common.model.Path;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;
import com.squareup.sqldelight.RowMapper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_FAIL;

/**
 * DAO for Path model
 */
@AutoValue
public abstract class PathDao implements PathDbModel {


    public static long insertPathList(BriteDatabase db, List<Path> paths){
        long pos = -1;

        for(Path path : paths) {
            ContentValues values = new Builder()
                    .id(path.getId())
                    .name(path.getName())
                    .info(path.getInfo())
                    .active(path.getActive())
                    .build();

            pos = db.insert(PathDbModel.TABLE_NAME, CONFLICT_FAIL, values);
        }

        return pos;
    }

    public static Observable<List<Path>> getAllPaths(BriteDatabase db){
        Observable<SqlBrite.Query> users = db.createQuery(TABLE, "SELECT * FROM " + TABLE);
        return users.map(query -> {
            ArrayList<Path> paths = new ArrayList<>();

            Cursor cursor = query.run();
            if (cursor == null){
                return paths;
            }

            while(cursor.moveToNext()){
                paths.add(new Path(cursor.getInt(cursor.getColumnIndex(ID)),
                                cursor.getString(cursor.getColumnIndex(NAME)),
                                cursor.getString(cursor.getColumnIndex(INFO)),
                                cursor.getInt(cursor.getColumnIndex(ACTIVE))));
            }

            cursor.close();
            return paths;
        });
    }

    public static void deleteAll(BriteDatabase db){
        db.delete(TABLE, null);
    }

    public static final Factory<PathDao> FACTORY = new Factory<PathDao>(AutoValue_PathDao::new);

    public static final RowMapper<PathDao> SELECT_ALL_MAPPER = FACTORY.selectAllMapper()
}
