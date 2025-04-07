package wad.sentinel.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wad.sentinel.api.dto.MonitorizacionDto;
import wad.sentinel.api.service.MonitorizacionService;

@RestController
@RequestMapping("/wadsentinel/api/monitorizaciones")
public class MonitorizacionController {

    @Autowired
    private MonitorizacionService monitorizacionService;

    /**
     * INSERT
     * 
     * @param monitorizacionDto
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<?> create(
            @RequestBody MonitorizacionDto monitorizacionDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(monitorizacionService.create(monitorizacionDto));
    }
}
