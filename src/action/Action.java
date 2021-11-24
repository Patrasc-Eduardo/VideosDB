package action;

import fileio.ActionInputData;

public class Action {

  private ActionInputData actionInp;

  public Action(final ActionInputData in) {
    this.actionInp = in;
  }

  public Action() { }

  public final ActionInputData getActionInp() {
    return actionInp;
  }

  @Override
  public final String toString() {
    return "ActionInputData{"
        + "actionId="
        + this.getActionInp().getActionId()
        + ", actionType='"
        + this.getActionInp().getActionType()
        + '\''
        + ", type='"
        + this.getActionInp().getType()
        + '\''
        + ", username='"
        + this.getActionInp().getUsername()
        + '\''
        + ", objectType='"
        + this.getActionInp().getObjectType()
        + '\''
        + ", sortType='"
        + this.getActionInp().getSortType()
        + '\''
        + ", criteria='"
        + this.getActionInp().getCriteria()
        + '\''
        + ", title='"
        + this.getActionInp().getTitle()
        + '\''
        + ", genre='"
        + this.getActionInp().getGenre()
        + '\''
        + ", number="
        + this.getActionInp().getNumber()
        + ", grade="
        + this.getActionInp().getGrade()
        + ", seasonNumber="
        + this.getActionInp().getSeasonNumber()
        + ", filters="
        + this.getActionInp().getFilters()
        + '}'
        + "\n";
  }
}
