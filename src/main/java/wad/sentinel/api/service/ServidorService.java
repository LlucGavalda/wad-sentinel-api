package wad.sentinel.api.service;

import java.util.List;

import wad.sentinel.api.dto.ServidorDto;
import wad.sentinel.api.utils.dto.SearchCriteriaDto;

public interface ServidorService {

	public List<ServidorDto> list(SearchCriteriaDto[] dto);

	public ServidorDto get(Long id);

	public ServidorDto create(ServidorDto dto);

	public ServidorDto modify(ServidorDto dto);

}
