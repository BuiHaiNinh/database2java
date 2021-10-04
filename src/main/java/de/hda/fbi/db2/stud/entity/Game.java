package de.hda.fbi.db2.stud.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Game", schema = "dbmo3y")
public class Game {

  public Game() {

  }

  /**
   * Create Game.
   *
   * @param created Date
   */
  public Game(Date created) {
    this.created = (Date) created.clone();
    this.questionsList = new ArrayList<>();
  }

  /**
   * Create Game.
   *
   * @param created Date
   * @param user    User
   */
  public Game(Date created, User user) {
    this.created = (Date) created.clone();

    this.user = user;
    this.questionsList = new ArrayList<>();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private int id;

  @Basic(fetch = FetchType.LAZY)
  @Temporal(TemporalType.DATE)
  private Date created;

  @ManyToOne()
  private User user;

  @OneToMany(mappedBy = "game", cascade = CascadeType.PERSIST)
  private List<GameQuestion> questionsList;

  public void addGameQuestion(GameQuestion gameQuestion) {
    questionsList.add(gameQuestion);
  }

  public User getUser() {
    return user;
  }

  public List<GameQuestion> getQuestionsList() {
    return questionsList;
  }

  public Date getCreated() {
    return (Date) created.clone();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Game game = (Game) o;
    return id == game.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
