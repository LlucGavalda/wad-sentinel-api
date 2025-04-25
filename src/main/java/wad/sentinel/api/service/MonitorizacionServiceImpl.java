package wad.sentinel.api.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import wad.sentinel.api.dto.MonitorizacionDto;
import wad.sentinel.api.dto.MonitorizacionPage;
import wad.sentinel.api.dto.ServidorDto;
import wad.sentinel.api.entity.Monitorizacion;
import wad.sentinel.api.entity.MonitorizacionDisco;
import wad.sentinel.api.entity.MonitorizacionMemoria;
import wad.sentinel.api.entity.MonitorizacionProcesador;
import wad.sentinel.api.entity.MonitorizacionPuerto;
import wad.sentinel.api.entity.MonitorizacionTemperatura;
import wad.sentinel.api.entity.Servidor;
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

	@Autowired
	private ServidorService servidorService;

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
	}

	@Override
	public List<Monitorizacion> last() {
		logger.info("[Start] last...");

		List<Monitorizacion> monitorizacions = new ArrayList<Monitorizacion>();

		List<ServidorDto> servidors = servidorService.list(null);

		for (int i = 0; i < servidors.size(); i++) {
			Monitorizacion monitorizacion = entityRepository.last(servidors.get(i).getId());
			if (monitorizacion != null) {
				monitorizacions.add(monitorizacion);
			}
		}

		logger.info("[End] last.");
		return monitorizacions;
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

	@Transactional
	@Override
	public MonitorizacionDto create(MonitorizacionDto dto) {
		logger.info("[Start] create...");

		Monitorizacion entity = dto.mapEntity();

		Servidor servidor = servidorRepository.findById(dto.getServidor().getId())
				.orElseThrow(() -> new IllegalArgumentException(
						"Servidor no encontrado con ID: " + dto.getServidor().getId()));

		MonitorizacionMemoria memoria = entity.getMemoria();
		memoria = memoriaRepository.save(memoria);

		MonitorizacionProcesador procesador = entity.getProcesador();
		procesador = procesadorRepository.save(procesador);

		// Persistir los discos
		Set<MonitorizacionDisco> discos = new HashSet<>();
		if (entity.getDiscos() != null) {
			for (MonitorizacionDisco monitorizacionDisco : entity.getDiscos()) {
				monitorizacionDisco = entityManager.merge(monitorizacionDisco);
				discos.add(monitorizacionDisco);
			}
		}

		// Asociamos los puertos y temperaturas
		Set<MonitorizacionPuerto> puertos = new HashSet<>();
		if (entity.getPuertos() != null) {
			for (MonitorizacionPuerto monitorizacionPuerto : entity.getPuertos()) {
				monitorizacionPuerto = entityManager.merge(monitorizacionPuerto);
				puertos.add(monitorizacionPuerto);
			}
		}

		Set<MonitorizacionTemperatura> temperaturas = new HashSet<>();
		if (entity.getTemperaturas() != null) {
			for (MonitorizacionTemperatura monitorizacionTemperatura : entity.getTemperaturas()) {
				monitorizacionTemperatura = entityManager.merge(monitorizacionTemperatura);
				temperaturas.add(monitorizacionTemperatura);
			}
		}

		entity.setId(null);
		entity.prepareToSave();
		entity.setFecha(new Timestamp(System.currentTimeMillis()));

		entity.setServidor(servidor);
		entity.setMemoria(memoria);
		entity.setProcesador(procesador);
		entity.setDiscos(discos);
		entity.setPuertos(puertos);
		entity.setTemperaturas(temperaturas);

		Monitorizacion savedEntity = entityRepository.save(entity);

		logger.info("[End] create.");
		return savedEntity.mapDto();
	}

}
