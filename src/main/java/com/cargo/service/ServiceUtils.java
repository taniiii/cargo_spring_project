package com.cargo.service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ServiceUtils {
    static Set<String> getParameters(Enum[] e){
        return Arrays.stream(e).map(Enum :: name).collect(Collectors.toSet());
    }
}
