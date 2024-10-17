package com.players.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDTO <T>{
    private  T content;
    private int numberOfElements;
    private int totalElements;
    private int totalPages;
}
