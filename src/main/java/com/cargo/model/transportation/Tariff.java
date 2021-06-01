package com.cargo.model.transportation;

import javax.persistence.*;

@Entity
@Table(name = "tariffs")
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int price;
//    @ManyToOne(fetch = FetchType.EAGER)//при выгрузке перевозки выгружается и клиент
//    @JoinColumn(name = "address_id", nullable = false)
    @Enumerated(EnumType.STRING)
    private Address address;
    @Enumerated(EnumType.STRING)
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "size_id", nullable = false)
    private Size size;
    @Enumerated(EnumType.STRING)
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "weight_id", nullable = false)
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
}
