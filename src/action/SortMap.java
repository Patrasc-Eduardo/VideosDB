package action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface SortMap {
  /**
   * Some javadoc. // OK
   */
  static List<String> sortMap(
      HashMap<String, Double> map,
      String sortType,
      String apearCriteria,
      ArrayList<String> apOrder,
      Integer number) {

    int order = sortType.equals("asc") ? 1 : -1;

    List<Map.Entry<String, Double>> list = new LinkedList<>(map.entrySet());

    list.sort(
        (o1, o2) -> {
          if (o1.getValue().compareTo(o2.getValue()) == 0) {
            if (apearCriteria != null) {
              if (apOrder.indexOf(o1.getKey()) < apOrder.indexOf(o2.getKey())) {
                return -1;
              } else {
                return 1;
              }
            } else {
              return o1.getKey().compareTo(o2.getKey()) * order;
            }
          }
          return o1.getValue().compareTo(o2.getValue()) * order;
        });

    HashMap<String, Double> newMap = new LinkedHashMap<>();

    for (Map.Entry<String, Double> aux : list) {
      newMap.put(aux.getKey(), aux.getValue());
    }

    List<String> names = new ArrayList<>(newMap.keySet());

    if (number == null || names.size() < number) {
      return names;
    }

    names = names.stream().limit(number).collect(Collectors.toList());

    return names;
  }
}
