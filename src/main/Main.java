package main;

import action.Action;
import action.ActorQuery;
import action.Commands;
import action.Recommandation;
import action.UserQuery;
import action.VideoQuery;
import action.Query;
import actor.Actor;
import database.Database;
import video.Movie;
import video.Show;
import video.Video;
import database.User;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import fileio.Input;
import fileio.InputLoader;
import fileio.Writer;
import org.json.simple.JSONArray;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** The entry point to this homework. It runs the checker that tests your implentation. */
public final class Main {

  /** for coding style */
  private Main() { }

  /**
   * Call the main checker and the coding style checker
   *
   * @param args from command line
   * @throws IOException in case of exceptions to reading / writing
   */
  public static void main(final String[] args) throws IOException {
    File directory = new File(Constants.TESTS_PATH);
    Path path = Paths.get(Constants.RESULT_PATH);
    if (!Files.exists(path)) {
      Files.createDirectories(path);
    }

    File outputDirectory = new File(Constants.RESULT_PATH);

    Checker checker = new Checker();
    checker.deleteFiles(outputDirectory.listFiles());

    for (File file : Objects.requireNonNull(directory.listFiles())) {

      String filepath = Constants.OUT_PATH + file.getName();
      File out = new File(filepath);
      boolean isCreated = out.createNewFile();
      if (isCreated) {
        action(file.getAbsolutePath(), filepath);
      }
    }

    checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
    Checkstyle test = new Checkstyle();
    test.testCheckstyle();
  }

