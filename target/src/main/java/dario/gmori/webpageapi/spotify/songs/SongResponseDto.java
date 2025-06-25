package dario.gmori.webpageapi.spotify.songs;

import com.fasterxml.jackson.annotation.JsonProperty;
import dario.gmori.webpageapi.spotify.artists.ArtistResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SongResponseDto {
    private String name;

    @JsonProperty("cover_url")
    private String coverUrl;

    private String url;

    @JsonProperty("duration_ms")
    private int durationMs;

    private List<ArtistResponseDto> artists;
}
