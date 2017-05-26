package edu.gsu.csc2720.prj3.atran;

import org.eareddick.whitechapel.Detectives;

public class Project3Tester {

	public static void main(final String[] args) {
		Night night = new Night(1);
		Jack jack = new Jack(night.getCoachAlley(), night.getCrimeScene()[0]);
		Detectives detectives = night.getDetectives();
		NightController nc = new NightController(night, jack, detectives, "C65");
		nc.run();
		nc.run();
	}
}
