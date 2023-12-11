package com.apontadores.utils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TopScores extends LeaderBoard {

        private String username;
        private Integer score;
        private Integer level;
        private String time_in_string;



    }