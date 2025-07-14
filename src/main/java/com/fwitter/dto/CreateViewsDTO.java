package com.fwitter.dto;

import java.util.List;
import java.util.Objects;

public class CreateViewsDTO{
    private List<Integer> ids;

    public CreateViewsDTO() {
    }

    public CreateViewsDTO(List<Integer> ids) {
        this.ids = ids;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "CreateViewsDTO{ids=" + ids + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(ids);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CreateViewsDTO other = (CreateViewsDTO) obj;
        return Objects.equals(ids, other.ids);
    }

}
