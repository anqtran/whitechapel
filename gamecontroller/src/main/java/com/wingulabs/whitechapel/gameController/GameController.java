package com.wingulabs.whitechapel.gameController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.wingulabs.whitechapel.Utility.ConsoleUtility;
import com.wingulabs.whitechapel.detectives.Detective;
import com.wingulabs.whitechapel.detectives.Detectives;
import com.wingulabs.whitechapel.detectives.Detective.DetectiveColor;
import com.wingulabs.whitechapel.gameBoard.GameBoard;
import com.wingulabs.whitechapel.jack.Jack;
import com.wingulabs.whitechapel.night.Night;
import com.wingulabs.whitechapel.night.NightConsole;
import com.wingulabs.whitechapel.nightController.NightController;

/**
 * Control the flow of the game.
 * 
 * @author anqtr
 *
 */
public class GameController {
    /**
     * List of detective color to determine the head of investigation.
     */
    private List<DetectiveColor> investigationPile;
    /**
     * Jack's hideout through entire game.
     */
    private String hideOut;
    /**
     * gameboard of WhiteChapel.
     */
    protected GameBoard gb = GameBoard.SINGLETON;
    /**
     * Constructor generates the list of detectives' color and Jack's hide out.
     */
    private NightController nightController = null;

    public GameController() {
        investigationPile = new LinkedList<DetectiveColor>() {
            {
                add(DetectiveColor.BLUE);
                add(DetectiveColor.RED);
                add(DetectiveColor.GREEN);
                add(DetectiveColor.BROWN);
                add(DetectiveColor.YELLOW);
            }
        };
        // this.hideOut = hideOutGenerator();
    }

    /**
     * Run and control the game flow.
     */
    public final void run() {
        Set<String> previousCrimeScene = new HashSet<String>();
        Set<String> lastDetectivesLocation = new HashSet<String>(gb.YELLOW_SQUARES);
        boolean gameContinue = true;
        int night = 0;
        while (gameContinue && night < 5) {
            night++;
            generateHeadofInvestigation();
            gameContinue = runNight(night, lastDetectivesLocation, previousCrimeScene);
            Detectives detectives = nightController.getDetectives();
            lastDetectivesLocation = lastDetectivesLocation(detectives);
            previousCrimeScene.add(nightController.getNight().getStartingCrimeSceneLocation());
        }
        if (night == 5) {
            System.out.println("Congratulations Jack! You won.");
        } else {
            System.out.println("Congratulations Detectives!");
        }
    }

    public final void runSingleNight() {
        Detectives dt = new Detectives();
        List<String> detectivesLocations = new ArrayList<String>(gb.YELLOW_SQUARES);
        detectivesLocations.remove("SC101S1");
        detectivesLocations.remove("SC130S1");

        Detective[] dts = dt.getDetectives();
        for (int i = 0; i < dts.length; i++) {
            String location = detectivesLocations.get(i);
            System.out.println(dts[i].getColor() + " detective is at: " + location);
            dts[i].setLocation(location);
        }
        Jack jack = new Jack(new int[] {0, 0 }, "C65");
        nightController = new NightController(jack, dt, "C66");
        boolean jackReachesHideOut = nightController.simpleRun();
    }

    /**
     * Run the night of the game.
     * 
     * @param lastLocation
     *            the set of last locations of detectives in the previous game
     * @param previousCrimeScene
     *            set of crime scene that Jack has chosen.
     * @return true if jack reaches his hide out.
     */
    private boolean runNight(int night, final Set<String> lastLocation, final Set<String> previousCrimeScene) {
        Set<String> possibleCrimeScene = gb.RED_CIRCLES;
        possibleCrimeScene.removeAll(previousCrimeScene);

        Night currentNight = new NightConsole(night, lastLocation, possibleCrimeScene);
        Detectives detectives = currentNight.getDetectives();
        Jack jack = new Jack(currentNight.getCoachAlley(), currentNight.getCrimeScene());
        nightController = new NightController(currentNight, jack, detectives, hideOut);
        boolean jackReachesHideOut = nightController.run();
        return jackReachesHideOut;
    }

    /**
     * Generate randomly the head of investigation in each night. Head of
     * investigation can place the police tokens in the beginning.
     */
    private void generateHeadofInvestigation() {
        Random rn = new Random();
        int i = rn.nextInt(investigationPile.size());
        DetectiveColor dColor = investigationPile.get(i);
        investigationPile.remove(i);
        System.out.println(dColor + " Detective is head of" + " investigation in this night!");
    }

    /**
     * Ask user to choose hideout from the entire game.
     * 
     * @return Jack's hideout.
     */
    protected String hideOutGenerator() {
        System.out.println("Jack is choosing his Hide Out: ");
        System.out.println("Select any circle except the red circles: ");
        Set<String> circlesExceptRed = new HashSet<String>(this.getGb().getCircleGraph().vertexSet());
        circlesExceptRed.removeAll(this.getGb().RED_CIRCLES);
        String hideOut = ConsoleUtility.selectLocationfromSet(circlesExceptRed);
        return hideOut;
    }

    /**
     * Get the detectives' locations.
     * 
     * @param detectives
     *            detectives
     * @return set of locations of the detectives.
     */
    private Set<String> lastDetectivesLocation(final Detectives detectives) {
        Set<String> locations = new HashSet<String>();
        for (Detective detective : detectives.getDetectives()) {
            locations.add(detective.getLocation());
        }
        return locations;
    }

    public GameBoard getGb() {
        return gb;
    }

}
