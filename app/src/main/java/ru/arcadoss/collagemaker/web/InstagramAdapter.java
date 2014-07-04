package ru.arcadoss.collagemaker.web;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import ru.arcadoss.collagemaker.json.EntriesListWrapper;

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


	private static final JacksonConverter JACKSON_CONVERTER;
	static {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		SimpleModule module = new SimpleModule("EntryListWrapper Deserializer", new Version(1, 1, 1, null));
		module.addDeserializer(EntriesListWrapper.class, new EntriesListWrapper.Deserializer());

		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(module);

		JACKSON_CONVERTER = new JacksonConverter(mapper, factory);
	}

	public static final RestAdapter get() {
		RestAdapter adapter = new RestAdapter.Builder()
				.setRequestInterceptor(HEADER_INJECTOR)
				.setEndpoint(Instagram.ENDPOINT)
				.setLogLevel(RestAdapter.LogLevel.NONE)
				.setConverter(JACKSON_CONVERTER)
				.build();

		return adapter;
	}

	public static final Instagram getService() {
		return get()
				.create(Instagram.class);
	}

	public static JacksonConverter getConverter() {
		return JACKSON_CONVERTER;
	}
}
