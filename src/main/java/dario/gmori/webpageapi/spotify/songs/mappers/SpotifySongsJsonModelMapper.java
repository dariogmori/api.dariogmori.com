package dario.gmori.webpageapi.spotify.songs.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import dario.gmori.webpageapi.spotify.artists.mappers.SpotifyArtistJsonModelMapper;
import dario.gmori.webpageapi.spotify.songs.Song;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class SpotifySongsJsonModelMapper implements Function<JsonNode, List<Song>> {
    private final SpotifyArtistJsonModelMapper artistMapper;
    private String getCoverImageWithSmallestSize(JsonNode song)
    {
        if (song.get("album").get("images").size() == 1) {
            return song.get("album").get("images").get(0).get("url").asText();
        }else {
            return song.get("album").get("images").get(song.get("album").get("images").size() - 1).get("url").asText(); // Return the smallest image
        }
    }
    @Override
    public List<Song> apply(JsonNode jsonNode) {
        List<Song> songs = new ArrayList<>();
        jsonNode.get("items").forEach(song -> {
            songs.add(Song.builder()
                    .name(song.get("name").asText())
                    .artists(artistMapper.apply(song.get("artists")))
                    .coverImageUrl(getCoverImageWithSmallestSize(song))
                    .url(song.get("external_urls").get("spotify").asText())
                    .durationMs(song.get("duration_ms").asInt())
                    .songId(song.get("id").asText())
                    .build());
        });
        return songs;
    }
}
