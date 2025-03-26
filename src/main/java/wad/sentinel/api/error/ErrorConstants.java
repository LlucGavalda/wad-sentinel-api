package wad.sentinel.api.error;

public class ErrorConstants {

	// Error codes
	public static final int ERROR_GENERIC 					= 100;		// Generic error, not specified
	public static final int ERROR_SECURITY 					= 101;		// Security error
	public static final int ERROR_CREDENTIALS 				= 110;		// Bad credentials, login rejected
	public static final int ERROR_NOT_FOUND 				= 120;		// Element not found. Used in searches
	public static final int ERROR_ALREADY_EXISTS			= 121;		// Element already exists. Used in new entities creation
	public static final int ERROR_ALREADY_RELATED			= 122;		// Elements are already related to each other
	public static final int ERROR_STORAGE 					= 130;		// Error while managing storage
	public static final int ERROR_FILE_NOT_FOUND			= 131;		// File not found
	public static final int ERROR_FILE_TOO_LARGE			= 132;		// File is too large
	public static final int ERROR_FILE_ALREADY_EXISTS		= 133;		// File already exists
	public static final int ERROR_FILE_WITHOUT_EXTENSION	= 134;		// File does not have an extension
	public static final int ERROR_PARAMETER_NOT_FOUND		= 140;		// Parameter not found
	public static final int ERROR_PARAMETER_INVALID			= 141;		// Value for the parameter is invalid
	public static final int ERROR_PARAMETER_EMPTY			= 142;		// Parameter cannot be empty
	public static final int ERROR_PARAMETER_MODIFIED		= 143;		// Parameter cannot be modified
	public static final int ERROR_PARAMETER_EQUALS			= 144;		// Parameter cannot be equal
	public static final int ERROR_PROPERTY_NOT_FOUND		= 145;		// Property has not been found
	public static final int ERROR_PARAMETERS_CORRESPONDENCE	= 146;		// Parameters do not correspond to each other
	public static final int ERROR_EMAIL						= 150;		// Generic email error
	public static final int ERROR_URL_INVALID				= 151;		// Generic not valid url error
	public static final int ERROR_SMS						= 152;		// Generic SMS error
	public static final int ERROR_INVALID_CALL				= 153;		// The method cannot be called
	public static final int ERROR_NOT_ENOUGH_PERMISSIONS	= 160;		// The user does not have enough permissions or is not logged in
	public static final int ERROR_BAD_QUERY_PARENTHISATION	= 170;		// The query has too many unclosed parenthesis or has more close than open parenthesis
	public static final int ERROR_BAD_LOGICAL_OPERATORS		= 171;		// The query has logical operators 'AND' 'OR' in a bad place
	public static final int ERROR_BAD_QUERY					= 172;		// The native query is not well formed and cannot be executed
	public static final int ERROR_PROCESSING_NOT_FOUND 		= 180;		// Processing not found. Used in searches
	public static final int ERROR_SUBSCRIPTION_NOT_ALLOWED_CHANGE_PERSON 	= 181;		
	public static final int ERROR_SUBSCRIPTION_ALREADY_EXIST_COOWNER 		= 182;		
	public static final int ERROR_SUBSCRIPTION_ALREADY_EXIST_BENEFICIARY 	= 183;		
	public static final int ERROR_SEGURIDAD_SOCIAL_NUMBER_FORMAT = 184;
	public static final int ERROR_SAME_ID_AS_ITSELF = 185;
	public static final int ERROR_NATIONAL_EXIST_WITH_OTHER = 186;
	public static final int ERROR_ONLY_ONE_ATTR_PERMITTED = 187;



}
