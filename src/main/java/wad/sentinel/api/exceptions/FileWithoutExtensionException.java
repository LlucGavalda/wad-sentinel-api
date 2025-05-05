package wad.sentinel.api.exceptions;

import wad.sentinel.api.error.ErrorConstants;

public class FileWithoutExtensionException extends AbstractException {

	private static final long serialVersionUID = 7948831832861112481L;

	/**
	 * File does not have an extension
	 * 
	 * @param fileName
	 */
	public FileWithoutExtensionException(String fileName) {
		super(ErrorConstants.ERROR_FILE_WITHOUT_EXTENSION,
				new String[] { fileName },
				String.format("File %s does not have an extension", fileName));
	}

}
