package com.wingulabs.whitechapel;

public class Project3Tester {

	public static void main(final String[] args) {
		System.out.println("git testing");
		Night night = new Night(1);
		Jack jack = new Jack(night.getCoachAlley(), night.getCrimeScene().get(0));
		MyDetectives detectives =  night.getDetectives();
		NightController nc = new NightController(night, jack, detectives, "C65");
		nc.run();
		nc.run();
	}
}
