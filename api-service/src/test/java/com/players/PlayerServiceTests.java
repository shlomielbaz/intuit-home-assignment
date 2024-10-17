package com.players;

import com.players.domain.entity.Player;
import com.players.repository.PlayerRepository;
import com.players.service.PlayerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceTests {

    @Mock
    private PlayerRepository repository;

    @InjectMocks
    private PlayerService service;

    static Answer<Page<Player>> fakeFunction;

    static {
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Player player = new Player();
            players.add(player);
        }
        fakeFunction = invocation -> {
            Pageable request = invocation.getArgument(0);
            Sort sort = request.getSort();
            var order = sort.stream().findFirst().get();
            var sorted = players.stream().sorted(Comparator.comparing(Player::getNameFirst)).toList().subList(0, request.getPageSize());
            if (order.getDirection().name().equals("DESC")) {
                sorted = players.stream().sorted(Comparator.comparing(Player::getNameFirst).reversed()).toList().subList(0, request.getPageSize());
            }
            Page<Player> page = new PageImpl<>(sorted, request, players.size());
            return page;
        };
    }



    @Test
    public void findByIdTest() {
        String input = "aardsda01";

        var p1 = new Player();
        p1.setNameFirst("David");
        p1.setNameGiven("David Allan");
        p1.setNameLast("Aardsma");

        var expectedOutput = Optional.of(p1);

        when(repository.findById(input)).thenReturn(expectedOutput);

        var actualOutput = service.findById(input);

        var p2 = expectedOutput.get();

        assertEquals(expectedOutput, actualOutput);
        assertEquals(p2.getNameFirst(), p1.getNameFirst());
        assertEquals(p2.getNameGiven(), p1.getNameGiven());
        assertEquals(p2.getNameLast(), p1.getNameLast());
    }
}