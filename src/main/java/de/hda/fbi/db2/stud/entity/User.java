package de.hda.fbi.db2.stud.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "User", schema = "dbmo3y")
@NamedQueries({
    @NamedQuery(name = "User.findByName",
        query = "select u from User u where u.name=:name")
})
public class User {

  public User() {
    this.games = new LinkedList<>();
  }

  public User(String name) {
    this.games = new LinkedList<>();
    this.name = name;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private int id;

  @Column(name = "name")
  private String name;

  public String getName() {
    return name;
  }

  @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
  private List<Game> games;

  public List<Game> getGames() {
    return games;
  }

  public void addGame(Game game) {
    games.add(game);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return id == user.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }


}
