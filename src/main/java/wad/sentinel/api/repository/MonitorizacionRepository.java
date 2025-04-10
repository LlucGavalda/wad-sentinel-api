package wad.sentinel.api.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import wad.sentinel.api.entity.Monitorizacion;

public interface MonitorizacionRepository
		extends JpaRepository<Monitorizacion, Long>, JpaSpecificationExecutor<Monitorizacion> {
	@Query(value = "SELECT t FROM Monitorizacion t "
			+ "WHERE (:disponible IS NULL OR t.disponible = :disponible) "
			+ "AND (:incidencia IS NULL OR t.incidencia = :incidencia) "
			+ "AND (:fechaDesde IS NULL OR t.fecha >= :fechaDesde) "
			+ "AND (:fechaHasta IS NULL OR t.fecha <= :fechaHasta) "
			+ "AND (:idServidor IS NULL OR t.servidor.id = :idServidor) "
			+ "ORDER BY t.fecha DESC")
	List<Monitorizacion> list(
			@Param("disponible") Boolean disponible,
			@Param("incidencia") Boolean incidencia,
			@Param("fechaDesde") Timestamp fechaDesde,
			@Param("fechaHasta") Timestamp fechaHasta,
			@Param("idServidor") Long idServidor);

	@SuppressWarnings("null")
	@Query(value = "SELECT t FROM Monitorizacion t "
			+ "WHERE t.id = :id ")
	Optional<Monitorizacion> findById(@Param("id") Long id);

}
