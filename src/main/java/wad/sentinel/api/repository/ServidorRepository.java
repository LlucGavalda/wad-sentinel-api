package wad.sentinel.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import wad.sentinel.api.entity.Servidor;

public interface ServidorRepository extends JpaRepository<Servidor, Long>, JpaSpecificationExecutor<Servidor> {
	
	@SuppressWarnings("null")
	@Query(
			value="SELECT t FROM Servidor t "
					+ "WHERE t.id = :id "
	)	
	Optional<Servidor> findById(@Param("id") Long id);
	

}
