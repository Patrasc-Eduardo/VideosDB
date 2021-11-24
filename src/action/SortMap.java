package action;

import video.Video;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface SortMap {
  /**
   * Some javadoc. // OK
   *
   * @author Some javadoc. // OK
   * @version Some javadoc. // OK
   * @param Some javadoc. // OK
   * @return Some javadoc. // OK
   * @throws Some javadoc. // OK
   * @exception Some javadoc. // OK
   * @see Some javadoc. // OK
   * @since Some javadoc. // OK
   * @serial Some javadoc. // OK
   * @serialField // OK
   * @serialData // OK
   * @deprecated Some javadoc. // OK
   */
  static List<String> sortMap(
      HashMap<String, Double> map,
      String sortType,
      String apearCriteria,
      ArrayList<Video> apOrder,
      Integer number) {

    int order = sortType.equals("asc") ? 1 : -1;

    List<Map.Entry<String, Double>> list = new LinkedList<>(map.entrySet());

    Collections.sort(
        list,
        new Comparator<Map.Entry<String, Double>>() {
          @Override
          public int compare(
              final Map.Entry<String, Double> o1, final Map.Entry<String, Double> o2) {
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
          }
        });

    HashMap<String, Double> newMap = new LinkedHashMap<String, Double>();

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
