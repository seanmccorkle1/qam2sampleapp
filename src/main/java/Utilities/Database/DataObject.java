package Utilities.Database;

import java.util.Optional;
import javafx.collections.ObservableList;


public interface DataObject<T> {

    Optional<T> fetch(int id);
    ObservableList<T> get_every();
    boolean insert(T t);
    boolean update(T t);
    boolean delete(int id);
}