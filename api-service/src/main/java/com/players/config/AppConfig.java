package com.players.config;

import com.opencsv.CSVReader;
import com.players.domain.entity.Player;
import com.players.service.PlayerService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class AppConfig {

    private static final String COMMA_DELIMITER = ",";

    @Bean
    CommandLineRunner runner(PlayerService service){
        return args -> {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream input = loader.getResourceAsStream("csv/player.csv");

            assert input != null;
            try (CSVReader reader = new CSVReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
                CsvToBean<Player> csvToBean = new CsvToBeanBuilder<Player>(reader)
                        .withType(Player.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                List<Player> players = csvToBean.parse();
                service.save(players);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
