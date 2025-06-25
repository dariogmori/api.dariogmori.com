package dario.gmori.webpageapi.spotify.songs.mappers;

import dario.gmori.webpageapi.spotify.artists.mappers.SpotifyArtistResponseDtoMapper;
import dario.gmori.webpageapi.spotify.songs.Song;
import dario.gmori.webpageapi.spotify.songs.SongResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class SpotifySongResponseDtoMapper implements Function<Song, SongResponseDto> {
    private final SpotifyArtistResponseDtoMapper spotifyArtistResponseDtoMapper;
    @Override
    public SongResponseDto apply(Song song) {
        return SongResponseDto.builder()
                .url(song.getUrl())
                .name(song.getName())
                .coverUrl(song.getCoverImageUrl())
                .durationMs(song.getDurationMs())
                .artists(song.getArtists().stream().map(spotifyArtistResponseDtoMapper).toList())
                .build();
    }
}
