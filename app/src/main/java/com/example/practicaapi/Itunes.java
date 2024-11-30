package com.example.practicaapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class Itunes {

    class Respuesta {
        List<Contenido> results;
    }

    class Contenido {
        String artistName;
        String trackName;
        String artworkUrl100;
    }

    public static Api api = new Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api.class);

    public interface Api {
        @GET("search/")
        Call<Respuesta> buscar(@Query("term") String texto);
    }
}
