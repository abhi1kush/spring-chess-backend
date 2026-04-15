package com.abhi1kush.positionanalyser.dto;

import java.util.List;

public record LegalMovesResponse(String fen, List<String> legalMovesUci) {
}
