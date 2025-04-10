package wad.sentinel.api.service;

import java.util.List;

import wad.sentinel.api.dto.ServidorDto;

public interface ServidorService {

	public List<ServidorDto> list(Boolean disponible);

	public ServidorDto get(Long id);

	public ServidorDto create(ServidorDto dto);

	public ServidorDto modify(ServidorDto dto);

}
