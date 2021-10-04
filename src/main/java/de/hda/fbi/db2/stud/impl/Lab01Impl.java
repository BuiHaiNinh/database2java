package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab01Data;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Question;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lab01Impl extends Lab01Data {

  @Override
  public List<?> getQuestions() {
    return questions;
  }

  @Override
  public List<?> getCategories() {
    return new ArrayList<>(categories.values());
  }

  Map<String, Category> categories = new HashMap<>();
  List<Question> questions = new ArrayList<>();

  @Override
  public void loadCsvFile(List<String[]> additionalCsvLines) {
    additionalCsvLines.remove(0);

    for (String[] line : additionalCsvLines) {
      Category category = categories.get(line[7]);
      if (category == null) {
        category = new Category(line[7]);
        categories.put(category.getName(), category);
      }

      Question question = new Question(Integer.parseInt(line[0]), line[1], category);
      category.addQuestion(question);

      for (int i = 2; i < 6; i++) {
        question.addAnswer(line[i]);
      }

      question.setIndexCorrectAnswer(Integer.parseInt(line[6]) - 1);
      questions.add(question);
    }

    System.out.println("Anzahl der Fragen: " + questions.size());
    System.out.println("Anzahl der Kategorien: " + categories.size());
    System.out.println();

    for (Question question : questions) {
      System.out.println("Frage: " + question.getValue());
      System.out.println("Kategorie: " + question.getCategory().getName());
      System.out.println(("Korrekte Antwort: " + (question.getIndexCorrectAnswer() + 1)));
      for (int x = 0; x < 4; x++) {
        System.out.println("  " + (x + 1) + ". " + question.getAnswers().get(x));
      }
      System.out.println();
    }
  }
}
