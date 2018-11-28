package com.niit.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.niit.model.Category;
import com.niit.model.Product;
@Repository
@Transactional //commit and rollback
public class ProductDaoImpl implements ProductDao {
	@Autowired//Spring container auto wire bean of type SessionFactory
private SessionFactory sf;
	
	public ProductDaoImpl(){
		System.out.println("ProductDaoImpl bean is created");
	}
	
	public void addProduct(Product product) {//product is input
		//database operation -insert product details in product table
	//	System.out.println("Before inserting product " + product.getId());
		Session session=sf.getCurrentSession();
		session.save(product);//permanently store the product object in database, session.sav
		//System.out.println("After inserting product " + product.getId());
		
	}

	public void updateProduct(Product product) {//product.id=5
		Session session=sf.getCurrentSession();
		session.update(product);//-> details will get updated in the table
	}

	public Product selectProducts(int id) {
		Session session=sf.getCurrentSession();
		Product product=(Product)session.get(Product.class, id);
		return product;
	}

	public void deleteProducts(int id) {
		Session session=sf.getCurrentSession();
		Product product=(Product)session.get(Product.class, id);
		if(product!=null)
		session.delete(product);//delete from product where id=1
	}

	public List<Product> getAllProducts() {
		Session session=sf.getCurrentSession();
		Query query=session.createQuery("from Product");//HQL ,Product is java class name/entity name
		 List<Product> products= query.list();
		 return products;
		//hibernate will get converted into SQL query 
		//and fetch all the records 
		//and convert it into list of products
	}

	public List<Category> getAllCategories() {
		Session session=sf.openSession();
		Query query=session.createQuery("from Category");
		List<Category> categories=(List<Category>)query.list();
		return categories;
	
	
	}

	
	public Product getId(int id) {
		Session session=sf.openSession();
		Transaction tans=session.beginTransaction();
		/*Query q=session.createQuery(query); here Query from hibernate query*/
		Product product= (Product) session.get(Product.class, id);
		tans.commit();
		session.flush();
		session.close();
		return product;
	}


}
