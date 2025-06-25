package dario.gmori.webpageapi.spotify.artists.mappers;

import dario.gmori.webpageapi.spotify.artists.Artist;
import dario.gmori.webpageapi.spotify.artists.ArtistResponseDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class SpotifyArtistResponseDtoMapper implements Function<Artist, ArtistResponseDto> {
    @Override
    public ArtistResponseDto apply(Artist artist) {
        return ArtistResponseDto.builder()
                .name(artist.getName())
                .profileUrl(artist.getProfileUrl())
                .avatarUrl(artist.getAvatarUrl())
                .followers(artist.getFollowers())
                .genres(artist.getGenres())
                .build();
    }
}
