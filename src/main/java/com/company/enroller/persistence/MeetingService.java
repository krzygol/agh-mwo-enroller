package com.company.enroller.persistence;

import java.util.Collection;

import com.company.enroller.model.Participant;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {

		connector = DatabaseConnector.getInstance();
	}

	// 3.1. Pobieranie listy wszystkich spotkań
	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	// 3.2. Pobieranie listy pojedyncznego spotkania
	public Meeting findById(long id) {

		String hql = "FROM Meeting WHERE id = :id";

		Query<Meeting> query =
				connector.getSession().createQuery(hql, Meeting.class);

		query.setParameter("id", id);

		return query.uniqueResult();
	}

	// 3.3. Dodawanie spotkań
	public void add(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}

	// 3.4. Usuwanie spotkań
	public void delete(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(meeting);
		transaction.commit();
	}

	// 3.5.Aktualizację spotkań
	public void update(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().merge(meeting);
		transaction.commit();
	}

	// Save meeting
	public void save(Meeting meeting) {

		Session session = connector.getSession();

		Transaction transaction =
				session.beginTransaction();

		session.saveOrUpdate(meeting);

		transaction.commit();
	}
}
