package wad.sentinel.api.service;

import java.util.List;

import wad.sentinel.api.dto.MonitorizacionDto;

public interface MonitorizacionService {

	public List<MonitorizacionDto> list(Integer pageNumber, Integer pageSize, Boolean disponible, Boolean incidencia,
			String fechaDesde, String fechaHasta, Long idServidor);

	public MonitorizacionDto get(Long id);

	public MonitorizacionDto create(MonitorizacionDto dto);

}
