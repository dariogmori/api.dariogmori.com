package dario.gmori.webpageapi.spotify.songs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dario.gmori.webpageapi.spotify.SpotifyUser;
import dario.gmori.webpageapi.spotify.artists.Artist;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "songs")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown=true)
@Data
@EqualsAndHashCode(exclude={"artists","user"})
@ToString
public class Song {

    @Id
    @Column(unique=true,name="song_id")
    private String songId;

    @Column(name="song_name")
    private String name;

    @Column(name="cover_url")
    private String coverImageUrl;

    @Column(name="song_url")
    private String url;

    @Column(name="duration_ms")
    private int durationMs;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "artist_song",
            joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    @Fetch(value = FetchMode.SELECT)
    private List<Artist> artists;

    @ManyToOne
    @JoinColumn(name = "spotify_user")
    @JsonIgnore
    @ToString.Exclude
    private SpotifyUser user;

}
