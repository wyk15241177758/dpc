package com.jt.test.exportTest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class JAXBDemo {

	
	

	private List<Person> person;
	@XmlElementWrapper(name = "persons")
	@XmlElement(name = "person")
	public List<Person> getPerson() {
		return person;
	}

	public void setPerson(List<Person> person) {
		this.person = person;
	}

	public static void main(String[] args) {
		Person a=new Person("zxb",11);
		Person b=new Person("gd",11);
		List<Person> list = new ArrayList<Person>();
		list.add(a);
		list.add(b);
		JAXBDemo demo = new JAXBDemo();
		demo.setPerson(list);
		
		
//		List persons = new LinkedList();
//		persons .add(new Person("zhao", 21));
//		persons.add(new Person("zhang", 11));
//		demo.setPersons(persons);
		
		JAXB.marshal(demo, System.out);
	}
	

	
	
}
