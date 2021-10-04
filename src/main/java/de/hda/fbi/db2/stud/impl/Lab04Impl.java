package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab04MassData;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Game;
import de.hda.fbi.db2.stud.entity.GameQuestion;
import de.hda.fbi.db2.stud.entity.Question;
import de.hda.fbi.db2.stud.entity.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class Lab04Impl extends Lab04MassData {

  private static final int playerNr = 10000;
  //  private static final int playerNr = 1000;
  private static final int gamesPerPlayer = 100;
  private final Random random = new Random();

  @Override
  public void init() {
    super.init();
  }

  @Override
  public void createMassData() {
    System.out.println("Generate mass data...");

    long start = new Date().getTime();

    this.generate();

    long runningTime = new Date().getTime() - start;
    System.out.println(runningTime / 10000);
  }

  @SuppressWarnings("deprecation")
  private void generate() {
    EntityManager entityManager = lab02EntityManager.getEntityManager();

    List<Category> categories = entityManager.createNamedQuery("Category.findAll", Category.class)
        .getResultList();

    entityManager.getTransaction().begin();

    long startDate = new Date("06/01/2020").getTime();
    long endDate = new Date("06/30/2020").getTime();
    long delta = endDate - startDate;

    Random r = new Random();

    int batchSize = 100;
    int count = 0;
    for (int p = 1; p <= playerNr; p++) { //players
      User user = new User("Player" + p);

      for (int i = 0; i < gamesPerPlayer; i++) {

        long date = startDate + (long) (r.nextDouble() * delta);

        var game = new Game(new Date(date), user);

        var randomQuestion = randomQuestions(categories);
        for (Question question : randomQuestion) {

          GameQuestion gameQuestion = new GameQuestion(question);
          gameQuestion.setGame(game);
          gameQuestion.setCorrect(random.nextInt(4) == question.getIndexCorrectAnswer());

          game.addGameQuestion(gameQuestion);
        }
        user.addGame(game);
      }

      entityManager.persist(user);

      if (count >= batchSize) {
        entityManager.flush();
        entityManager.clear();
        count = 0;
      } else {
        count++;
      }

    }
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @SuppressWarnings("unchecked")
  private List<Question> randomQuestions(List<Category> categories) {
    Random random = new Random();
    Set<Integer> randoms = new HashSet<>();

    var questions = new ArrayList<Question>();
    while (questions.size() < 12) {

      int index;
      do {
        index = random.nextInt(categories.size());
      } while (randoms.contains(index));
      randoms.add(index);

      var tempQuestions = lab03Game
          .getQuestions(Collections.singletonList(categories.get(index)), 5);
      questions.addAll((Collection<? extends Question>) tempQuestions);
    }

    return questions;
  }

  @SuppressWarnings("unchecked")
  public void printStatistics() {
    EntityManager em = lab02EntityManager.getEntityManager();

    // ========================= Frage 1 ===================================
    Date startDate = new Date("06/10/2020");
    Date endDate = new Date("06/11/2020");

    Query query1 = em.createQuery("select distinct g.user.name from Game g "
        + "where g.created between :start and :end");
    query1.setParameter("start", startDate);
    query1.setParameter("end", endDate);
    List<String> query1ResultList = query1.getResultList();
    System.out.println("Alle Spieler in einem Zeitraum " + startDate + " => " + endDate);
    for (var object : query1ResultList) {
      System.out.println(object);
    }

    // =====================================================================
    System.out.println();

    // ========================= Frage 3 ===================================

    String userName = "Player4";
    Query query3 = em.createQuery(
        "SELECT game.id, game.created, SUM(CASE WHEN gq.isCorrect = TRUE then 1 else 0 end), COUNT(game.id)"
            + "FROM GameQuestion gq, gq.game game, game.user user "
            + "WHERE user.name = :user "
            + "GROUP BY game.id");
    query3.setParameter("user", userName);
    List<Object[]> query3ResultList = query3.getResultList();
    System.out.println("Frage 2:");
    for (var result : query3ResultList) {
      //TODO: Fix
      System.out.println(Arrays.toString(result));
    }

    // =====================================================================
    System.out.println();

    // ========================= Frage 3 ===================================
    Query query6 = em.createQuery(
        "SELECT p.name, COUNT(g.id) " +
            "FROM User p, p.games g " +
            "GROUP BY p.name " +
            "ORDER BY COUNT(g.id) DESC"
    );
    System.out.println("Spieler nach Anzahl der Spiele sortiert:");
    List<Object[]> query6ResultList = query6.getResultList();
    for (var user : query6ResultList) {
      System.out.println("Name: " + user[0] + " . Games count: " + user[1]);
    }
    // =====================================================================

    System.out.println();

    // ========================= Frage 4 ===================================

    Query query4 = em.createQuery("SELECT category.name, COUNT(gq.id) "
        + "FROM GameQuestion gq, gq.question.category category "
        + "GROUP BY category.name "
        + "ORDER BY COUNT(gq.id) DESC");
    List<Object[]> query4ResultList = query4.getResultList();
    for (var category : query4ResultList) {
      System.out.println(Arrays.toString(category));
    }
    // =====================================================================

  }


}
