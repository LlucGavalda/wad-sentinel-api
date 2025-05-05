package wad.sentinel.api.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import wad.sentinel.api.Constants;
import wad.sentinel.api.dto.DocumentFileDto;
import wad.sentinel.api.entity.DocumentFile;
import wad.sentinel.api.entity.Parameter;
import wad.sentinel.api.exceptions.FileAlreadyExistsException;
import wad.sentinel.api.exceptions.FileWithoutExtensionException;
import wad.sentinel.api.exceptions.NotFoundException;
import wad.sentinel.api.exceptions.NotFoundParameterException;
import wad.sentinel.api.exceptions.ParameterEmptyException;
import wad.sentinel.api.exceptions.ParameterInvalidException;
import wad.sentinel.api.exceptions.StorageException;
import wad.sentinel.api.repository.DocumentFileRepository;
import wad.sentinel.api.repository.ParameterRepository;
import wad.sentinel.api.utils.Utils;

@Service
public class DocumentFileServiceImpl implements DocumentFileService {

	@Autowired
	private ParameterRepository parameterRepository;

	@Autowired
	private DocumentFileRepository documentFileRepository;

	/**
	 * Wrapper to create.
	 * 
	 * @param file         The file contents itself
	 * @param relativePath Path relative to the base path
	 * 
	 * @return File registry created
	 */
	@Override
	public DocumentFile create(MultipartFile file, String path) {
		return create(file, path, null);
	}

	/**
	 * Creates a file in the corresponding table and saves it to the storage.
	 * The storage is defined by a base file path in parameters table under id
	 * Constants.PARAMETER_FILE_PATH.
	 * 
	 * @param file         The file contents itself
	 * @param relativePath Path relative to the base path
	 * @param fileName     The name of the file once it is stored. In case it's
	 *                     null, the name of the file will be the registry Id
	 * 
	 * @return File registry created
	 */
	@Override
	@Transactional
	public DocumentFile create(MultipartFile file, String relativePath, String fileName) {
		Boolean calculateFileName = (fileName == null || fileName.trim().equalsIgnoreCase(""));
		String fileExtension = "";

		if (calculateFileName) {
			// Use a temporal file name to insert in the DB
			fileName = "TEMPORAL";

			// We'll need an extension, so we'll get it from the original file name
			fileExtension = Utils.getFileExtension(file.getOriginalFilename());
			if (fileExtension.isBlank())
				throw new FileWithoutExtensionException(file.getOriginalFilename());
		}

		// Create new document in DB
		// Registry must be created first just in case we need the Id to name the file
		DocumentFile documentFile = new DocumentFile();
		documentFile.prepareToSave();
		documentFile.setFileName(fileName);
		documentFile.setOriginalFileName(file.getOriginalFilename());
		documentFile.setFilePath(relativePath);
		documentFile.setMediaType(file.getContentType());
		documentFile = documentFileRepository.save(documentFile);

		if (calculateFileName) {
			// Use the Id+extension as the file name
			fileName = documentFile.getId().toString() + "." + fileExtension;

			// Update and save the registry
			documentFile.setFileName(fileName);
			documentFile = documentFileRepository.save(documentFile);
		}

		// Find the file root location in parameters
		Parameter parameter = parameterRepository.findByCode(Constants.PARAMETER_FILE_PATH)
				.orElseThrow(() -> new NotFoundParameterException(Constants.PARAMETER_FILE_PATH));
		String basePath = parameter.getValue();

		// Build path from root location appending relative path received
		Path location = Paths.get(basePath, relativePath);
		// Build file complete path
		Path destinationFile = location.resolve(
				Paths.get(fileName))
				.normalize().toAbsolutePath();

		// Check file
		if (file.isEmpty())
			throw new StorageException("File must not be empty");
		// Check location
		if (!Files.exists(location))
			throw new ParameterInvalidException("RelativePath", relativePath);
		if (Files.exists(destinationFile))
			throw new FileAlreadyExistsException(fileName, relativePath);

		try {
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile);
				// Files.copy(inputStream, destinationFile,
				// StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new StorageException("Failed to store file", e);
		}
		// File has been written successfully

