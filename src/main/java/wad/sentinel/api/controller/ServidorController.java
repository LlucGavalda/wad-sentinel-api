package wad.sentinel.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wad.sentinel.api.dto.ServidorDto;
import wad.sentinel.api.service.ServidorService;
import wad.sentinel.api.utils.dto.SearchCriteriaDto;

@RestController
@RequestMapping("/wadsentinel/api/servidors")
public class ServidorController {

    @Autowired
    private ServidorService servidorService;

    /**
     * READ ALL
     * 
     * @return
     */
    @GetMapping("/list")
    public List<ServidorDto> list(@RequestBody(required = false) SearchCriteriaDto[] dto) {
        return servidorService.list(dto);
    }

    /**
     * SEARCH
     * 
     * @return
     */
    @PostMapping("/search")
    public List<ServidorDto> search(@RequestBody(required = false) SearchCriteriaDto[] dto) {
        return servidorService.list(dto);
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
        ServidorDto servidorDto = servidorService.get(id);
        return ResponseEntity.ok(servidorDto);
    }

    /**
     * INSERT
     * 
     * @param servidorDto
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<?> create(
            @RequestBody ServidorDto servidorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servidorService.create(servidorDto));
    }

    /**
     * UPDATE
     * 
     * @param servidorDto
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> modify(
            @RequestBody ServidorDto servidorDto,
            @PathVariable Long id) {
        servidorDto.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(servidorService.modify(servidorDto));
    }
}
