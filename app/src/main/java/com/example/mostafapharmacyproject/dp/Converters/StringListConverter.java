package com.example.mostafapharmacyproject.dp.Converters;


import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

@ProvidedTypeConverter
public class StringListConverter {
    @TypeConverter
    public String ListToString(List<String> strings){
        return new Gson().toJson(strings , new TypeToken<List<String>>() {} .getType());
    }

    @TypeConverter
    public List<String> StringToList(String str){
        return new Gson().fromJson(str , new TypeToken<List<String>>() {} .getType());
    }
}
