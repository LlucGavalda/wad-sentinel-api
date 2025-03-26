package wad.sentinel.security.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import wad.sentinel.security.entity.UserAuth;

public interface UserAuthRepository extends JpaRepository<UserAuth, String> {

	@Query(value = "SELECT u FROM UserAuth u ")
	Page<UserAuth> search(Pageable pageable);

	Optional<UserAuth> findByUsername(String usuario);

	Boolean existsByUsername(String usuario);
}
