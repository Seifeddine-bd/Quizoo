// com/seifeddine/bd/quizoo/data/local/OptionsConverter.java
package com.seifeddine.bd.quizoo.data.local;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class OptionsConverter {
    @TypeConverter
    public static String fromList(List<String> options) {
        return new Gson().toJson(options);
    }

    @TypeConverter
    public static List<String> toList(String json) {
        return new Gson().fromJson(json, new TypeToken<List<String>>(){}.getType());
    }
}