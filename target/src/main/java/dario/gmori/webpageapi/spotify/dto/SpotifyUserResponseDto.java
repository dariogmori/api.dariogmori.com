package dario.gmori.webpageapi.spotify.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dario.gmori.webpageapi.spotify.artists.ArtistResponseDto;
import dario.gmori.webpageapi.spotify.songs.SongResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpotifyUserResponseDto {
    private String username;

    private String avatar;

    @JsonProperty("profile_url")
    private String profileUrl;

    @JsonProperty("top_songs")
    private List<SongResponseDto> topSongs;

    @JsonProperty("top_artists")
    private List<ArtistResponseDto> topArtists;
}
