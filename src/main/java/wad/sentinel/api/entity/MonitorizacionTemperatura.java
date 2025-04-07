package wad.sentinel.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import wad.sentinel.api.dto.MonitorizacionTemperaturaDto;

@Entity
@Table(name = "ws_monitorizaciones_temperaturas")

public class MonitorizacionTemperatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_monitorizacion_temperatura")
    private Long id;

    @Column(name = "sensor", length = 45, nullable = false)
    private String sensor;

    @Column(name = "temperatura", nullable = false)
    private Double temperatura;

    // Constructors -----------------------------

    public MonitorizacionTemperatura() {

    }

    public MonitorizacionTemperatura(MonitorizacionTemperaturaDto dto) {
        updateFrom(dto);
        this.id = dto.getId();
        this.sensor = dto.getSensor();
        this.temperatura = dto.getTemperatura();
    }

    // Methods -----------------------------

    public MonitorizacionTemperaturaDto mapDto() {
        return new MonitorizacionTemperaturaDto(this);
    }

    public void updateFrom(MonitorizacionTemperaturaDto dto) {
        this.sensor = dto.getSensor();
        this.temperatura = dto.getTemperatura();
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

    public String toString() {
        String str = "Servidor {\n" +
                "   id [" + this.id + "]\n" +
                "   sensor [" + this.sensor + "]\n" +
                "   temperatura [" + this.temperatura + "]\n" +
                "}";
        return str;
    }
}
