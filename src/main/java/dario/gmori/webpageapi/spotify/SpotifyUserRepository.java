package dario.gmori.webpageapi.spotify;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpotifyUserRepository extends JpaRepository<SpotifyUser, Long> {
    @Query(value = "SELECT s.* FROM spotify_users s WHERE s.id = 1;", nativeQuery = true)
    Optional<SpotifyUser> findUserInfo();
}
