package entities.interfaces;

// Интерфейс, который должны реализовывать все классы, содержащие поле ID,
// применяется в методе поиска объекта по ID в util.ArraySearch.
public interface HasId {
    public int getId();
}
