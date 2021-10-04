package de.hda.fbi.db2.stud.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Category", schema = "dbmo3y")
@NamedQueries({
    @NamedQuery(name = "Category.findByName",
        query = "select c from Category c where c.name=:name"),
    @NamedQuery(name = "Category.findAll",
        query = "select c from Category c")
})
public class Category {

  public Category() {

  }

  public Category(String name) {
    this.name = name;
    this.questions = new ArrayList<>();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(unique = true)
  private String name;

  public String getName() {
    return name;
  }

  @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
  private List<Question> questions;

  public List<Question> getQuestions() {
    return questions;
  }

  public void addQuestion(Question question) {
    questions.add(question);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Category category = (Category) o;
    return id == category.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
