package com.company.enroller.persistence;

import java.util.Collection;
import java.util.List;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	// Get all participants
	public Collection<Participant> getAll() {
		String hql = "FROM Participant";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	// 1.1. Find participant by login
	public Participant findByLogin(String login) {

		String hql = "FROM Participant WHERE login = :login";

		Query<Participant> query =
				connector.getSession().createQuery(hql, Participant.class);

		query.setParameter("login", login);

		return query.uniqueResult();
	}

	// 1.2. Add new participant
	public void add(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(participant);
		transaction.commit();
	}

	// 2.1. Remove participant
	public void delete(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(participant);
		transaction.commit();
	}

	// 2.1.2. Remove participant
	public void update(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().merge(participant);
		transaction.commit();
	}

	// 2.2. Sort participants
	public List<Participant> getAll(String sortBy, String sortOrder) {

		String hql = "FROM Participant";

		if (sortBy != null && sortOrder != null) {
			hql += " ORDER BY " + sortBy + " " + sortOrder;
		}

		return connector.getSession()
				.createQuery(hql, Participant.class)
				.list();
	}

	// 2.3. Filter parameter
	public List<Participant> getAll(String key) {

		String hql =
				"FROM Participant WHERE login LIKE :key";

		Query<Participant> query =
				connector.getSession()
						.createQuery(hql, Participant.class);

		query.setParameter("key", "%" + key + "%");

		return query.list();
	}
}
