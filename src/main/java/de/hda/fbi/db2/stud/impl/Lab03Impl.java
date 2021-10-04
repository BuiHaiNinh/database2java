package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab03Game;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Game;
import de.hda.fbi.db2.stud.entity.GameQuestion;
import de.hda.fbi.db2.stud.entity.Question;
import de.hda.fbi.db2.stud.entity.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder.In;

public class Lab03Impl extends Lab03Game {

  BufferedReader reader =
      new BufferedReader(new InputStreamReader(System.in, Charset.defaultCharset()));

  EntityManager entityManager;
  Random random = new Random();

  @Override
  public void init() {
    entityManager = lab02EntityManager.getEntityManager();
    super.init();
  }

  @Override
  public Object getOrCreatePlayer(String playerName) {
    try {
      User user = entityManager.createNamedQuery("User.findByName", User.class)
          .setParameter("name", playerName).getSingleResult();
      return user;
    } catch (NoResultException exception) {
      return new User(playerName);
    }
  }

  @Override
  public Object interactiveGetOrCreatePlayer() {
    System.out.println("Player name:");
    try {
      String name = reader.readLine();
      return getOrCreatePlayer(name);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public List<?> getQuestions(List<?> categories, int amountOfQuestionsForCategory) {
    List<Question> result = new ArrayList<Question>();
    Set<Integer> randoms = new HashSet<>();

    for (Object object : categories) {
      if (object instanceof Category) {
        Category category = (Category) object;

        Integer index;
        do {
          index = random.nextInt(category.getQuestions().size());
        } while (randoms.contains(index));
        randoms.add(index);

        int x = 0;
        while (x < amountOfQuestionsForCategory && x < category.getQuestions().size()) {
          result.add(category.getQuestions().get(x));
          x++;
        }

      }
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<?> interactiveGetQuestions() {

    List<Category> categories = new ArrayList<>();

    String categoryName = "";

    //All Categories anzeigen
    System.out.println("All categories in datenbank: ");
    List<String> categoriesList = this.lab02EntityManager.getEntityManager()
        .createQuery("select c.name from Category c").getResultList();
    int no = 0;
    for (Iterator i = categoriesList.iterator(); i.hasNext(); ) {
      String cate = (String) i.next();
      System.out.println(no + ". " + cate);
      no++;
    }

    //Choose categories and amount for each of them
    do {
      try {
        System.out
            .println("Type your favorite categorie names and enter (type 0 to stop selection): ");

        categoryName = reader.readLine();

        Category category = this.lab02EntityManager.getEntityManager()
            .createNamedQuery("Category.findByName", Category.class)
            .setParameter("name", categoryName).getSingleResult();
        categories.add(category);

      } catch (IOException e) {
        break;
      } catch (NoResultException exception) {
        System.out.println("This Category could not be found");
      }
    } while (categoryName == null || !categoryName.equals("0"));

    System.out.println("Type amount of questions for each categories");
    int amount = 0;
    while (amount == 0) {
      try {
        String line = reader.readLine();
        if (line == null) {
          continue;
        }
        amount = Integer.parseInt(line);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return getQuestions(categories, amount);
  }

  @Override
  public Object createGame(Object player, List<?> questions) {

    User user = null;
    if (player instanceof User) {
      user = (User) player;
    } else {
      return null;
    }

    Game game = new Game(new Date(), user);

    for (Object object : questions) {
      if (object instanceof Question) {
        Question question = (Question) object;

        GameQuestion gameQuestion = new GameQuestion(question);
        gameQuestion.setGame(game);
        game.addGameQuestion(gameQuestion);
      }
    }

    user.addGame(game);
    return game;
  }

  @Override
  public void playGame(Object game) {
    Game ga;
    if (game instanceof Game) {
      ga = (Game) game;
    } else {
      return;
    }

    for (GameQuestion gameQuestion : ga.getQuestionsList()) {

      int choose = new Random().nextInt(4);

      gameQuestion.setCorrect(gameQuestion.getQuestion().getIndexCorrectAnswer() == choose);

    }

  }

  @Override
  public void interactivePlayGame(Object game) {
    Game ga = null;
    if (game instanceof Game) {
      ga = (Game) game;
    } else {
      return;
    }

    System.out.println("Start Game :D");
    int index = 1;
    for (GameQuestion gameQuestion : ga.getQuestionsList()) {
      System.out.println("Question " + index + ": " + gameQuestion.getQuestion().getValue());

      int i = 1;
      for (String answer : gameQuestion.getQuestion().getAnswers()) {
        System.out.println(i + ". " + answer);
        i++;
      }

      int choose = -1;
      while (choose < 1 || choose > 4) {
        String input = "-1";
        try {
          input = reader.readLine();
          if (input == null) {
            continue;
          }

          choose = Integer.parseInt(input);
        } catch (Exception e) {
          System.out.println("Try Again");
        }
      }

      System.out.println("Ihr Antwort:: " + choose);
      System.out.println(
          "Richtiger Antwort: " + (gameQuestion.getQuestion().getIndexCorrectAnswer() + 1));
      System.out.println("Ihrer Antwort ist " + (
          ((gameQuestion.getQuestion().getIndexCorrectAnswer() + 1) == choose) ? "RICHTIG"
              : "FALSCH"));

      gameQuestion.setCorrect(gameQuestion.getQuestion().getIndexCorrectAnswer() == (choose - 1));
      index++;

      System.out.println();
    }

    // Result
    int amountCorrect = 0;
    for (GameQuestion gameQuestion : ga.getQuestionsList()) {
      if (gameQuestion.isCorrect()) {
        amountCorrect++;
      }
    }
    System.out.println("Result: " + amountCorrect + "/" + ga.getQuestionsList().size());
  }

  @Override
  public void persistGame(Object game) {

    Game gm = null;
    if (game instanceof Game) {
      gm = (Game) game;
    } else {
      return;
    }

    entityManager.getTransaction().begin();

    entityManager.persist(gm.getUser());

    entityManager.getTransaction().commit();
  }
}