		return documentFile;
	}

	/**
	 * Gets a file identified by its internal code Id
	 * 
	 * @param id File internal code
	 * 
	 * @return File data
	 */
	@Override
	public DocumentFileDto get(Long id) {

		// Find document data
		DocumentFile documentFile = documentFileRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("DocumentFile", "id", String.valueOf(id)));

		return documentFile.mapDto();
	}

	/**
	 * Gets a file identified by its internal code Id
	 * 
	 * @param id File internal code
	 * 
	 * @return File data
	 * @throws FileNotFoundException
	 */
	@Override
	public DocumentFileDto getContent(Long id) {

		// Find document file data
		DocumentFile documentFile = documentFileRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("DocumentFile", "id", String.valueOf(id)));

		// Find the file root location in parameters
		Parameter parameter = parameterRepository.findByCode(Constants.PARAMETER_FILE_PATH)
				.orElseThrow(() -> new NotFoundParameterException(Constants.PARAMETER_FILE_PATH));
		String basePath = parameter.getValue();

		// Build path from root location appending relative path received
		Path location = Paths.get(basePath, documentFile.getFilePath());

		Path destinationFile = location.resolve(
				Paths.get(documentFile.getFileName()))
				.normalize().toAbsolutePath();

		// Create the file
		File file = new File(destinationFile.toString());

		// InputStreamResource resource;
		// try {
		// resource = new InputStreamResource(new FileInputStream(file));
		// } catch (FileNotFoundException e) {
		// throw new FileNotFoundException("File not found: " + file.getName());
		// }

		// Create the Dto and set the file in it
		DocumentFileDto documentFileDto = documentFile.mapDto();
		documentFileDto.setFile(file);

		return documentFileDto;
	}

	/**
	 * Updates a file identified by its internal code Id.
	 * Only allows to update:
	 * - valid
	 * - fileName
	 * 
	 * @param documentFileDto Document data to update
	 * 
	 * @return File new data after the update
	 */
	@Override
	@Transactional
	public DocumentFileDto modify(DocumentFileDto documentFileDto) {

		DocumentFile documentFile = documentFileRepository.findById(documentFileDto.getId()).orElseThrow(
				() -> new NotFoundException("DocumentFile", "id", String.valueOf(documentFileDto.getId())));

		// Check parameters that cannot be changed
		if ((documentFileDto.getFileName() != null) && documentFile.getFileName().equals(documentFileDto.getFileName()))
			throw new ParameterInvalidException("FileName", documentFileDto.getFileName());
		if ((documentFileDto.getFilePath() != null) && documentFile.getFilePath().equals(documentFileDto.getFilePath()))
			throw new ParameterInvalidException("FilePath", documentFileDto.getFilePath());
		// Check parameters that cannot be null
		if ((documentFileDto.getDisponible() == null))
			throw new ParameterEmptyException("Valid");

		// Save
		documentFile.prepareToSave();
		documentFile.setDisponible(documentFileDto.getDisponible());
		DocumentFile documentFileActualizado = documentFileRepository.save(documentFile);

		return documentFileActualizado.mapDto();
	}

	/**
	 * Deletes logically a file identified by Id, just invalidating it.
	 * 
	 * @param id File internal code
	 * 
	 * @return File data after the update
	 */
	@Override
	public DocumentFileDto delete(Long id) {
		DocumentFile documentFile = documentFileRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("DocumentFile", "id", String.valueOf(id)));

		// Save changes
		documentFile.prepareToSave();
		documentFile.setDisponible(false);
		DocumentFile saved = documentFileRepository.save(documentFile);

		return saved.mapDto();
	}

}