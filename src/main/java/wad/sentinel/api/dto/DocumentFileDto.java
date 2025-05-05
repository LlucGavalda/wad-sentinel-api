package wad.sentinel.api.dto;

import java.io.File;

import wad.sentinel.api.entity.DocumentFile;

public class DocumentFileDto extends AbstractDto {

	private String filePath;
	private String fileName;
	private String originalFileName;
	private String mediaType;
	private File file;
	private String extension;

	// Constructors -----------------------------

	public DocumentFileDto() {
		super();
	}

	public DocumentFileDto(DocumentFile entity) {
		super(entity);
		this.setFileName(entity.getFileName());
		this.setOriginalFileName(entity.getOriginalFileName());
		this.setFilePath(entity.getFilePath());
		this.setMediaType(entity.getMediaType());
		this.setFile(entity.getFile());
		this.setExtension(entity.getFileName().split("\\.")[1]);
	}

	// Methods -----------------------------

	public DocumentFile mapEntity() {
		return new DocumentFile(this);
	}

	// Getters and Setters -----------------------------

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

}
