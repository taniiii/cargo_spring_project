package com.cargo.model.transportation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


public enum TransportationStatus {
    NEW, WAITING_FOR_PAYMENT, PAID, REJECTED;

    public String getStatus(){
        return name();
    }

}
