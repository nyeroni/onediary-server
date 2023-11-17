package onediary.onediary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {
    @GetMapping("/success")
    public ResponseEntity getSuccessMessage(){
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/success")
    public ResponseEntity postSuccessMessage(@RequestParam String param){
        log.info("param = {}", param);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
