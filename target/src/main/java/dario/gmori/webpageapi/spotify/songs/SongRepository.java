package dario.gmori.webpageapi.spotify.songs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {
    @Query(value = "SELECT s FROM Song s WHERE s.songId = ?1 ")
    Optional<Song> findBySongId(String songId);
}
