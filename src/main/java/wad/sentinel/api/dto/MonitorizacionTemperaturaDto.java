package wad.sentinel.api.dto;

import wad.sentinel.api.entity.MonitorizacionTemperatura;

public class MonitorizacionTemperaturaDto {

    public Long id;
    public String sensor;
    public Double temperatura;

    // Constructors -----------------------------

    public MonitorizacionTemperaturaDto() {
    }

    public MonitorizacionTemperaturaDto(MonitorizacionTemperatura entity) {
        updateFrom(entity);
        this.id = entity.getId();
        this.sensor = entity.getSensor();
        this.temperatura = entity.getTemperatura();
    }

    // Methods -----------------------------

    public MonitorizacionTemperatura mapEntity() {
        return new MonitorizacionTemperatura(this);
    }

    private void updateFrom(MonitorizacionTemperatura entity) {
        this.sensor = entity.getSensor();
        this.temperatura = entity.getTemperatura();
    }

    // Getters and Setters -----------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

}
