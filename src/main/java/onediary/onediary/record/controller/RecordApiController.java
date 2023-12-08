package onediary.onediary.record.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import onediary.onediary.member.repository.MemberRepository;
import onediary.onediary.record.dto.record.RecordViewDto;
import onediary.onediary.record.dto.record.RecordWriteDto;
import onediary.onediary.oauth.utils.JwtHeaderUtil;
import onediary.onediary.member.service.IMemberService;
import onediary.onediary.record.service.IRecordService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@Tag(name="Records", description = "일기 관련 API")
public class RecordApiController {
    private final IRecordService recordService;
    private final IMemberService memberService;
    private final MemberRepository memberRepository;

    @Operation(summary = "일기 생성", description = "새로운 일기를 생성합니다.")
    @RequestMapping (value = "/api/records/create", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<Void> createRecord(
            HttpServletRequest request, @RequestBody RecordWriteDto recordWriteDto){
        // 현재 사용자의 ID 가져오기
        String token = JwtHeaderUtil.getAccessToken(request);
        recordService.createRecord(token, recordWriteDto);
        return ResponseEntity.created(null).build();
    }


    @Operation(summary = "일기 내용 수정", description = "기존 일기의 내용을 수정합니다.")
    @RequestMapping(value = "/api/records/edit/{recordId}/description",produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateRecordDescription(
            @PathVariable Long recordId,
            @RequestParam String updatedDescription){

        recordService.updateRecordDescription(recordId, updatedDescription);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "일기 감정 수정", description = "기존 일기의 감정을 수정합니다.")
    @RequestMapping(value = "/api/records/edit/{recordId}/emotion",produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateRecordEmotion(
            @PathVariable Long recordId,
            @RequestParam String updatedEmotion){
       recordService.updateRecordDescription(recordId, updatedEmotion);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "일기 삭제", description = "일기를 삭제합니다.")
    @RequestMapping(value = "/api/records/delete/{recordId}", produces = "application/json", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteRecord(
            HttpServletRequest request,
            @PathVariable Long recordId){
            // 현재 사용자의 ID 가져오기
            String token = JwtHeaderUtil.getAccessToken(request);
        recordService.deleteRecord(token, recordId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "특정 날짜의 일기 조회", description = "특정 날짜의 일기를 조회합니다.")
    @RequestMapping(value = "/api/records/view/{date}", produces = "application/json",method = RequestMethod.GET)
    public ResponseEntity<RecordViewDto>getRecordByDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
    {
        RecordViewDto recordDto = recordService.findRecordByCreatedDate(date);
        return recordDto != null ?
                ResponseEntity.ok(recordDto) :
                ResponseEntity.notFound().build();
    }



}
