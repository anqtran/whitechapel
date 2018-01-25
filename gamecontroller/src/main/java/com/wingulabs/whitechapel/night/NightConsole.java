package com.wingulabs.whitechapel.night;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.wingulabs.whitechapel.Utility.ConsoleUtility;
import com.wingulabs.whitechapel.detectives.Detective;
import com.wingulabs.whitechapel.detectives.Detectives;
import com.wingulabs.whitechapel.gameBoard.Edge;
import com.wingulabs.whitechapel.gameBoard.GameBoard;

/**
 * Generates the night of whitechapel from the user's input.
 *
 * @author anqtr
 *
 */
public class NightConsole extends Night {
    /**
     * GameBoard of the game.
     */
    private GameBoard gb = GameBoard.SINGLETON;
    /**
     * all the crime scenes on the board.
     */
    protected Set<String> crimeSceneSet;
    /**
     * All the starting point of the detectives.
     */
    protected Set<String> detectivesStartingLocation;

    /**
     * Night Console Constructor.
     *
     * @param currentNight
     *            number of night.
     * @param detectivesStartingLocation
     *            starting locations of detectives.
     * @param crimeSceneSet
     *            starting locations of Jack.
     */
    public NightConsole(final int currentNight, final Set<String> detectivesStartingLocation,
            final Set<String> crimeSceneSet) {
        super(currentNight);
        this.crimeSceneSet = new HashSet<String>(crimeSceneSet);
        this.markedToken = markedTokenGenerator();
        this.fakeToken = fakeTokenGenerator();
        this.detectivesStartingLocation = detectivesStartingLocationGenerator(currentNight, detectivesStartingLocation);
        this.detectives = detectivesGenerator();
        this.fakePatrolToken = fakePatrolTokenGenerator();
        this.crimeScene = crimeSceneGenerator();
    }

    /**
     * Generates the set of starting locations of detectives from the given set.
     * 
     * @param detectivesStartingLocation
     *            set of starting locations
     * @return the set of detectives' locations in the night.
     */
    protected final Set<String> detectivesStartingLocationGenerator(int nightNo,
            Set<String> detectivesStartingLocation) {
        if (nightNo == 1) {
            return new HashSet<>(detectivesStartingLocation);
        } else {
            Set<String> startingLocation = new HashSet<String>(detectivesStartingLocation);
            GameBoard gb = GameBoard.SINGLETON;
            Set<String> yellowSquare = new HashSet<String>(gb.YELLOW_SQUARES);
            yellowSquare.removeAll(startingLocation);
            System.out.println("Detectives start at 5 location at the end " + "of the previous night.");
            for (int i = 0; i < 2; i++) {
                System.out.println("Select two more tokens" + " from the yellow starting square: ");
                String token = ConsoleUtility.selectLocationfromSet(yellowSquare);
                startingLocation.add(token);
                yellowSquare.remove(token);
            }
            if (nightNo == 3) {
                String fakeCrimeScene = fakeCrimeSceneGenerator();
                super.setFakeCrimeScene(fakeCrimeScene);
            }
            return startingLocation;
        }
    }

    /**
     * Ask Jack to place the marked token to set up the night.
     *
     * @return the locations of all the marked token.
     */
    protected final List<String> markedTokenGenerator() {
        System.out.println("Setting up Crime Scene! ");
        List<String> markedToken = new ArrayList<>();
        int numberOfMarkedToken = womenToken[0];
        for (int i = 0; i < numberOfMarkedToken; i++) {
            System.out.println("Jack is choosing marked token: ");
            String choseToken = ConsoleUtility.selectLocationfromSet(crimeSceneSet);
            markedToken.add(choseToken);
            crimeSceneSet.remove(choseToken);
        }
        startingMarkedToken = new ArrayList<String>(markedToken);
        return markedToken;
    }

    /**
     * Ask Jack to place the fake token to set up the night.
     *
     * @return the locations of fake token.
     */
    protected final Set<String> fakeTokenGenerator() {
        Set<String> fakeToken = new HashSet<String>();
        int numberOfFakeToken = womenToken[1];
        for (int i = 0; i < numberOfFakeToken; i++) {
            System.out.println("Jack is choosing fake token: ");
            String choseToken = ConsoleUtility.selectLocationfromSet(crimeSceneSet);
            fakeToken.add(choseToken);
            crimeSceneSet.remove(choseToken);
        }
        return fakeToken;
    }

    /**
     * Place the detectives in different location at the beginning of the night.
     *
     * @return detectives.
     */
    protected final Detectives detectivesGenerator() {
        Detectives dt = new Detectives();
        Detective[] dts = dt.getDetectives();
        for (int i = 0; i < dts.length; i++) {
            System.out.println(dts[i].getColor() + " detective:");
            String choseLocation = ConsoleUtility.selectLocationfromSet(detectivesStartingLocation);
            dts[i].setLocation(choseLocation);
            realPatrolToken.add(choseLocation);
            detectivesStartingLocation.remove(choseLocation);
        }
        return dt;
    }

