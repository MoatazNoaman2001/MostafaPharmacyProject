package com.example.mostafapharmacyproject.di;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.mostafapharmacyproject.Network.NetworkApi;
import com.example.mostafapharmacyproject.R;
import com.example.mostafapharmacyproject.dp.Converters.DateConverter;
import com.example.mostafapharmacyproject.dp.Converters.StringListConverter;
import com.example.mostafapharmacyproject.dp.PharmacyDataBase;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class DependencyGenerator {

    @Provides
    @Singleton
    public Retrofit RetrofitBuilder() {
        return new Retrofit.Builder()
                .baseUrl(NetworkApi.Companion.getBase_url())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .setLenient()
                                .setDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS").create()
                ))
                .build();
    }

    @Provides
    @Singleton
    public NetworkApi getApi(Retrofit retrofit) {
        return retrofit.create(NetworkApi.class);
    }

    @Provides
    @Singleton
    public PharmacyDataBase getInstance(Application application) {
        return Room.databaseBuilder(application, PharmacyDataBase.class, "Pharmacy Database")
                .fallbackToDestructiveMigration()
                .addTypeConverter(new StringListConverter())
                .addTypeConverter(new DateConverter())
                .build();
    }

    @Provides
    @Singleton
    public RequestManager getImageLoader(@ApplicationContext Context context) {
        return Glide.with(context).applyDefaultRequestOptions(
                new RequestOptions()
                        .placeholder(R.drawable.ic_baseline_image_24)
                        .error(R.drawable.ic_baseline_broken_image_24)
        );
    }
}
