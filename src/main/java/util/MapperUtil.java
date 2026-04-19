package util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MapperUtil {
	private static ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static <S, T> T map(S source, Class<T> target) {
		return objectMapper.convertValue(source, target);
	}
}
