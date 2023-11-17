package onediary.onediary.controller;

import lombok.RequiredArgsConstructor;
import onediary.onediary.service.impl.RecordServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RecordController {
    private final RecordServiceImpl recordService;

    //저장

    //수정


}
