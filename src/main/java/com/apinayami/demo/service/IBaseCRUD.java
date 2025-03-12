package com.apinayami.demo.service;

import java.util.List;

public interface IBaseCRUD<T> {
    String create(T a);

    String update(T a);

    String delete(T a);
}
