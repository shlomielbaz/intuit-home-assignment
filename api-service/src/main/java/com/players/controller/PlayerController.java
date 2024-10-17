package com.players.controller;

import com.players.domain.dto.PageDTO;
import com.players.domain.dto.PlayerDTO;
import com.players.domain.entity.Player;
import com.players.service.PlayerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/players")
@CrossOrigin(origins = {"http://localhost:3000", "http://0.0.0.0:3000", "http://127.0.0.1:5500/"})
public class PlayerController {

    @Autowired
    protected PlayerService service;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/all")
    public ResponseEntity findAll() {
        try {
            List<PlayerDTO> list = new ArrayList<>();
            service.findAll().forEach(item -> {
                list.add(convertToDto(item));
            });
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity(String.format("ERROR: %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity findAll(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "nameFirst") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        try {
            Page<Player> result = service.findAll(pageNo, pageSize, sortBy, sortDirection);

            var dtos = result.getContent().stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList());

            var page = modelMapper.map(result, PageDTO.class);

            page.setContent(dtos);

            return new ResponseEntity<>(page, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity(String.format("ERROR: %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private PlayerDTO convertToDto(Player p) {
        PlayerDTO postDto = modelMapper.map(p, PlayerDTO.class);

        return postDto;
    }

    @GetMapping("/{id}")
    public ResponseEntity findOne(@PathVariable("id") String id) {
        try {
            var player = service.findById(id);
            return player
                    .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity(String.format("A player with id: %s not found", id), HttpStatus.NOT_FOUND));

        } catch (Exception e) {
            return new ResponseEntity(String.format("ERROR: %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        Player createdPlayer = service.createPlayer(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlayer);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Player> updatePlayer(@PathVariable String id, @RequestBody Player player) {
//        Player updatedPlayer = service.updatePlayer(id, player);
//        return ResponseEntity.ok(updatedPlayer);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String id) {
        service.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }
}
