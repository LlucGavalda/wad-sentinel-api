package wad.sentinel.api.entity;

import java.io.File;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import wad.sentinel.api.dto.DocumentFileDto;

@Entity
@Table(name = "documents")
@Audited
public class DocumentFile extends AbstractEntity {

	private static final long serialVersionUID = -5478677145557373673L;

	// public static final String PAGE_GROUP = Constants.PAGE_GROUP_GENERIC;
	// public static final String PAGE_ID = Constants.PAGE_ID_DOCUMENT_FILES;

	public static final String CODE_DESCRIPTION = "DESCRIPCIO_";

	@Column(name = "ruta_fitxer", length = 255, nullable = false)
	private String filePath;

	@Column(name = "nom_fitxer", length = 255, nullable = false)
	private String fileName;

	@Column(name = "nom_original_fitxer", length = 255, nullable = false)
	private String OriginalFileName;

	@Column(name = "tipus_fitxer", length = 255)
	private String mediaType;

	@Transient
	private File file;

	// Constructors -----------------------------
	public DocumentFile() {
		super();
	}

	public DocumentFile(DocumentFileDto dto) {
		super(dto);
		this.setFileName(dto.getFileName());
		this.setFilePath(dto.getFilePath());
		this.setOriginalFileName(dto.getOriginalFileName());
		this.setMediaType(dto.getMediaType());
		this.setFile(dto.getFile());
	}

	// Methods -----------------------------

	public DocumentFileDto mapDto() {
		return new DocumentFileDto(this);
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
		return OriginalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		OriginalFileName = originalFileName;
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

}
