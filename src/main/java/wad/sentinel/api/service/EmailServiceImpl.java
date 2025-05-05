package wad.sentinel.api.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import wad.sentinel.api.utils.Email;
import wad.sentinel.api.utils.dto.EmailDto;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private Environment env;

	@Autowired
	private DocumentFileService documentFileService;

	/**
	 * Send an email from the data received in the dto.
	 * The server parameters will be taken from the properties under
	 * com.cronda.juridic3.api.common.smtp_origin_user
	 * com.cronda.juridic3.api.common.smtp_server
	 * com.cronda.juridic3.api.common.smtp_port
	 * com.cronda.juridic3.api.common.smtp_user
	 * com.cronda.juridic3.api.common.smtp_password
	 * The attachments referenced by Id in the dto will be taken from the
	 * DocumentFile definition in the system, so they must previosly
	 * exist and be registered as DocumentFile.
	 * 
	 * @Param dto Dto that stores all the mail data
	 */
	@Override
	public EmailDto send(EmailDto dto, Boolean html) {

		// Get environment parameters
		String fromAddress = env.getProperty("spring.mail.from");
		String server = env.getProperty("spring.mail.host");
		String port = env.getProperty("spring.mail.port");
		String user = env.getProperty("spring.mail.username");
		String password = env.getProperty("spring.mail.password");

		// Set default values, if needed
		if (dto.getFromAddress() == null)
			dto.setFromAddress(fromAddress);

		// Create the email
		Email email = new Email(server, port, user, password);
		email.compose(dto.getToAddress(), dto.getFromAddress(), dto.getSubject(), dto.getContent(), html);
		if (dto.getCcAddress() != null) {
			for (int i = 0; i < dto.getCcAddress().length; i++)
				email.addAddressCC(dto.getCcAddress()[i]);
		}
		if (dto.getCcoAddress() != null) {
			for (int i = 0; i < dto.getCcoAddress().length; i++)
				email.addAddressCCO(dto.getCcoAddress()[i]);
		}
		if (dto.getFiles() != null) {
			Long fileId;
			File file;
			for (int i = 0; i < dto.getFiles().length; i++) {
				fileId = dto.getFiles()[i].getId();
				file = documentFileService.getContent(fileId).getFile();
				email.addAttachment(file);
			}
		}

		// Send
		email.send();

		return dto;
	}

}
