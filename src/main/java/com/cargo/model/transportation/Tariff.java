package com.cargo.model.transportation;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tariffs")
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int price;

    @Enumerated(EnumType.STRING)
    private Address address;
    @Enumerated(EnumType.STRING)
    private Size size;
    @Enumerated(EnumType.STRING)
    private Weight weight;

    private Long deliveryTermDays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    public Long getDeliveryTermDays() {
        return deliveryTermDays;
    }

    public void setDeliveryTermDays(Long deliveryTermDays) {
        this.deliveryTermDays = deliveryTermDays;
    }

    @Override
    public String toString() {
        return "Tariff{" +
                "id=" + id +
                ", price=" + price +
                ", address=" + address +
                ", size=" + size +
                ", weight=" + weight +
                ", deliveryTermDays=" + deliveryTermDays +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tariff tariff = (Tariff) o;
        return price == tariff.price && Objects.equals(id, tariff.id) && address == tariff.address && size == tariff.size && weight == tariff.weight && Objects.equals(deliveryTermDays, tariff.deliveryTermDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, address, size, weight, deliveryTermDays);
    }
}
