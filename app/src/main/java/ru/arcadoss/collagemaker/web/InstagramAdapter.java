package ru.arcadoss.collagemaker.web;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by arcturus at 02.07.14
 */
public class InstagramAdapter {
	private InstagramAdapter() {}

	private static final RequestInterceptor HEADER_INJECTOR = new RequestInterceptor() {
		@Override
		public void intercept(RequestFacade request) {
			request.addQueryParam("client_id", "d21f2469b3b84eb68d95a44b88c96d38");
		}
	};

	private static final Gson GSON_CONVERTER = new GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.registerTypeAdapter(EntriesListWrapper.class, new EntriesListWrapper.Deserializer())
			.create();


	private static final JacksonConverter JACKSON_CONVERTER;
	static {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		JACKSON_CONVERTER = new JacksonConverter(mapper, factory);
	}

	public static final RestAdapter get() {
		RestAdapter adapter = new RestAdapter.Builder()
				.setRequestInterceptor(HEADER_INJECTOR)
				.setEndpoint(Instagram.ENDPOINT)
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.setConverter(JACKSON_CONVERTER)
				.build();

		return adapter;
	}

	public static final Instagram getService() {
		return get()
				.create(Instagram.class);
	}
}
