package wad.sentinel.api.controller;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wad.sentinel.api.dto.MonitorizacionDto;
import wad.sentinel.api.service.MonitorizacionService;

@RestController
@RequestMapping("/wadsentinel/api/monitorizaciones")
public class MonitorizacionController {

    @Autowired
    private MonitorizacionService monitorizacionService;

    /**
     * READ ALL
     * 
     * @return
     */
    @GetMapping("/list")
    public List<MonitorizacionDto> list(
            @RequestParam(required = false) Boolean disponible,
            @RequestParam(required = false) Boolean incidencia,
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta,
            @RequestParam(required = false) Long idServidor) {
        return monitorizacionService.list(disponible, incidencia, fechaDesde, fechaHasta, idServidor);
    }

    /**
     * READ
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> get(
            @PathVariable Long id) {
        MonitorizacionDto monitorizacionDto = monitorizacionService.get(id);
        return ResponseEntity.ok(monitorizacionDto);
    }

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
