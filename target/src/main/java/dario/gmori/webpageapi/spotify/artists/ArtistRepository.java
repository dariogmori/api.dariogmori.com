package dario.gmori.webpageapi.spotify.artists;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {
    @Query(value = "SELECT a FROM Artist a WHERE a.name = ?1 ")
    Optional<Artist> findByName(String name);
}
