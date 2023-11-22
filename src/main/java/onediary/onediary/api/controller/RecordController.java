//package onediary.onediary.api.controller;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import onediary.onediary.api.dto.record.RecordViewDto;
//import onediary.onediary.api.dto.record.RecordWriteDto;
//import onediary.onediary.api.service.impl.RecordServiceImpl;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@RequestMapping("/diary")
//@RestController
//public class RecordController {
//    private final RecordServiceImpl recordService;
//
//    @ApiOperation("일기 생성")
//    @PostMapping
//    public ResponseEntity<Long> createRecord(
//            @Valid RecordWriteDto recordWriteDto,
//            @AuthenticationPrincipal UserDetails user){
//        Long recordId = recordService.createRecord(recordWriteDto);
//        return new ResponseEntity<>(recordId, HttpStatus.CREATED);
//    }
//    @ApiOperation("회원의 모든 일기 조회")
//    @GetMapping("/{memberId}")
//    public ResponseEntity<List<RecordViewDto>> getRecords(
//            @PathVariable Long memberId,
//            @AuthenticationPrincipal UserDetails user){
//        List<RecordViewDto> recordViewDtos = recordService.getRecords(memberId);
//        return new ResponseEntity<>(recordViewDtos, HttpStatus.OK);
//    }
//
//    @ApiOperation("일기 수정")
//    @PutMapping("/{recordId}")
//    public ResponseEntity<Long> updateRecordDescription(
//            @PathVariable Long recordId,
//            @RequestParam String updatedDescription,
//        @AuthenticationPrincipal UserDetails user){
//        Long updatedRecordId = recordService.updateRecordDescription(recordId, updatedDescription);
//        return new ResponseEntity<>(updatedRecordId, HttpStatus.OK);
//    }
//
//    @ApiOperation("일기 삭제")
//    @DeleteMapping("/{recordId}")
//    public ResponseEntity<Void> deleteRecord(
//            @PathVariable Long recordId,
//            @AuthenticationPrincipal UserDetails user){
//        recordService.deleteRecord(recordId);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    @ApiOperation("특정 날짜의 일기 조회")
//    @GetMapping("/record/{date}")
//    public ResponseEntity<Optional<Long>>getRecordByDate(
//            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDateTime date,
//            @AuthenticationPrincipal UserDetails user)
//    {
//        Optional<Long> recordIdOptional = recordService.findRecordByCreatedDate(date);
//        return new ResponseEntity<>(recordIdOptional, HttpStatus.OK);
//    }
//
//
//}
