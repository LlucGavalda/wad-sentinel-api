package wad.sentinel.api.service;

import java.sql.Timestamp;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import wad.sentinel.api.dto.MonitorizacionDto;
import wad.sentinel.api.dto.MonitorizacionPage;
import wad.sentinel.api.entity.Monitorizacion;
import wad.sentinel.api.exceptions.NotFoundException;
import wad.sentinel.api.repository.MonitorizacionMemoriaRepository;
import wad.sentinel.api.repository.MonitorizacionProcesadorRepository;
import wad.sentinel.api.repository.MonitorizacionRepository;
import wad.sentinel.api.repository.ServidorRepository;
import wad.sentinel.api.utils.NativeQueryHelper;
import wad.sentinel.api.utils.dto.SearchCriteriaDto;

@Service
public class MonitorizacionServiceImpl extends AbstractService implements MonitorizacionService {

	private static final Logger logger = LoggerFactory.getLogger(MonitorizacionServiceImpl.class);

	@Autowired
	private MonitorizacionRepository entityRepository;

	@Autowired
	private ServidorRepository servidorRepository;

	@Autowired
	private MonitorizacionMemoriaRepository memoriaRepository;

	@Autowired
	private MonitorizacionProcesadorRepository procesadorRepository;

	@Autowired
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked") // Avoid warning in cast of query.getResultList()
	public MonitorizacionPage list(Integer pageNumber, Integer pageSize, SearchCriteriaDto[] searchCriteria) {

		logger.info("[Start] list...");

		Pageable pageable = preparePageable(pageNumber, pageSize);

		NativeQueryHelper queryHelper = new NativeQueryHelper(Monitorizacion.class, 1);
		queryHelper.setSearchCriteria(addFilterValid(searchCriteria));

		PageImpl<Monitorizacion> page = (PageImpl<Monitorizacion>) queryHelper.getQueryResultPage(entityManager,
				pageable);

		logger.info("[End] list.");
		return new MonitorizacionPage(page);

		// Timestamp fechaDesdeTimestamp = parseFecha(fechaDesde);
		// Timestamp fechaHastaTimestamp = parseFecha(fechaHasta);

		// List<Monitorizacion> monitorizaciones = entityRepository.list(disponible,
		// incidencia, fechaDesdeTimestamp,
		// fechaHastaTimestamp, idServidor);

		// // Convertim la llista d'entitats a una llista de DTOs utilitzant el mètode
		// // mapDto() de la classe Monitorizacion
		// List<MonitorizacionDto> monitorizacionDto = monitorizaciones.stream()
		// .map(Monitorizacion::mapDto) // Utilitzem el mètode mapDto() de
		// Monitorizacion per convertir l'entitat
		// // en DTO
		// .collect(Collectors.toList());
	}

	@Override
	public MonitorizacionDto get(Long id) {
		logger.info("[Start] get...");

		Optional<Monitorizacion> existing = entityRepository.findById(id);
		if (!existing.isPresent()) {
			throw new NotFoundException("Monitorizacion", "id", String.valueOf(id));
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
