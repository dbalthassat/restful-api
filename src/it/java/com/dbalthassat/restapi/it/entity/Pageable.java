package com.dbalthassat.restapi.it.entity;

import com.dbalthassat.restapi.entity.ApiEntity;
import org.springframework.data.domain.Sort;

import java.util.List;

@SuppressWarnings("unused")
public class Pageable<T extends ApiEntity> {
    private List<T> content;
    private Boolean first;
    private Boolean last;
    private int totalElements;
    private int totalPages;
    private int size;
    private int number;
    private int numberOfElements;
    private List<Sort.Order> sort;

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public List<Sort.Order> getSort() {
        return sort;
    }

    public void setSort(List<Sort.Order> sort) {
        this.sort = sort;
    }
}
