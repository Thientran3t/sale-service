package com.vnet.solution.common.dto;

import java.util.Objects;
public class Tuple {

    private String product;
    private String store;

    public Tuple(String product, String store) {
        this.product = product;
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return Objects.equals(product, tuple.product) && Objects.equals(store, tuple.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, store);
    }

}
