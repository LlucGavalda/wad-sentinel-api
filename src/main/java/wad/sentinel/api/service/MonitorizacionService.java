package wad.sentinel.api.service;

import java.util.List;

import wad.sentinel.api.dto.MonitorizacionDto;

public interface MonitorizacionService {

	public List<MonitorizacionDto> list(Boolean disponible);

	public MonitorizacionDto get(Long id);

	public MonitorizacionDto create(MonitorizacionDto dto);

}
