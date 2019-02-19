package uk.co.firefly.library.rest.version0_1.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import uk.co.firefly.library.Application;
import uk.co.firefly.library.rest.version0_1.model.Author;
import uk.co.firefly.library.rest.version0_1.repository.AuthorRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@EnableAutoConfiguration
@ContextConfiguration(classes = Application.class)
public class AuthorServiceTest {
	
	@Autowired
	private AuthorService service;
	
	@Autowired
	private AuthorRepository repository;
	
	@Test
	public void save() {
		Author author = new Author("firstName", "middleName", "lastName", "about");
		Author savedAuthor = service.save(author);
		Assert.assertEquals(savedAuthor.getAbout(), "about");
		repository.delete(savedAuthor);
	}
	
	@Test
	public void getAll() {
		Author author = new Author("firstName", "middleName", "lastName", "about"); 
		Author author2 = new Author("firstName2", "middleName2", "lastName2", "about2");
		Author savedAuthor = service.save(author);
		Author savedAuthor2 = service.save(author2);
		Assert.assertTrue(service.getAll().size() == 2);
		repository.delete(savedAuthor);
		repository.delete(savedAuthor2);
	}
	
	@Test
	public void get() {
		Author author = new Author("firstName", "middleName", "lastName", "about"); 
		Author savedAuthor = service.save(author);
		Assert.assertEquals("firstName", service.get(savedAuthor.getId()).getFirstName());
		service.delete(savedAuthor.getId());
	}
	
	@Test
	public void delete() {
		Author author = new Author("firstName", "middleName", "lastName", "about"); 
		Author savedAuthor = service.save(author);
		service.delete(savedAuthor.getId());
		Assert.assertTrue(service.getAll().size() == 0);
	}
	
	
	@Test
	public void put() {
		Author author = new Author("firstName", "middleName", "lastName", "about"); 
		Author savedAuthor = service.save(author);
		Assert.assertEquals(savedAuthor.getAbout(), "about");
		Author newAuthor = new Author("firstNameTwo", "middleNameTwo", "lastNameTwo", "aboutTwo");
		Author savedAuthorTwo = service.put(savedAuthor.getId(), newAuthor);
		Assert.assertEquals(savedAuthorTwo.getAbout(), "aboutTwo");
		Assert.assertEquals(savedAuthorTwo.getFirstName(), "firstNameTwo");
		Assert.assertEquals(savedAuthorTwo.getMiddleName(), "middleNameTwo");
		Assert.assertEquals(savedAuthorTwo.getLastName(), "lastNameTwo");
		repository.delete(savedAuthor);
	}
	
}