  /**
   * @param filePath1 for input file
   * @param filePath2 for output file
   * @throws IOException in case of exceptions to reading / writing
   */
  public static void action(final String filePath1, final String filePath2) throws IOException {
    InputLoader inputLoader = new InputLoader(filePath1);
    Input input = inputLoader.readData();

    Writer fileWriter = new Writer(filePath2);
    JSONArray arrayResult = new JSONArray();

    // TODO add here the entry point to your implementation

    Database mainDatabase = new Database(input);
    for (Action act : mainDatabase.getActionsList()) {

      String typeOfAction = act.getActionInp().getActionType();

      switch (typeOfAction) {
        case "command" -> {
          Commands comm = new Commands();

          String typeOfCommand = act.getActionInp().getType();

          Video vid = mainDatabase.getVideoByTitle(act.getActionInp().getTitle());
          User us = mainDatabase.getUserByName(act.getActionInp().getUsername());

          int id = act.getActionInp().getActionId();
          double grade = act.getActionInp().getGrade();
          int seasonNum = act.getActionInp().getSeasonNumber();

          if (typeOfCommand.compareTo("view") == 0) {
            arrayResult.add(comm.view(vid, us, id, fileWriter)); // apelam metoda de view
          }
          if (typeOfCommand.compareTo("favorite") == 0) {
            arrayResult.add(comm.favorite(vid, us, id, fileWriter));
          }
          if (typeOfCommand.compareTo("rating") == 0) {
            arrayResult.add(comm.rating(vid, us, id, grade, seasonNum, fileWriter));
          }
        }
        case "query" -> {
          Query query = new Query(mainDatabase);
          String typeOf = act.getActionInp().getObjectType();
          String typeOfCriteria = act.getActionInp().getCriteria();
          int id = act.getActionInp().getActionId();
          int queryNumber = act.getActionInp().getNumber();
          String sortType = act.getActionInp().getSortType();

          switch (typeOf) {
            case "actors":

              ActorQuery aquery = new ActorQuery(mainDatabase);
              ArrayList<Actor> actorsList = mainDatabase.getActorsList();

              if (typeOfCriteria.compareTo("average") == 0) {
              arrayResult.add(aquery.average(id, actorsList, queryNumber, sortType, fileWriter));
              }
              if (typeOfCriteria.compareTo("awards") == 0) {
                List<String> awards = act.getActionInp().getFilters().
                        get(Constants.AWARDS_POSITION);

                arrayResult.add(aquery.awards(id, actorsList, sortType, fileWriter,
                        awards));

              }
              if (typeOfCriteria.compareTo("filter_description") == 0) {
                List<String> words = act.getActionInp().getFilters().
                        get(Constants.WORDS_POSITION);
                arrayResult.add(aquery.filterDescription(id, actorsList, sortType, fileWriter,
                        words));
              }
              break;
            case "movies":
            case "shows":
              VideoQuery vquery = new VideoQuery(mainDatabase);
              ArrayList<Video> videos = new ArrayList<>();

              if (typeOf.compareTo("movies") == 0) {
                for (Movie mv : mainDatabase.getMoviesList()) {
                  videos.add(mv);
                }
              } else {
                for (Show show : mainDatabase.getShowsList()) {
                  videos.add(show);
                }
              }
              List<String> yearList = act.getActionInp().getFilters().
                      get(Constants.YEAR_POSITION);
              List<String> genreList = act.getActionInp().getFilters().
                      get(Constants.GENRE_POSITION);
              int year = 0;
              if (yearList.get(0) != null) {
                year = Integer.parseInt(yearList.get(0));
              }
              String genre = genreList.get(0);

              if (typeOfCriteria.compareTo("ratings") == 0) {
                arrayResult.add(vquery.rating(id, videos, queryNumber, sortType,
                        year, genre, fileWriter));
              }
              if (typeOfCriteria.compareTo("favorite") == 0) {
                ArrayList<User> users = mainDatabase.getUsersList();
                arrayResult.add(vquery.favorite(id, videos, users, queryNumber, sortType,
                        year, genre, fileWriter));
              }
              if (typeOfCriteria.compareTo("longest") == 0) {

                arrayResult.add(vquery.longest(id, videos, queryNumber, sortType,
                        year, genre, fileWriter));

              }
              if (typeOfCriteria.compareTo("most_viewed") == 0) {
                ArrayList<User> users = mainDatabase.getUsersList();
                arrayResult.add(vquery.mostViewed(id, videos, users, queryNumber, sortType,
                        year, genre, fileWriter));
              }
              break;
            case "users":
              UserQuery uquery = new UserQuery(mainDatabase);
              if (typeOfCriteria.compareTo("num_ratings") == 0) {
                ArrayList<User> users = mainDatabase.getUsersList();
                arrayResult.add(uquery.numRatings(id, users, queryNumber, sortType, fileWriter));

              }
              break;
            default : System.out.println("No type found !");
          }
        }
        case "recommendation" -> {

          Recommandation recom = new Recommandation(mainDatabase);
          User us = mainDatabase.getUserByName(act.getActionInp().getUsername());
          int id = act.getActionInp().getActionId();
          ArrayList<Video> allVids = new ArrayList<>();
          allVids.addAll(mainDatabase.getMoviesList());
          allVids.addAll(mainDatabase.getShowsList());
          String type = act.getActionInp().getType();

          if (type.compareTo("standard") == 0) {
          arrayResult.add(recom.standard(id, allVids, us, fileWriter));
          break;
          }

          if (type.compareTo("best_unseen") == 0) {

            arrayResult.add(recom.bestUnseen(id, allVids, us, fileWriter));
            break;
          }

          if (us.getSubscriptionType().compareTo("PREMIUM") == 0) {

            ArrayList<User> allUsers = mainDatabase.getUsersList();

            if (type.compareTo("popular") == 0) {
            arrayResult.add(recom.popular(id, allVids, us, allUsers, fileWriter));
            break;
            }
            if (type.compareTo("favorite") == 0) {
              arrayResult.add(recom.favorite(id, allVids, us, allUsers, fileWriter));
              break;
            }
            if (type.compareTo("search") == 0) {
              String genre = act.getActionInp().getGenre();
              arrayResult.add(recom.search(id, allVids, us, allUsers, genre, fileWriter));
              break;
            }
          }

            arrayResult.add(fileWriter.writeFile(id, null,
                    "PopularRecommendation cannot be applied!"));

        }
        default -> System.out.println("No command found !");
      }
    }
    fileWriter.closeJSON(arrayResult);
  }
}
