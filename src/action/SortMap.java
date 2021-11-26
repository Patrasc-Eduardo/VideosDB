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
   * Metoda care sorteaza un map (<String, Double>) dupa valoare sau dupa cheie.
   * in functie de variabila order, sortarea se face crescator sau descrescator, iar
   * daca este specificat prin @param apearCriteria ca se vrea o sortare dupa ordinea
   * de aparitie, aceasta conditie este de asemenea indeplinita.
   * Metoda este rensponsabila de sortarea tuturor map-urilor care apar in program, lucru realizat
   * prin conversia valorii unei chei la double (chiar daca numarul de vizionari sau ratinguri este
   * in realitate un int).
   *
   * @param map Map-ul mentionat mai sus.
   * @param sortType Tipul de soratare dorit.
   * @param apearCriteria Parametrul este != null daca se doreste sortare
   *                      si dupa ordinea de aparitie.
   * @param apOrder Lista care retine oridnea de aparitie a videoclipurilor in baza de date.
   * @param number Numarul de elemente returnate.
   * @return Lista de stringuri sortate (acestea pot reprezenta numele videourilor, userilor, etc.)
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

    HashMap<String, Double> newMap = new LinkedHashMap<>(); // LinkedHashMap pentru a pastra ordinea

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
