package wad.sentinel.api.dto;

import wad.sentinel.api.entity.MonitorizacionDisco;

public class MonitorizacionDiscoDto {

    public Long id;
    public String total;
    public String usado;
    public String libre;
    public Double porcentaje;

    // Constructors -----------------------------

    public MonitorizacionDiscoDto() {
    }

    public MonitorizacionDiscoDto(MonitorizacionDisco entity) {
        updateFrom(entity);
        this.id = entity.getId();
        this.total = entity.getTotal();
        this.usado = entity.getUsado();
        this.libre = entity.getLibre();
        this.porcentaje = entity.getPorcentaje();
    }

    // Methods -----------------------------

    public MonitorizacionDisco mapEntity() {
        return new MonitorizacionDisco(this);
    }

    private void updateFrom(MonitorizacionDisco entity) {
        this.total = entity.getTotal();
        this.usado = entity.getUsado();
        this.libre = entity.getLibre();
        this.porcentaje = entity.getPorcentaje();
    }

    // Getters and Setters -----------------------------

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

}
