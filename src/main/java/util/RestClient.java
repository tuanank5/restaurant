package util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * REST Client utility for communicating with the backend API
 */
public class RestClient {
	private static final String BASE_URL = "http://localhost:8080/api";
	private static final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Make a GET request to retrieve all resources
	 */
	public static <T> List<T> getAll(String endpoint, Class<T> resourceClass) throws IOException {
		String url = BASE_URL + endpoint;
		String response = makeRequest("GET", url, null);
		
		// Parse JSON array response
		var typeFactory = objectMapper.getTypeFactory();
		Type listType = typeFactory.constructCollectionType(List.class, resourceClass);
		return objectMapper.readValue(response, 
			objectMapper.getTypeFactory().constructCollectionType(List.class, resourceClass));
	}

	/**
	 * Make a GET request to retrieve a specific resource
	 */
	public static <T> T getById(String endpoint, String id, Class<T> resourceClass) throws IOException {
		String url = BASE_URL + endpoint + "/" + URLEncoder.encode(id, StandardCharsets.UTF_8);
		String response = makeRequest("GET", url, null);
		return objectMapper.readValue(response, resourceClass);
	}

	/**
	 * Make a POST request to create a new resource
	 */
	public static <T> T create(String endpoint, T resource, Class<T> resourceClass) throws IOException {
		String url = BASE_URL + endpoint;
		String requestBody = objectMapper.writeValueAsString(resource);
		String response = makeRequest("POST", url, requestBody);
		return objectMapper.readValue(response, resourceClass);
	}

	/**
	 * Make a PUT request to update an existing resource
	 */
	public static <T> T update(String endpoint, String id, T resource, Class<T> resourceClass) throws IOException {
		String url = BASE_URL + endpoint + "/" + URLEncoder.encode(id, StandardCharsets.UTF_8);
		String requestBody = objectMapper.writeValueAsString(resource);
		String response = makeRequest("PUT", url, requestBody);
		return objectMapper.readValue(response, resourceClass);
	}

	/**
	 * Make a DELETE request to remove a resource
	 */
	public static void delete(String endpoint, String id) throws IOException {
		String url = BASE_URL + endpoint + "/" + URLEncoder.encode(id, StandardCharsets.UTF_8);
		makeRequest("DELETE", url, null);
	}

	/**
	 * Make the HTTP request
	 */
	private static String makeRequest(String method, String urlString, String requestBody) throws IOException {
		java.net.URL url = new java.net.URL(urlString);
		java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
		connection.setRequestMethod(method);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);

		if (requestBody != null) {
			connection.setDoOutput(true);
			try (var os = connection.getOutputStream()) {
				byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}
		}

		int statusCode = connection.getResponseCode();
		if (statusCode >= 400) {
			throw new IOException("HTTP Error: " + statusCode + " - " + connection.getResponseMessage());
		}

		try (var is = connection.getInputStream()) {
			return new String(is.readAllBytes(), StandardCharsets.UTF_8);
		}
	}
}
