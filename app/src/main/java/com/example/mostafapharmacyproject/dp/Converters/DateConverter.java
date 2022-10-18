package com.example.mostafapharmacyproject.dp.Converters;


import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.Date;

@ProvidedTypeConverter
public class DateConverter {

    @TypeConverter
    public Long DateToLong(Date date){
        return date.getTime();
    }

    @TypeConverter
    public Date LongToDate(Long l){
        return new Date(l);
    }
}
