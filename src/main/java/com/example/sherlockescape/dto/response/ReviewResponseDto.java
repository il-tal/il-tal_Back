package com.example.sherlockescape.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
	private Long id;
	private String nickname;
	private LocalDate playDate;
	private double score;
	private boolean success;
	private int difficulty;
	private int hint;
	private String comment;

}
