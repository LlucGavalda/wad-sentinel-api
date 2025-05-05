package wad.sentinel.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wad.sentinel.api.service.EmailService;
import wad.sentinel.api.utils.dto.EmailDto;

@RestController
@RequestMapping("/wadsentinel/api/tools/email")
public class EmailController {

	@Autowired
	private EmailService entityService;

	/**
	 * Sends an email.
	 * 
	 * URL: /api/tools/email
	 * Parameters DTO:
	 * toAddress
	 * ccAddress[]
	 * ccoAddress[]
	 * subject
	 * content
	 * Request:
	 * html (optional): indicator wether the body is in HTML format. TEXT otherwise.
	 * 
	 * @param dto
	 * @param html
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> sendMail(
			@RequestBody EmailDto dto,
			@RequestParam(value = "html", defaultValue = "0", required = false) Boolean html) {
		entityService.send(dto, html);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}