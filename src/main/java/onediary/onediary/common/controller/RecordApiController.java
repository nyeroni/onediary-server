package onediary.onediary.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onediary.onediary.domain.member.repository.MemberRepository;
import onediary.onediary.dto.record.RecordViewDto;
import onediary.onediary.dto.record.RecordWriteDto;
import onediary.onediary.service.IMemberService;
import onediary.onediary.service.IRecordService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Tag(name="Records", description = "일기 관련 API")
public class RecordApiController {
    private final IRecordService recordService;
    private final IMemberService memberService;
    private final MemberRepository memberRepository;

    @Operation(summary = "일기 생성", description = "새로운 일기를 생성합니다.")
    @RequestMapping (value = "/api/records/create", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<Long> createRecord(
            @Valid RecordWriteDto recordWriteDto){
        // 현재 사용자의 ID 가져오기
        RecordViewDto recordDto = recordService.createRecord(recordWriteDto);
        Long recordId = recordDto.getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(recordId);
    }


    @Operation(summary = "일기 내용 수정", description = "기존 일기의 내용을 수정합니다.")
    @RequestMapping(value = "/api/records/edit/{recordId}/description",produces = "application/json", method = RequestMethod.PUT)

    public ResponseEntity<Long> updateRecordDescription(
            @PathVariable Long recordId,
            @RequestParam String updatedDescription){

        RecordViewDto recordDto = recordService.updateRecordDescription(recordId, updatedDescription);
        Long updatedRecordId = recordDto.getId();
        return ResponseEntity.ok(updatedRecordId);
    }
    @Operation(summary = "일기 감정 수정", description = "기존 일기의 감정을 수정합니다.")
    @RequestMapping(value = "/api/records/edit/{recordId}/emotion",produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<Long> updateRecordEmotion(
            @PathVariable Long recordId,
            @RequestParam String updatedEmotion){
        RecordViewDto recordDto = recordService.updateRecordDescription(recordId, updatedEmotion);
        Long updatedRecordId = recordDto.getId();
        return ResponseEntity.ok(updatedRecordId);
    }

    @Operation(summary = "일기 삭제", description = "일기를 삭제합니다.")
    @RequestMapping(value = "/api/records/delete/{recordId}", produces = "application/json", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteRecord(
            @PathVariable Long recordId){
        recordService.deleteRecord(recordId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "특정 날짜의 일기 조회", description = "특정 날짜의 일기를 조회합니다.")
    @RequestMapping(value = "/api/records/view/{date}", produces = "application/json",method = RequestMethod.GET)
    public ResponseEntity<Optional<RecordViewDto>>getRecordByDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
    {
        Optional<RecordViewDto> recordDto = recordService.findRecordByCreatedDate(date);
        return recordDto != null ?
                ResponseEntity.ok(recordDto) :
                ResponseEntity.notFound().build();
    }

}
