package onediary.onediary.common.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class TestController {
    @GetMapping("/success")
    public ResponseEntity<String > getSuccessMessage(){

        log.info("GET /success called");
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/success")
    public ResponseEntity postSuccessMessage(@RequestParam String param){
        log.info("param = {}", param);
        return new ResponseEntity<>(param, HttpStatus.OK);    }
}
