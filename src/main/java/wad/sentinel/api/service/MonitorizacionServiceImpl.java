package wad.sentinel.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wad.sentinel.api.dto.MonitorizacionDto;
import wad.sentinel.api.entity.Monitorizacion;
import wad.sentinel.api.repository.MonitorizacionRepository;

@Service
public class MonitorizacionServiceImpl implements MonitorizacionService {

	private static final Logger logger = LoggerFactory.getLogger(ServidorServiceImpl.class);

	@Autowired
	private MonitorizacionRepository entityRepository;

	@Override
	public MonitorizacionDto create(MonitorizacionDto dto) {
		logger.info("[Start] create...");

		// Crear entidad y obtener su Id
		Monitorizacion entity = dto.mapEntity();
		entity.mapSystemFields(dto); // Mapear campos del DTO a la entidad
		entity.prepareToSave();
		Monitorizacion saved = entityRepository.save(entity);

		logger.info("[End] create.");
		return saved.mapDto();
	}
}
