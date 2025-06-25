package dario.gmori.webpageapi.spotify.artists;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ArtistResponseDto {
    private String name;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("profile_url")
    private String profileUrl;

    private Integer followers;

    private String genres;
}
