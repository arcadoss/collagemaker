package ru.arcadoss.collagemaker.web;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParseException;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * Created by arcturus at 04.07.14
 */
public class JacksonConverter implements Converter {
	ObjectMapper mapper;
	JsonFactory factory;

	public JacksonConverter(ObjectMapper mapper, JsonFactory factory) {
		this.mapper = mapper;
		this.factory = factory;
	}

	@Override
	public Object fromBody(TypedInput body, final Type type) throws ConversionException {
		String charset = "UTF-8";
		if (body.mimeType() != null) {
			charset = MimeUtil.parseCharset(body.mimeType());
		}
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(body.in(), charset);
			TypeReference hack = new TypeReference<String>() {
				@Override
				public Type getType() {
					return type;
				}
			};
			return mapper.readValue(isr, hack);
		} catch (IOException e) {
			throw new ConversionException(e);
		} catch (JsonParseException e) {
			throw new ConversionException(e);
		} finally {
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

	@Override
	public TypedOutput toBody(Object object) {
		try {
			return new JsonTypedOutput(mapper.writeValueAsBytes(object), "UTF-8");
		} catch (JsonProcessingException e) {
			throw new AssertionError(e);
		}
	}

	public ObjectMapper getObjectMapper() {
		return mapper;
	}

	private static class JsonTypedOutput implements TypedOutput {
		private final byte[] jsonBytes;
		private final String mimeType;

		JsonTypedOutput(byte[] jsonBytes, String encode) {
			this.jsonBytes = jsonBytes;
			this.mimeType = "application/json; charset=" + encode;
		}

		@Override public String fileName() {
			return null;
		}

		@Override public String mimeType() {
			return mimeType;
		}

		@Override public long length() {
			return jsonBytes.length;
		}

		@Override public void writeTo(OutputStream out) throws IOException {
			out.write(jsonBytes);
		}
	}
}
