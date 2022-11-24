package com.example.sherlockescape.dto.request;

import com.example.sherlockescape.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
	private Long id;
	private String nickname;

	public static MemberDto of(Member member) {
		return MemberDto.builder()
				.id(member.getId())
				.nickname(member.getNickname())
				.build();
	}
}