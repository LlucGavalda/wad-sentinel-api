package wad.sentinel.api.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import wad.sentinel.api.dto.ServidorDto;
import wad.sentinel.api.entity.Servidor;
import wad.sentinel.api.exceptions.NotFoundException;
import wad.sentinel.api.repository.ServidorRepository;
import wad.sentinel.api.utils.NativeQueryHelper;
import wad.sentinel.api.utils.dto.SearchCriteriaDto;

@Service
public class ServidorServiceImpl extends AbstractService implements ServidorService {

	private static final Logger logger = LoggerFactory.getLogger(ServidorServiceImpl.class);

	@Autowired
	private ServidorRepository entityRepository;

	@Autowired
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked") // Avoid warning in cast of query.getResultList()
	public List<ServidorDto> list(SearchCriteriaDto[] searchCriteria) {
		logger.info("[Start] list...");
		if (searchCriteria == null) {
			searchCriteria = new SearchCriteriaDto[0];
		}
		Pageable pageable = preparePageable(0, -1);

		NativeQueryHelper queryHelper = new NativeQueryHelper(Servidor.class, 1);
		queryHelper.setSearchCriteria(searchCriteria);
		if (searchCriteria.length == 0) {
			queryHelper.setSearchCriteria(addFilterValid(searchCriteria));
		}
		PageImpl<Servidor> page = (PageImpl<Servidor>) queryHelper.getQueryResultPage(entityManager,
				pageable);

		logger.info("[End] list.");
		return page.getContent().stream().map(Servidor::mapDto).toList();
	}

	@Override
	public ServidorDto get(Long id) {
		logger.info("[Start] get...");

		Optional<Servidor> existing = entityRepository.findById(id);
		if (!existing.isPresent()) {
			throw new NotFoundException("Servidor", "id", String.valueOf(id));
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
				.orElseThrow(() -> new NotFoundException("Servidor", "id", String.valueOf(dto.getId())));

		// Actualizar entidad con datos del DTO
		entity.updateFrom(dto);
		entity.mapSystemFields(dto); // Mapear campos del DTO a la entidad
		entity.prepareToSave();
		Servidor updated = entityRepository.save(entity);

		logger.info("[End] modify.");
		return updated.mapDto();
	}
}
