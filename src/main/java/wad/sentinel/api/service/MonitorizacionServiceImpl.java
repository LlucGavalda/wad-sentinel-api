package wad.sentinel.api.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wad.sentinel.api.dto.MonitorizacionDto;
import wad.sentinel.api.entity.Monitorizacion;
import wad.sentinel.api.exceptions.J3NotFoundException;
import wad.sentinel.api.repository.MonitorizacionMemoriaRepository;
import wad.sentinel.api.repository.MonitorizacionProcesadorRepository;
import wad.sentinel.api.repository.MonitorizacionRepository;
import wad.sentinel.api.repository.ServidorRepository;

@Service
public class MonitorizacionServiceImpl implements MonitorizacionService {

	private static final Logger logger = LoggerFactory.getLogger(MonitorizacionServiceImpl.class);

	@Autowired
	private MonitorizacionRepository entityRepository;

	@Autowired
	private ServidorRepository servidorRepository;

	@Autowired
	private MonitorizacionMemoriaRepository memoriaRepository;

	@Autowired
	private MonitorizacionProcesadorRepository procesadorRepository;

	@Override
	public List<MonitorizacionDto> list(Boolean disponible, Boolean incidencia,
			String fechaDesde, String fechaHasta, Long idServidor) {
		logger.info("[Start] list...");

		Timestamp fechaDesdeTimestamp = parseFecha(fechaDesde);
		Timestamp fechaHastaTimestamp = parseFecha(fechaHasta);

		List<Monitorizacion> monitorizaciones = entityRepository.list(disponible, incidencia, fechaDesdeTimestamp,
				fechaHastaTimestamp, idServidor);

		// Convertim la llista d'entitats a una llista de DTOs utilitzant el mètode
		// mapDto() de la classe Monitorizacion
		List<MonitorizacionDto> monitorizacionDto = monitorizaciones.stream()
				.map(Monitorizacion::mapDto) // Utilitzem el mètode mapDto() de Monitorizacion per convertir l'entitat
												// en DTO
				.collect(Collectors.toList());

		logger.info("[End] list.");
		return monitorizacionDto;
	}

	@Override
	public MonitorizacionDto get(Long id) {
		logger.info("[Start] get...");

		Optional<Monitorizacion> existing = entityRepository.findById(id);
		if (!existing.isPresent()) {
			throw new J3NotFoundException("Monitorizacion", "id", String.valueOf(id));
		}

		logger.info("[End] get.");

		return existing.get().mapDto();
	}

	@Override
	public MonitorizacionDto create(MonitorizacionDto dto) {
		logger.info("[Start] create...");

		Monitorizacion entity = dto.mapEntity();

		// Cargar el servidor desde la base de datos
		if (entity.getServidor() != null && entity.getServidor().getId() != null) {
			entity.setServidor(servidorRepository.findById(entity.getServidor().getId())
					.orElseThrow(() -> new IllegalArgumentException(
							"Servidor no encontrado con ID: " + entity.getServidor().getId())));
		}

		// Preparar la entidad principal
		entity.prepareToSave();
		entity.setMemoria(memoriaRepository.save(entity.getMemoria()));
		entity.setProcesador(procesadorRepository.save(entity.getProcesador()));
		Monitorizacion savedEntity = entityRepository.save(entity);

		logger.info("[End] create.");
		return savedEntity.mapDto();
	}

	private Timestamp parseFecha(String fecha) {
		if (fecha == null || fecha.isEmpty()) {
			return null;
		}

		try {
			if (fecha.matches("\\d{4}")) { // Formato: "2025"
				return Timestamp.valueOf(fecha + "-01-01 00:00:00");
			} else if (fecha.matches("\\d{4}-\\d{2}")) { // Formato: "2025-04"
				return Timestamp.valueOf(fecha + "-01 00:00:00");
			} else if (fecha.matches("\\d{4}-\\d{2}-\\d{2}")) { // Formato: "2025-04-11"
				return Timestamp.valueOf(fecha + " 00:00:00");
			} else if (fecha.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")) { // Formato: "2025-04-11 14:30"
				return Timestamp.valueOf(fecha + ":00");
			} else {
				throw new IllegalArgumentException("Formato de fecha no válido: " + fecha);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Error al parsear la fecha: " + fecha, e);
		}
	}
}
