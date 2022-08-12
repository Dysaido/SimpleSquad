package xyz.dysaido.squad.util;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FilterHelper {

    public static <K, V> Optional<K> findKeyByValue(Map<K, V> map, V value) {
        return map.entrySet().stream().parallel().filter(entry -> entry.getValue().equals(value))
                .map(Map.Entry::getKey).findFirst();
    }

    public static <K, V> Stream<K> findKeysByValue(Map<K, V> map, V value) {
        return map.entrySet().stream().parallel().filter(kvEntry -> kvEntry.getValue().equals(value))
                .map(Map.Entry::getKey);
    }

    public static <K, V> Optional<V> findValueByPredicate(Map<K, V> map, Predicate<V> predicate) {
        return map.values().stream().parallel().filter(predicate).findFirst();
    }

    public static <K, V> Stream<V> findValuesByPredicate(Map<K, V> map, Predicate<V> predicate) {
        return map.values().stream().parallel().filter(predicate);
    }
}
