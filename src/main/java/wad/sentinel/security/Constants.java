package wad.sentinel.security;

public class Constants {

	public static final String URL_LOGIN = "/wadsentinel/api"; // Base URL for all the api calls

	// JWT
	public final static String ACCESS_TOKEN_SECRET = "dfskj43opirf5345sdsdvcf236skjh67podfvmvlfdkvp"; 	// Secret
	public final static Long ACCESS_TOKEN_VALIDITY_SECONDS = 30 * 24 * 60 * 60 * 1000L;  // Token validity time: 30 days
	
	// CORS
	public static final String CORS_MAPPING = "/**";
	public static final String CORS_ORIGINS = "*";
	public static final String CORS_METHODS = "*";
	public static final String CORS_EXPOSED_HEADERS = "*";

}
