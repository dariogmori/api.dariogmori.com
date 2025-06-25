package dario.gmori.webpageapi.spotify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dario.gmori.webpageapi.common.requests.spotify.SpotifyRequestUtils;
import dario.gmori.webpageapi.spotify.artists.Artist;
import dario.gmori.webpageapi.spotify.artists.ArtistRepository;
import dario.gmori.webpageapi.spotify.artists.ArtistResponseDto;
import dario.gmori.webpageapi.spotify.artists.mappers.SpotifyArtistJsonModelMapper;
import dario.gmori.webpageapi.spotify.artists.mappers.SpotifyArtistResponseDtoMapper;
import dario.gmori.webpageapi.spotify.dto.SpotifyUserResponseDto;
import dario.gmori.webpageapi.spotify.mappers.SpotifyUserJsonModelMapper;
import dario.gmori.webpageapi.spotify.mappers.SpotifyUserResponseDtoMapper;
import dario.gmori.webpageapi.spotify.songs.Song;
import dario.gmori.webpageapi.spotify.songs.SongRepository;
import dario.gmori.webpageapi.spotify.songs.SongResponseDto;
import dario.gmori.webpageapi.spotify.songs.mappers.SpotifySongResponseDtoMapper;
import dario.gmori.webpageapi.spotify.songs.mappers.SpotifySongsJsonModelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class SpotifyUserServiceTest {
    private SpotifyUserService spotifyUserService;

    private SpotifyUserResponseDto defaultSpotifyUserResponseDto;
    private SpotifyUser defaultSpotifyUser;

    private ObjectNode defaultUserInfoJson;

    private ObjectNode defaultSongsJson;

    private ObjectNode defaultArtistJson;

    @Mock
    private SpotifyUserRepository spotifyUserRepository;

    @Mock
    private SpotifyRequestUtils spotifyRequestUtils;

    @Mock
    private SongRepository songRepository;
    @Mock
    private ArtistRepository artistRepository;
    @BeforeEach
    void setup(){
        spotifyUserService = new SpotifyUserService(spotifyUserRepository,songRepository,artistRepository, new SpotifyUserResponseDtoMapper(new SpotifySongResponseDtoMapper(new SpotifyArtistResponseDtoMapper()),new SpotifyArtistResponseDtoMapper()), new SpotifyUserJsonModelMapper(),new SpotifySongsJsonModelMapper(new SpotifyArtistJsonModelMapper()),new SpotifyArtistJsonModelMapper(),spotifyRequestUtils);

        defaultSpotifyUserResponseDto = new SpotifyUserResponseDto(
                "test",
                "https://test.com",
                "https://profile_url.com",
                List.of(
                        SongResponseDto.builder()
                                .coverUrl("https://test.com")
                                .url("https://profile_url.com")
                                .name("test")
                                .durationMs(1000)
                                .artists(List.of(
                                        ArtistResponseDto.builder()
                                                .name("test")
                                                .avatarUrl("https://test.com")
                                                .profileUrl("https://profile_url.com")
                                                .followers(0)
                                                .genres("")
                                                .build()
                                ))
                                .build()
                ),
                List.of(
                        ArtistResponseDto.builder()
                                .name("test")
                                .avatarUrl("https://test.com")
                                .profileUrl("https://profile_url.com")
                                .followers(0)
                                .genres("")
                                .build())
        );
        defaultSpotifyUser = new SpotifyUser(
                1L,
                "test",
                "https://test.com",
                "https://profile_url.com",
                LocalDateTime.now(),
                List.of(
                        Song.builder()
                                .coverImageUrl("https://test.com")
                                .url("https://profile_url.com")
                                .name("test")
                                .durationMs(1000)
                                .artists(List.of(
                                        Artist.builder()
                                                .name("test")
                                                .avatarUrl("https://test.com")
                                                .profileUrl("https://profile_url.com")
                                                .followers(0)
                                                .genres("")
                                                .build()
                                ))
                                .build()
                ),
                List.of(
                        Artist.builder()
                                .name("test")
                                .avatarUrl("https://test.com")
                                .profileUrl("https://profile_url.com")
                                .followers(0)
                                .genres("")
                                .build())
                );

        ObjectMapper mapper = new ObjectMapper();

        defaultUserInfoJson = mapper.createObjectNode();
        defaultUserInfoJson.put("display_name", "test");

        // Create external_urls object
        ObjectNode externalUrls = mapper.createObjectNode();
        externalUrls.put("spotify", "https://profile_url.com");
        defaultUserInfoJson.set("external_urls", externalUrls);

        // Create images array
        ArrayNode images = mapper.createArrayNode();
        ObjectNode image = mapper.createObjectNode();
        image.put("url", "https://test.com");
        images.add(image);
        defaultUserInfoJson.set("images", images);

        // Create default songs json
        defaultSongsJson = mapper.createObjectNode();
        ArrayNode items = mapper.createArrayNode();
        ObjectNode item = mapper.createObjectNode();
        item.put("name", "test");
        item.put("id", "test");
        item.put("duration_ms", 1000);
        item.put("preview_url", "https://preview.com");
        item.set("external_urls", externalUrls);
        item.set("album", mapper.createObjectNode().set("images", images));
        ArrayNode artists = mapper.createArrayNode();
        ObjectNode artist = mapper.createObjectNode();
        artist.put("name", "test");
        artist.put("id", "test");
        artist.set("external_urls", externalUrls);
        artist.set("images", images);
        artist.set("followers", mapper.createObjectNode().put("total", 0));
        artists.add(artist);
        item.set("artists", artists);
        items.add(item);
        defaultSongsJson.set("items", items);

        // Create default top artists
        defaultArtistJson = mapper.createObjectNode();
        defaultArtistJson.set("items", artists);

    }

    @Test
    void getSpotifyInfoNormalWorkflow(){
        when(spotifyUserRepository.findUserInfo()).thenReturn(Optional.of(defaultSpotifyUser));
        SpotifyUserResponseDto response = spotifyUserService.getSpotifyInfo();
        assertEquals(defaultSpotifyUserResponseDto, response);
    }

    @Test
    void getSpotifyInfoNeedsToBeCreated(){
        when(spotifyUserRepository.findUserInfo()).thenReturn(Optional.empty());
        when(spotifyRequestUtils.getSpotifyInformation()).thenReturn(defaultUserInfoJson);
        when(songRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(spotifyUserRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(artistRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(spotifyRequestUtils.getTopArtists()).thenReturn(defaultArtistJson);
        when(spotifyRequestUtils.getTopSongs()).thenReturn(defaultSongsJson);
        SpotifyUserResponseDto response = spotifyUserService.getSpotifyInfo();
        assertEquals(defaultSpotifyUserResponseDto, response);
    }

    @Test
    void getSpotifyInfoNeedsToBeUpdated(){
        defaultSpotifyUser.setLastModifiedDate(LocalDateTime.now().minusMinutes(1));
        assertTrue(defaultSpotifyUser.timeToRefreshPassed(10));
        when(spotifyUserRepository.findUserInfo()).thenReturn(Optional.of(defaultSpotifyUser));
        when(spotifyRequestUtils.getSpotifyInformation()).thenReturn(defaultUserInfoJson);
        when(songRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(spotifyUserRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(artistRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(spotifyRequestUtils.getTopArtists()).thenReturn(defaultArtistJson);
        when(spotifyRequestUtils.getTopSongs()).thenReturn(defaultSongsJson);

        assertTrue(defaultSpotifyUser.timeToRefreshPassed(10));
        SpotifyUserResponseDto response = spotifyUserService.getSpotifyInfo();
        assertFalse(defaultSpotifyUser.timeToRefreshPassed(10));
        assertEquals(defaultSpotifyUserResponseDto, response);
    }
}
