package wad.sentinel.api.repository;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import wad.sentinel.api.entity.Parameter;

public interface ParameterRepository extends AbstractRepository, JpaRepository<Parameter, Long> {

	@Cacheable(value = Parameter.cacheName)
	@Query(value = "SELECT r FROM Parameter r "
			+ "WHERE ( :disponible is null OR r.disponible = :disponible ) ")
	Page<Parameter> search(@Param("disponible") Boolean disponible, Pageable pageable);

	@Cacheable(value = Parameter.cacheName)
	@Query(value = "SELECT p FROM Parameter p "
			+ "WHERE p.code = :code ")
	Optional<Parameter> findByCode(@Param("code") String code);

}
