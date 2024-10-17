package com.players;

import com.players.domain.entity.Player;
import com.players.service.PlayerService;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService service;

    @Test
    public void testGetPlayerById() throws Exception {
        mockMvc.perform(get("/api/players/aardsda01"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.playerID").value("aardsda01"));
    }

    @Test
    public void testCreatePlayer() throws Exception {
        String playerJson = """
        { 
            "playerID": "newplayer01", "nameFirst": "John", "nameLast": "Doe" 
        }""";

        mockMvc.perform(post("/api/players")
                        .content(playerJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.playerID").value("newplayer01"));
    }

//    @Test
//    public void testUpdatePlayer() throws Exception {
//        String updateJson = "{ \"nameFirst\": \"Updated\", \"nameLast\": \"Player\" }";
//
//        mockMvc.perform(put("/api/players/aardsda01")
//                        .content(updateJson)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.nameFirst").value("Updated"));
//    }

    @Test
    public void testDeletePlayer() throws Exception {
        mockMvc.perform(delete("/api/players/aardsda01"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testCreatePlayer_MissingRequiredField() throws Exception {
        String invalidPlayerJson = "{ \"nameFirst\": \"John\" }"; // Missing nameLast and playerID

        mockMvc.perform(post("/api/players")
                        .content(invalidPlayerJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetPlayerById_NotFound() throws Exception {
        mockMvc.perform(get("/api/players/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetPlayerById_InvalidId() throws Exception {
        mockMvc.perform(get("/api/players/!@#invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreatePlayer_DuplicateID() throws Exception {
        String duplicatePlayerJson = "{ \"playerID\": \"aardsda01\", \"nameFirst\": \"David\", \"nameLast\": \"Aardsma\" }";

        mockMvc.perform(post("/api/players")
                        .content(duplicatePlayerJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void testCreatePlayer_InvalidBirthYear() throws Exception {
        String invalidPlayerJson = "{ \"playerID\": \"newplayer01\", \"nameFirst\": \"John\", \"nameLast\": \"Doe\", \"birthYear\": 3000 }";

        mockMvc.perform(post("/api/players")
                        .content(invalidPlayerJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testCreatePlayer_ExcessiveData() throws Exception {
        String largeName = "a".repeat(300);  // Large string
        String invalidPlayerJson = String.format("{ \"playerID\": \"newplayer01\", \"nameFirst\": \"%s\", \"nameLast\": \"Doe\" }", largeName);

        mockMvc.perform(post("/api/players")
                        .content(invalidPlayerJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @RepeatedTest(100)
    public void testPerformance_GetPlayerById() throws Exception {
        mockMvc.perform(get("/api/players/aardsda01"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPlayerById_MockService() throws Exception {
        Player player = new Player();
        player.setPlayerID("aardsda01");
        player.setNameFirst("David");
        player.setNameLast("Aardsma");

        when(service.findById("aardsda01")).thenReturn(Optional.of(player));

        mockMvc.perform(get("/api/players/aardsda01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerID").value("aardsda01"));
    }
}