package wad.sentinel.api.dto;

import wad.sentinel.api.entity.MonitorizacionPuerto;

public class MonitorizacionPuertoDto {

    public Long id;

    public String puerto = "";

    public String activo;

    // Constructors -----------------------------

    public MonitorizacionPuertoDto() {
    }

    public MonitorizacionPuertoDto(MonitorizacionPuerto entity) {
        updateFrom(entity);
        this.id = entity.getId();
        this.puerto = entity.getPuerto();
        this.activo = entity.getActivo();
    }

    // Methods -----------------------------

    public MonitorizacionPuerto mapEntity() {
        return new MonitorizacionPuerto(this);
    }

    private void updateFrom(MonitorizacionPuerto entity) {
        this.puerto = entity.getPuerto();
        this.activo = entity.getActivo();
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

}
