package wad.sentinel.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wad.sentinel.api.entity.MonitorizacionProcesador;

@Repository
public interface MonitorizacionProcesadorRepository extends CrudRepository<MonitorizacionProcesador, Long> {
}
