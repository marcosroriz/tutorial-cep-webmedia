package br.ufg.fct.cep;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.JsonNode;

public class BusLocationUpdateEvent implements Serializable {
    
    /**
     * For parsing dates
     */
    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    
    /**
     * Attributes
     */
    public LocalDateTime data;
    public String id;
    public int linha;
    public double latitude;
    public double longitude;
    public double velocidade;

    /**
     * Empty constructor for reflection
     */
    public BusLocationUpdateEvent() {}
    
    /**
     * Creating from JSON object
     */
    public BusLocationUpdateEvent(JsonNode json) {
        this.data = LocalDateTime.parse(json.get(0).asText(), format);
        this.id = json.get(1).asText();
        this.linha = json.get(2).asInt();
        this.latitude = json.get(3).asDouble();
        this.longitude = json.get(4).asDouble();
        this.velocidade = json.get(5).asDouble();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(data).append("\n")
          .append("Linha: " + linha).append("\n")
          .append("Posição: " + latitude + ", " + longitude).append("\n")
          .append("Velocidade: " + velocidade);
        return sb.toString();
    }
    
    // Gets and Sets

    public LocalDateTime getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    public int getLinha() {
        return linha;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getVelocidade() {
        return velocidade;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setVelocidade(double velocidade) {
        this.velocidade = velocidade;
    }

}