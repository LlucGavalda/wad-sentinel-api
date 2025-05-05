package wad.sentinel.api.utils.dto;

import wad.sentinel.api.dto.DocumentFileDto;

public class EmailDto {

	private String fromAddress;
	private String toAddress;
	private String ccAddress[];
	private String ccoAddress[];
	private String subject;
	private String content;
	private DocumentFileDto files[];

	// Constructors -----------------------------

	public EmailDto() {
		super();
	}

	// Getters and Setters -----------------------------

	public DocumentFileDto[] getFiles() {
		return files;
	}

	public String[] getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(String[] ccAddress) {
		this.ccAddress = ccAddress;
	}

	public String[] getCcoAddress() {
		return ccoAddress;
	}

	public void setCcoAddress(String[] ccoAddress) {
		this.ccoAddress = ccoAddress;
	}

	public void setFiles(DocumentFileDto[] files) {
		this.files = files;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
