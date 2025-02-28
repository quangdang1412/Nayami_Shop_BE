package com.apinayami.demo.service;

public interface IBaseCRUD<T> {
    String create(T a);

    String update(T a);

    String delete(T a);
}
