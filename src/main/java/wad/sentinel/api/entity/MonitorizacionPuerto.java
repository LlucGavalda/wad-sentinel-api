package wad.sentinel.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import wad.sentinel.api.dto.MonitorizacionPuertoDto;

@Entity
@Table(name = "ws_monitorizaciones_puertos")

public class MonitorizacionPuerto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_monitorizacion_puerto")
    private Long id;

    @Column(name = "puerto", length = 10, nullable = false)
    private String puerto;

    @Column(name = "activo", length = 1, nullable = false)
    private String activo = "S";

    // Constructors -----------------------------

    public MonitorizacionPuerto() {

    }

    public MonitorizacionPuerto(MonitorizacionPuertoDto dto) {
        updateFrom(dto);
        this.id = dto.getId();
        this.puerto = dto.getPuerto();
        this.activo = dto.getActivo();
    }

    // Methods -----------------------------

    public MonitorizacionPuertoDto mapDto() {
        return new MonitorizacionPuertoDto(this);
    }

    public void updateFrom(MonitorizacionPuertoDto dto) {
        this.puerto = dto.getPuerto();
        this.activo = dto.getActivo();
    }

    // Getters and Setters -----------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public String toString() {
        String str = "Servidor {\n" +
                "   id [" + this.id + "]\n" +
                "   puerto [" + this.puerto + "]\n" +
                "   activo [" + this.activo + "]\n" +
                "}";
        return str;
    }
}
