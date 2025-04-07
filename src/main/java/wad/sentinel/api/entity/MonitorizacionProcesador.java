package wad.sentinel.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import wad.sentinel.api.dto.MonitorizacionProcesadorDto;

@Entity
@Table(name = "ws_monitorizaciones_procesadores")

public class MonitorizacionProcesador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_monitorizacion_procesador")
    private Long id;

    @Column(name = "usado", length = 45, nullable = false)
    private String usado;

    @Column(name = "usuario", length = 45, nullable = false)
    private String usuario;

    @Column(name = "sistema", length = 45, nullable = false)
    private String sistema;

    @Column(name = "inactivo", length = 45, nullable = false)
    private String inactivo;

    // Constructors -----------------------------

    public MonitorizacionProcesador() {

    }

    public MonitorizacionProcesador(MonitorizacionProcesadorDto dto) {
        updateFrom(dto);
        this.id = dto.getId();
        this.usado = dto.getUsado();
        this.usuario = dto.getUsuario();
        this.sistema = dto.getSistema();
        this.inactivo = dto.getInactivo();
    }

    // Methods -----------------------------

    public MonitorizacionProcesadorDto mapDto() {
        return new MonitorizacionProcesadorDto(this);
    }

    public void updateFrom(MonitorizacionProcesadorDto dto) {
        this.usado = dto.getUsado();
        this.usuario = dto.getUsuario();
        this.sistema = dto.getSistema();
        this.inactivo = dto.getInactivo();
    }

    // Getters and Setters -----------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsado() {
        return usado;
    }

    public void setUsado(String usado) {
        this.usado = usado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSistema() {
        return sistema;
    }

    public void setSistema(String sistema) {
        this.sistema = sistema;
    }

    public String getInactivo() {
        return inactivo;
    }

    public void setInactivo(String inactivo) {
        this.inactivo = inactivo;
    }

    public String toString() {
        String str = "Servidor {\n" +
                "   id [" + this.id + "]\n" +
                "   usado [" + this.usado + "]\n" +
                "   usuario [" + this.usuario + "]\n" +
                "   sistema [" + this.sistema + "]\n" +
                "   inactivo [" + this.inactivo + "]\n" +
                "}";
        return str;
    }
}
