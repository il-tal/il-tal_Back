package com.example.sherlockescape.dto.response;

import com.example.sherlockescape.domain.Review;
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

	private String nickname;
	private LocalDate playDate;
	private double score;
	private boolean success;
	private int difficulty;
	private int hint;
	private String comment;

}
