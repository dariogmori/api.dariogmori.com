package dario.gmori.webpageapi.spotify.artists.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import dario.gmori.webpageapi.spotify.artists.Artist;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Service
public class SpotifyArtistJsonModelMapper implements Function<JsonNode, List<Artist>> {
    @Override
    public List<Artist> apply(JsonNode jsonNode) {
        List<Artist> artists = new ArrayList<>();

        jsonNode.forEach(artist -> {
            artists.add(Artist.builder()
                    .name(artist.get("name").asText())
                    .avatarUrl(artist.has("images") ? artist.get("images").get(0).get("url").asText() : null)
                    .profileUrl(artist.get("external_urls").get("spotify").asText())
                    .followers(artist.has("followers") ? artist.get("followers").get("total").asInt() : 0)
                    .genres(artist.has("genres") ? artist.get("genres").toString() : "")
                    .build());
        });
        return artists;
    }
}
