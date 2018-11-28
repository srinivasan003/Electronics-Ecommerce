package com.niit.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.niit.model.Authorities;
import com.niit.model.Customer;
import com.niit.model.User;
@Repository
@Transactional
public class CustomerDaoImpl implements CustomerDao {
	@Autowired
private SessionFactory sessionFactory;
	public void registerCustomer(Customer customer) {
	Session session=sessionFactory.getCurrentSession();
	//Make user property in Authorities to refer user object
	User user=customer.getUser();
	Authorities authorities=user.getAuthorities();
	authorities.setUser(user);//SET A VALUE FOR FK IN AUTHORITIES TABLE
//	
	
	session.save(customer);

	}
	public boolean isEmailUnique(String email) {//james.s@niit.com
		Session session=sessionFactory.getCurrentSession();
		User user=(User)session.get(User.class, email);//select * from user where email='james.s@niit.com'
		if(user==null)//entered email is unique
			return true;
		else //entered email already exists in the table
			return false;
	}
	public User getUser(String email) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.getCurrentSession();
		User user=(User)session.get(User.class, email);
		return user;
	}

}
