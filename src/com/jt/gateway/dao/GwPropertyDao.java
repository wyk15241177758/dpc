package com.jt.gateway.dao;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface GwPropertyDao {
	public void add(String key,String value) throws FileNotFoundException;
	public void del(String key) throws FileNotFoundException;
	public void update(String key ,String value) throws FileNotFoundException;
	public String get(String key) throws IOException;
	public void save() throws IOException;
}
