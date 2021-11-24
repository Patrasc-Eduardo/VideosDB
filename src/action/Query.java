package action;

import database.Database;

public class Query extends Action {
  private Database db;

  public Query(final Database db) {
    this.db = db;
  }
  /**
   * Some javadoc.
   *
   * @param Some javadoc.
   * @return Some javadoc.
   */
  public Database getDb() {
    return db;
  }
}
