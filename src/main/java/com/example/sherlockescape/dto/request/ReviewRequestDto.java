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

	@NotBlank(message = "플레이날짜를 입력해주세요!")
	private LocalDate playDate;

	@NotBlank(message = "평점을 입력해주세요!")
	private double score;

	///// Enum 고려
	@NotBlank(message = "성공여부를 입력해주세요!")
	private boolean success;

	@NotBlank(message = "난이도를 입력해주세요!")
	private int difficulty;

	@NotBlank(message = "힌트사용개수를 입력해주세요!")
	private int hint;

	@NotBlank(message = "후기를 입력해주세요!")
	private String comment;
}
