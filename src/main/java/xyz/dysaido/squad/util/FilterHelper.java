package xyz.dysaido.squad.util;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class FilterHelper {

    public static <K, V> Optional<K> getKey(Map<K, V> map, V value) {
        return map.entrySet().stream().filter(entry -> entry.getValue().equals(value))
                .map(Map.Entry::getKey).findFirst();
    }

    public static <K, V> Stream<K> getKeys(Map<K, V> map, V value) {
        return map.entrySet().stream().filter(kvEntry -> kvEntry.getValue().equals(value))
                .map(Map.Entry::getKey);
    }
}
