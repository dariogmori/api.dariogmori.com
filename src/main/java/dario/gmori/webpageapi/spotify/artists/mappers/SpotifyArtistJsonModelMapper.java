package dario.gmori.webpageapi.spotify.artists.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import dario.gmori.webpageapi.spotify.artists.Artist;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class SpotifyArtistJsonModelMapper implements Function<JsonNode, List<Artist>> {
    private String getCoverImageWithSmallestSize(JsonNode artist)
    {
        if(artist.has("images")){
            if (artist.get("images").size() == 1) {
                return artist.get("images").get(0).get("url").asText();
            } else {
                return artist.get("images").get(artist.get("images").size() - 1).get("url").asText(); // Return the smallest image
            }
        }else {
            return null; // Return an empty string if no images are available
        }
    }
    @Override
    public List<Artist> apply(JsonNode jsonNode) {
        List<Artist> artists = new ArrayList<>();

        jsonNode.forEach(artist -> {
            artists.add(Artist.builder()
                    .name(artist.get("name").asText())
                    .avatarUrl(getCoverImageWithSmallestSize(artist))
                    .profileUrl(artist.get("external_urls").get("spotify").asText())
                    .followers(artist.has("followers") ? artist.get("followers").get("total").asInt() : 0)
                    .genres(artist.has("genres") ? artist.get("genres").toString() : "")
                    .build());
        });
        return artists;
    }
}
