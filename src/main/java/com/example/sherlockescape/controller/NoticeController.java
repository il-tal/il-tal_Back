package com.example.sherlockescape.controller;

import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.NoticeRequestDto;
import com.example.sherlockescape.dto.response.NoticeResponseDto;
import com.example.sherlockescape.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class NoticeController {

	private final NoticeService noticeService;

	// 공지사항 작성
	@PostMapping("/notice")
	public ResponseDto<String> createNotice(@RequestPart(required = false, value = "file") MultipartFile multipartFile,
											@RequestPart(value = "notice") NoticeRequestDto noticeRequestDto) throws IOException {
		return noticeService.createNotice(multipartFile, noticeRequestDto);
	}

	// 공지사항 상세조회
	@GetMapping("/notice/{noticeId}")
	public ResponseDto<?> getNotice(@PathVariable Long noticeId){
		return ResponseDto.success(noticeService.getNotice(noticeId));
	}

	// 공지사항 전체조회
	@GetMapping("/notices")
	public ResponseDto<Page<NoticeResponseDto>> getAllNotice(@PageableDefault(size = 4, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseDto.success(noticeService.getAllNotice(pageable));
	}

	// 공지사항 수정
	@PutMapping("/notice/{noticeId}")
	public ResponseDto<String> updateNotice(@PathVariable  Long noticeId,
											@RequestPart(required = false, value = "file") MultipartFile multipartFile,
											@RequestPart(value = "notice") NoticeRequestDto noticeRequestDto) throws IOException {
		return noticeService.updateNotice(noticeId, multipartFile, noticeRequestDto);
	}

	// 공지사항 삭제
	@DeleteMapping("/notice/{noticeId}")
	public ResponseDto<String> deleteNotice(@PathVariable  Long noticeId) {
		return noticeService.deleteNotice(noticeId);
	}
}