package vn.mekosoft.designpattern.test;

import vn.mekosoft.designpattern.singleton.SingletonKey;

public class TestPattern {
	public static void main(String[] args) {
		testSingleton();
	}
	public static void testSingleton() {
		SingletonKey singletonKey = SingletonKey.getInstance();
		singletonKey.openDoor();
	}
}
