package com.example.sherlockescape.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {


	private LocalDate playDate;

	private double score;
	///// Enum 고려
	private boolean success;

	private int difficulty;

	private int hint;
	@NotBlank
	private String comment;

}
