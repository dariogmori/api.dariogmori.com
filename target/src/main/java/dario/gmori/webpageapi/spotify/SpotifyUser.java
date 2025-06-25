package dario.gmori.webpageapi.spotify;

import dario.gmori.webpageapi.spotify.artists.Artist;
import dario.gmori.webpageapi.spotify.songs.Song;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "spotify_users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SpotifyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    private String avatar;

    @Column(name="profile_url")
    private String profileUrl;

    @Column(name="last_modified_date")
    private LocalDateTime lastModifiedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Fetch(value = FetchMode.SELECT)
    private List<Song> topSongs;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Artist> topArtists;

    public boolean timeToRefreshPassed(int seconds){
        if(lastModifiedDate == null)
            return true;
        return LocalDateTime.now().isAfter(lastModifiedDate.plusSeconds(seconds));
    }

    public void updateUser(SpotifyUser apply, List<Song> songsList, List<Artist> artistList) {
        this.username = apply.getUsername();
        this.avatar = apply.getAvatar();
        this.profileUrl = apply.getProfileUrl();
        this.topSongs = songsList;
        this.topArtists = artistList;
        this.setLastModifiedDate(LocalDateTime.now());
    }
}
