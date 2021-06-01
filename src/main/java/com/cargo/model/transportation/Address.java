package com.cargo.model.transportation;

import javax.persistence.*;
import java.util.Set;


public enum Address {
    SPAIN, GERMANY, NORWAY, PORTUGAL, MOROCCO;

    public String getAddress(){
        return name();
    }

}
