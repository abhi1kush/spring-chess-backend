package com.abhi1kush.positionanalyser.dto;

public record PositionSummaryAnalysis(
		String fen,
		String sideToMove,
		String gameStatus,
		boolean inCheck,
		int legalMoveCount,
		int materialWhite,
		int materialBlack,
		int materialDifference) {
}
