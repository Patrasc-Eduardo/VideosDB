package action;

import database.Database;
/** Some javadoc. */
public class Query extends Action {
  private Database db;

  public Query(final Database db) {
    this.db = db;
  }
  /** Some javadoc. */
  public Query() { }

  /** Some javadoc. */
  public Database getDb() {
    return db;
  }
}