    /**
     * Generate the crime scene for each night. From the locations of all the
     * tokens and detectives, ask Jack to wait or kill. If wait, let the
     * detectives move all the marked token.
     *
     * @return the crime scene of the night.
     */
    protected final String crimeSceneGenerator() {
        String crimeScence = "";
        boolean kill = false;
        List<String> revealedPatrolToken = new ArrayList<String>();
        int choice = 0;
        while (!kill) {
            if (currentTurn > -5) {
                System.out.println("Jack, do you want to kill or wait? ");
                System.out.println("\t 1- Kill");
                System.out.println("\t 2- Wait");
                choice = ConsoleUtility.getIndexSelection(1, 2);
            } else {
                System.out.println("Jack has waited for 5 turns." + " He has to kill the victim. ");
            }
            // if Jack Select to kill.
            if (choice == 1 || currentTurn == -5) {
                kill = true;
                crimeScence = jackKill();
            } else { // if Jack Select to wait
                currentTurn--;
                jackWait(markedToken);
                // Jack can reveal 1 patrol token.
                jackRevealToken(revealedPatrolToken, realPatrolToken, fakePatrolToken);
            }
        }
        return crimeScence;
    }

    /**
     * get the Connected circle from the root location.
     *
     * @param root
     *            current circle.
     * @return the set of adjacent circles from the location.
     */
    public final Set<String> getConnectedCircle(final String root) {
        Set<String> circleSet = new HashSet<String>();
        Set<Edge> edgeSet = gb.getCircleGraph().edgesOf(root);
        for (Edge s : edgeSet) {
            circleSet.add(s.getConnectedVertex(root));
        }
        return circleSet;
    }

    @Override
    protected final List<String> fakePatrolTokenGenerator() {
        List<String> fakePatrol = new ArrayList<String>();
        for (int i = 0; i < 2; i++) {
            System.out.println("Fake Patrol Token:  ");
            String choseLocation = ConsoleUtility.selectLocationfromSet(detectivesStartingLocation);
            fakePatrol.add(choseLocation);
            detectivesStartingLocation.remove(choseLocation);
        }
        return fakePatrol;
    }

    /**
     * Ask jack to Select the crime scene from the marked token.
     * 
     * @return chosen Crime Scene.
     */
    private String jackKill() {
        System.out.println("Jack's turn: ");
        System.out.println("Jack is choosing Crime Scene:");
        String crimeScene = ConsoleUtility.selectLocationfromList(markedToken);
        indexOfCrimeScene = markedToken.indexOf(crimeScene);
        startingCrimeSceneLocation = startingMarkedToken.get(indexOfCrimeScene);
        markedToken.remove(crimeScene);
        return crimeScene;
    }

    /**
     * Move the marked locations one circle when jack Selects to wait. Let Jack
     * reveals one police token.
     * 
     * @param markedToken
     *            the locations of marked Token Jack chosen.
     */
    private void jackWait(final List<String> markedToken) {
        for (int i = 0; i < markedToken.size(); i++) {
            String token = markedToken.get(i);
            System.out.println("Detectives turn: ");
            System.out.println("Select one in the locations below to move the " + markedToken.get(i) + " token: ");
            Set<String> adjacentCircles = getConnectedCircle(token);
            String selectedLocation = ConsoleUtility.selectLocationfromSet(adjacentCircles);
            markedToken.set(i, selectedLocation);
        }
    }

    /**
     * Reveal the patrol token in jack's waiting turn.
     * 
     * @param revealedPatrolToken
     *            Token has been revealed.
     * @param realPatrolToken
     *            real police patrol token.
     * @param fakePatrolToken
     *            fake police patrol token.
     */
    private void jackRevealToken(final List<String> revealedPatrolToken, final List<String> realPatrolToken,
            final List<String> fakePatrolToken) {
        System.out.println("Jack reveals one patrol token: ");
        System.out.println("Select one of the police tokens below to reveal");
        List<String> allToken = new ArrayList<String>();
        allToken.addAll(realPatrolToken);
        allToken.addAll(fakePatrolToken);
        allToken.removeAll(revealedPatrolToken);
        // shuffle to ensure it is fair
        Collections.shuffle(allToken);
        for (String s : allToken) {
            System.out.print(s + "  ");
        }
        String input = ConsoleUtility.selectLocationfromList(allToken);

        if (fakePatrolToken.contains(input)) {
            System.out.println(input + " is a fake Token!");
            System.out.println("Remove " + input + " from the board.");
            fakePatrolToken.remove(input);
        } else {
            int index = realPatrolToken.indexOf(input);
            System.out.println(input + " is a " + detectives.getDetectives()[index].getColor() + " detective token");
            revealedPatrolToken.add(input);
        }
    }

    /**
     * Get the fake crime scene from Jack's decision.
     * 
     * @return the fake crime scene.
     */
    private String fakeCrimeSceneGenerator() {
        System.out.println("Select one of the locations" + " below to be the fake Crime Scene:");
        String fakeCrimeScene = ConsoleUtility.selectLocationfromList(markedToken);
        markedToken.remove(fakeCrimeScene);
        return crimeScene;
    }
}
