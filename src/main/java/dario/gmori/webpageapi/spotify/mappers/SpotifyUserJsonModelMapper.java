package dario.gmori.webpageapi.spotify.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import dario.gmori.webpageapi.spotify.SpotifyUser;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class SpotifyUserJsonModelMapper implements Function<JsonNode, SpotifyUser> {
    @Override
    public SpotifyUser apply(JsonNode jsonNode) {
        return SpotifyUser.builder()
                .username(jsonNode.get("display_name").asText())
                .avatar(jsonNode.get("images").get(0).get("url").asText())
                .profileUrl(jsonNode.get("external_urls").get("spotify").asText())
                .build();
    }

}
