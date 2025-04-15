package wad.sentinel.api.service;

import wad.sentinel.api.dto.MonitorizacionDto;
import wad.sentinel.api.dto.MonitorizacionPage;
import wad.sentinel.api.utils.dto.SearchCriteriaDto;

public interface MonitorizacionService {

	public MonitorizacionPage list(Integer pageNumber, Integer pageSize, SearchCriteriaDto[] dto);

	public MonitorizacionDto get(Long id);

	public MonitorizacionDto create(MonitorizacionDto dto);

}
