package wad.sentinel.api.service;

import wad.sentinel.api.utils.dto.EmailDto;

public interface EmailService {

	public EmailDto send(EmailDto d, Boolean html);

}
