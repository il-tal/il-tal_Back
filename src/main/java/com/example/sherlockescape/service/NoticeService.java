package com.example.sherlockescape.service;

import com.example.sherlockescape.domain.Notice;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.NoticeRequestDto;
import com.example.sherlockescape.dto.response.NoticeResponseDto;
import com.example.sherlockescape.exception.ErrorCode;
import com.example.sherlockescape.exception.GlobalException;
import com.example.sherlockescape.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

	private final NoticeRepository noticeRepository;

	// 공지사항 작성
	@Transactional
	public ResponseDto<String> createNotice(NoticeRequestDto noticeRequestDto) {

		Notice notice = Notice.builder()
				.title(noticeRequestDto.getTitle())
				.notice(noticeRequestDto.getNotice())
				.noticeImgUrl(noticeRequestDto.getNoticeImgUrl())
				.build();
		noticeRepository.save(notice);
		return ResponseDto.success("공지사항 등록 완료!");
	}


	// 공지사항 상세조회
	@Transactional(readOnly = true)
	public ResponseDto<NoticeResponseDto> getNotice(Long noticeId) {

		Notice notice = noticeRepository.findById(noticeId).orElseThrow(
				() -> new GlobalException(ErrorCode.NOTICE_NOT_FOUND)
		);

		return ResponseEntity.ok().body(ResponseDto.success(
				NoticeResponseDto.builder()
						.id(notice.getId())
						.title(notice.getTitle())
						.notice(notice.getNotice())
						.noticeImgUrl(notice.getNoticeImgUrl())
						.createdAt(notice.getCreatedAt())
						.modifiedAt(notice.getModifiedAt())
						.build()
		)).getBody();
	}


	// 공지사항 전체조회
	@Transactional(readOnly = true)
	public Page<NoticeResponseDto> getAllNotice(Pageable pageable) {

		Page<Notice> noticeList = noticeRepository.getNoticeList(pageable);

		List<NoticeResponseDto> noticeAllList = new ArrayList<>();

		for(Notice notice: noticeList) {
			noticeAllList.add(
					NoticeResponseDto.builder()
							.id(notice.getId())
							.title(notice.getTitle())
							.notice(notice.getNotice())
							.noticeImgUrl(notice.getNoticeImgUrl())
							.createdAt(notice.getCreatedAt())
							.modifiedAt(notice.getModifiedAt())
							.build()
			);
		}
		return new PageImpl<>(noticeAllList,pageable,noticeList.getTotalElements());
	}


	// 공지사항 수정
	@Transactional
	public ResponseDto<String> updateNotice(Long noticeId, NoticeRequestDto noticeRequestDto) {

		Notice notice = noticeRepository.findById(noticeId).orElseThrow(
				() -> new GlobalException(ErrorCode.NOTICE_NOT_FOUND)
		);

		notice.update(noticeRequestDto);
		return ResponseDto.success("공지사항 수정 완료!");
	}


	// 공지사항 삭제
	@Transactional
	public ResponseDto<String> deleteNotice(Long noticeId) {
		noticeRepository.deleteById(noticeId);
		return ResponseDto.success("공지사항 삭제 완료!");
	}
}
