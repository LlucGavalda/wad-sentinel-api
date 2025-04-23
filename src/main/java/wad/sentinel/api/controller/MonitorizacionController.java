package wad.sentinel.api.controller;

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
import wad.sentinel.api.dto.MonitorizacionPage;
import wad.sentinel.api.entity.Monitorizacion;
import wad.sentinel.api.service.MonitorizacionService;
import wad.sentinel.api.utils.dto.SearchCriteriaDto;

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
    @GetMapping("/paged")
    public MonitorizacionPage list(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "1", required = false) Integer pageSize,
            @RequestBody(required = false) SearchCriteriaDto[] dto) {
        return monitorizacionService.list(pageNumber, pageSize, dto);
    }

    /**
     * READ LAST
     * 
     * @return
     */
    @GetMapping("/last")
    public List<Monitorizacion> last() {
        return monitorizacionService.last();
    }

    @PostMapping("/search")
    public MonitorizacionPage search(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "1", required = false) Integer pageSize,
            @RequestBody(required = false) SearchCriteriaDto[] dto) {
        return monitorizacionService.list(pageNumber, pageSize, dto);
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
