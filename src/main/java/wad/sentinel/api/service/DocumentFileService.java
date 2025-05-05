package wad.sentinel.api.service;

import org.springframework.web.multipart.MultipartFile;

import wad.sentinel.api.dto.DocumentFileDto;
import wad.sentinel.api.entity.DocumentFile;

public interface DocumentFileService {

	public DocumentFileDto get(Long id);

	public DocumentFileDto getContent(Long id);

	public DocumentFile create(MultipartFile file, String path);

	public DocumentFile create(MultipartFile file, String path, String name);

	public DocumentFileDto modify(DocumentFileDto dto);

	public DocumentFileDto delete(Long id);

}
