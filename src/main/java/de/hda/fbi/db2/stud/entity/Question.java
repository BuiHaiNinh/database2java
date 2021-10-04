package de.hda.fbi.db2.stud.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Entity
@Table(name = "Question", schema = "dbmo3y")
public class Question {

  public Question() {

  }

  /**
   * Default constructor.
   *
   * @param id       primary key
   * @param value    The question string
   * @param category Category
   */
  public Question(int id, String value, Category category) {
    this.id = id;
    this.value = value;
    this.category = category;
    this.answers = new ArrayList<>();
  }

  @Id
  private int id;

  private String value;

  public int getId() {
    return id;
  }

  public String getValue() {
    return value;
  }

  private Integer indexCorrectAnswer;

  public Integer getIndexCorrectAnswer() {
    return indexCorrectAnswer;
  }

  public void setIndexCorrectAnswer(Integer indexCorrectAnswer) {
    this.indexCorrectAnswer = indexCorrectAnswer;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(schema = "dbmo3y")
  @OrderColumn(name = "reihenfolge")
  @OrderBy("reihenfolge")
  private List<String> answers;

  public List<String> getAnswers() {
    return answers;
  }

  public void addAnswer(String answer) {
    answers.add(answer);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Question question = (Question) o;
    return id == question.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
