package wad.sentinel.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wad.sentinel.api.entity.MonitorizacionMemoria;

@Repository
public interface MonitorizacionMemoriaRepository extends CrudRepository<MonitorizacionMemoria, Long> {
}
