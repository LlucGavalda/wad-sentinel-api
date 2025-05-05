package wad.sentinel.api.exceptions;

import wad.sentinel.api.error.ErrorConstants;

public class FileAlreadyExistsException extends AbstractException {

	private static final long serialVersionUID = 7948831832861112481L;

	/**
	 * File already exists in the location
	 * 
	 * @param fileName
	 * @param filePath
	 */
	public FileAlreadyExistsException(String fileName, String filePath) {
		super(ErrorConstants.ERROR_FILE_ALREADY_EXISTS,
				new String[] { filePath, fileName },
				String.format("File %s already exists in %s", fileName, filePath));

	}

}
