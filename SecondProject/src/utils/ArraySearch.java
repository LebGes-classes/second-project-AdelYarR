package utils;

import entities.interfaces.HasId;

import java.util.ArrayList;

// Вспомогательный класс с методом для поиска объекта в списке по ID.
public class ArraySearch {
    public static <T extends HasId> T findById(int id, ArrayList<T> list) {
        T currEl = null;
        for (T el : list) {
            if (el.getId() == id) {
                currEl = el;
            }
        }
        
        return currEl;
    }
}
