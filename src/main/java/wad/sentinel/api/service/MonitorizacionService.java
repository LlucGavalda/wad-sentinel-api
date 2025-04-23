package wad.sentinel.api.service;

import java.util.List;

import wad.sentinel.api.dto.MonitorizacionDto;
import wad.sentinel.api.dto.MonitorizacionPage;
import wad.sentinel.api.entity.Monitorizacion;
import wad.sentinel.api.utils.dto.SearchCriteriaDto;

public interface MonitorizacionService {

	public MonitorizacionPage list(Integer pageNumber, Integer pageSize, SearchCriteriaDto[] dto);

	public List<Monitorizacion> last();

	public MonitorizacionDto get(Long id);

	public MonitorizacionDto create(MonitorizacionDto dto);

}
