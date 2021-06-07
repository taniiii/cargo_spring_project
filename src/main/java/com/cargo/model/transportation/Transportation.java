package com.cargo.model.transportation;

import com.cargo.model.user.User;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "transportations")
public class Transportation{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Length(max=2048, message = "Message is too long (size more 2kB)")
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    @Enumerated(EnumType.STRING)
    private TransportationStatus status;

    @Column(name = "created_at", updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CreatedDate
    private LocalDate creationDate;

    @Column(name = "delivery_at", updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;

    public Transportation() {
        this.creationDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public TransportationStatus getTransportationStatus() {
        return status;
    }

    public void setTransportationStatus(TransportationStatus status) {
        this.status = status;
    }


    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Override
    public String toString() {
        return "Transportation{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", customer=" + customer +
                ", tariff=" + tariff +
                ", status=" + status +
                ", creationDate=" + creationDate +
                ", deliveryDate=" + deliveryDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transportation that = (Transportation) o;
        return Objects.equals(id, that.id) && Objects.equals(comment, that.comment) && Objects.equals(customer, that.customer) && Objects.equals(tariff, that.tariff) && status == that.status && Objects.equals(creationDate, that.creationDate) && Objects.equals(deliveryDate, that.deliveryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, comment, customer, tariff, status, creationDate, deliveryDate);
    }
}
