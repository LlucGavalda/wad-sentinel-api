package wad.sentinel.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import wad.sentinel.api.dto.MonitorizacionMemoriaDto;

@Entity
@Table(name = "ws_monitorizaciones_memorias")

public class MonitorizacionMemoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_monitorizacion_memoria")
    private Long id;

    @Column(name = "total", length = 45, nullable = false)
    private String total;

    @Column(name = "usado", length = 45, nullable = false)
    private String usado;

    @Column(name = "libre", length = 45, nullable = false)
    private String libre;

    @Column(name = "porcentaje", nullable = false)
    private Double porcentaje;

    // Constructors -----------------------------

    public MonitorizacionMemoria() {

    }

    public MonitorizacionMemoria(MonitorizacionMemoriaDto dto) {
        updateFrom(dto);
        this.id = dto.getId();
        this.total = dto.getTotal();
        this.usado = dto.getUsado();
        this.libre = dto.getLibre();
        this.porcentaje = dto.getPorcentaje();
    }

    // Methods -----------------------------

    public MonitorizacionMemoriaDto mapDto() {
        return new MonitorizacionMemoriaDto(this);
    }

    public void updateFrom(MonitorizacionMemoriaDto dto) {
        this.total = dto.getTotal();
        this.usado = dto.getUsado();
        this.libre = dto.getLibre();
        this.porcentaje = dto.getPorcentaje();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUsado() {
        return usado;
    }

    public void setUsado(String usado) {
        this.usado = usado;
    }

    public String getLibre() {
        return libre;
    }

    public void setLibre(String libre) {
        this.libre = libre;
    }

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String toString() {
        String str = "Memoria {\n" +
                "   id [" + this.id + "]\n" +
                "   total [" + this.total + "]\n" +
                "   usado [" + this.usado + "]\n" +
                "   libre [" + this.libre + "]\n" +
                "   porcentaje [" + this.porcentaje + "]\n" +
                "}";
        return str;
    }
}
