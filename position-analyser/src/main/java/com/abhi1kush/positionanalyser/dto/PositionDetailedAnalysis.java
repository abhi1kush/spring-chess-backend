package com.abhi1kush.positionanalyser.dto;

import java.util.List;
import java.util.Map;

public record PositionDetailedAnalysis(
		String fen,
		String sideToMove,
		String gameStatus,
		boolean inCheck,
		int legalMoveCount,
		int materialWhite,
		int materialBlack,
		int materialDifference,
		List<String> legalMovesUci,
		int halfMoveClock,
		int fullMoveNumber,
		String castlingRights,
		String enPassantSquare,
		Map<String, Integer> pieceCountsWhite,
		Map<String, Integer> pieceCountsBlack) {
}
