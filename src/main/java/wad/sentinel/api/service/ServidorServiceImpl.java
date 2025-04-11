package wad.sentinel.api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wad.sentinel.api.dto.ServidorDto;
import wad.sentinel.api.entity.Servidor;
import wad.sentinel.api.exceptions.J3NotFoundException;
import wad.sentinel.api.repository.ServidorRepository;

@Service
public class ServidorServiceImpl implements ServidorService {

	private static final Logger logger = LoggerFactory.getLogger(ServidorServiceImpl.class);

	@Autowired
	private ServidorRepository entityRepository;

	@Override
	public List<ServidorDto> list(Boolean disponible) {
		logger.info("[Start] list...");

		List<Servidor> servidors = entityRepository.list(disponible);

		// Convertim la llista d'entitats a una llista de DTOs utilitzant el mètode
		// mapDto() de la classe Servidor
		List<ServidorDto> servidorDto = servidors.stream()
				.map(Servidor::mapDto) // Utilitzem el mètode mapDto() de Servidor per convertir l'entitat en DTO
				.collect(Collectors.toList());

		logger.info("[End] list.");
		return servidorDto;
	}

	@Override
	public ServidorDto get(Long id) {
		logger.info("[Start] get...");

		Optional<Servidor> existing = entityRepository.findById(id);
		if (!existing.isPresent()) {
			throw new J3NotFoundException("Servidor", "id", String.valueOf(id));
		}

		logger.info("[End] get.");

		return existing.get().mapDto();
	}

	@Override
	public ServidorDto create(ServidorDto dto) {
		logger.info("[Start] create...");

		// Crear entidad y obtener su Id
		Servidor entity = dto.mapEntity();
		entity.mapSystemFields(dto); // Mapear campos del DTO a la entidad
		entity.prepareToSave();
		Servidor saved = entityRepository.save(entity);

		logger.info("[End] create.");
		return saved.mapDto();
	}

	@Override
	public ServidorDto modify(ServidorDto dto) {
		logger.info("[Start] modify...");

		Servidor entity = entityRepository.findById(dto.getId())
				.orElseThrow(() -> new J3NotFoundException("Servidor", "id", String.valueOf(dto.getId())));

		// Actualizar entidad con datos del DTO
		entity.updateFrom(dto);
		entity.mapSystemFields(dto); // Mapear campos del DTO a la entidad
		entity.prepareToSave();
		Servidor updated = entityRepository.save(entity);

		logger.info("[End] modify.");
		return updated.mapDto();
	}

}
