package wad.sentinel.api.dto;

import wad.sentinel.api.entity.MonitorizacionProcesador;

public class MonitorizacionProcesadorDto {

    public Long id;
    public String usado;
    public String usuario;
    public String sistema;
    public String inactivo;

    // Constructors -----------------------------

    public MonitorizacionProcesadorDto() {
    }

    public MonitorizacionProcesadorDto(MonitorizacionProcesador entity) {
        updateFrom(entity);
        this.id = entity.getId();
        this.usado = entity.getUsado();
        this.usuario = entity.getUsuario();
        this.sistema = entity.getSistema();
        this.inactivo = entity.getInactivo();
    }

    // Methods -----------------------------

    public MonitorizacionProcesador mapEntity() {
        return new MonitorizacionProcesador(this);
    }

    private void updateFrom(MonitorizacionProcesador entity) {
        this.usado = entity.getUsado();
        this.usuario = entity.getUsuario();
        this.sistema = entity.getSistema();
        this.inactivo = entity.getInactivo();
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

}
