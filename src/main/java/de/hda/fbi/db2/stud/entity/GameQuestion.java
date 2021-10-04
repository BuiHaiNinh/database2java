package de.hda.fbi.db2.stud.entity;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "GameQuestion", schema = "dbmo3y")
public class GameQuestion {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private int id;

  public void setQuestion(Question question) {
    this.question = question;
  }

  @ManyToOne
  private Question question;

  @ManyToOne
  private Game game;

  private boolean isCorrect;

  public GameQuestion() {

  }

  public GameQuestion(Question question) {
    this.question = question;
  }

  public void setCorrect(boolean correct) {
    isCorrect = correct;
  }

  public boolean isCorrect() {
    return isCorrect;
  }

  public Game getGame() {
    return game;
  }

  public Question getQuestion() {
    return question;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameQuestion that = (GameQuestion) o;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
