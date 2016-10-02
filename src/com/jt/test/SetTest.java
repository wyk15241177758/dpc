package com.jt.test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class SetTest {
public static void main(String[] args) {
	Queue<String> q=new LinkedList<String>();
	q.offer("1");
	q.offer("2");
	q.offer("3");
	q.remove();
	for(String s:q){
		System.out.println(s);
	}
}
}
