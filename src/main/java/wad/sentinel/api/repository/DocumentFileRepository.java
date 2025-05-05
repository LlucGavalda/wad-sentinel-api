package wad.sentinel.api.repository;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import wad.sentinel.api.entity.DocumentFile;

public interface DocumentFileRepository extends AbstractRepository, JpaRepository<DocumentFile, Long> {

	@CacheEvict(cacheNames = "document_files", allEntries = true)
	@Query(value = "SELECT t FROM DocumentFile t "
			+ "WHERE t.id = :id ")
	Optional<DocumentFile> findById(@Param("id") Long id);

	// @Query(
	// value="SELECT t FROM DocumentFile t "
	// + "WHERE ( :valid is null OR t.valid = :valid ) "
	// )
	// Page<DocumentFile> search(@Param("valid") Boolean valid, Pageable pageable);

}
