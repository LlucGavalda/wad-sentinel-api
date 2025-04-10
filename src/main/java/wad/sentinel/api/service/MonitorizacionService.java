package wad.sentinel.api.service;

import java.sql.Timestamp;
import java.util.List;

import wad.sentinel.api.dto.MonitorizacionDto;

public interface MonitorizacionService {

	public List<MonitorizacionDto> list(Boolean disponible, Boolean incidencia, Timestamp fechaDesde,
			Timestamp fechaHasta, Long idServidor);

	public MonitorizacionDto get(Long id);

	public MonitorizacionDto create(MonitorizacionDto dto);

}
