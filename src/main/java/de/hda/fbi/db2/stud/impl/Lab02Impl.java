package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab02EntityManager;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Question;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Lab02Impl extends Lab02EntityManager {

  EntityManager entityManager;

  @Override
  public void persistData() {
    entityManager.getTransaction().begin();

    for (Object object : lab01Data.getCategories()) {
      if (object instanceof Category) {
        Category category = (Category) object;
        entityManager.persist(category);
      }
    }

    for (Object object : lab01Data.getQuestions()) {
      if (object instanceof Question) {
        Question question = (Question) object;
        entityManager.persist(question);
      }
    }
    entityManager.getTransaction().commit();
  }

  @Override
  public EntityManager getEntityManager() {
    EntityManagerFactory factory = Persistence
        .createEntityManagerFactory("docker-local-postgresPU");

    entityManager = factory.createEntityManager();
    return entityManager;
  }
}
