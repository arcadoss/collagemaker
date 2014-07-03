package ru.arcadoss.collagemaker.web;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import ru.arcadoss.collagemaker.json.EntriesListWrapper;
import ru.arcadoss.collagemaker.json.Envelope;
import ru.arcadoss.collagemaker.json.User;

import java.util.List;

/**
 * Created by arcturus at 01.07.14
 */
public interface Instagram {
	static final String ENDPOINT = "https://api.instagram.com/v1";

	@GET("/users/search")
	void findUser(@Query("q") String query, Callback<Envelope<List<User>>> cb);

	@GET("/users/{userid}/media/recent")
	void getTopPhotos(@Path("userid") String userid, Callback<Envelope<EntriesListWrapper>> cb);
}
