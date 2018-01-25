package com.wingulabs.whitechapel.nightController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.wingulabs.whitechapel.Utility.ConsoleUtility;
import com.wingulabs.whitechapel.Utility.GraphUtility;
import com.wingulabs.whitechapel.detectives.Detective;
import com.wingulabs.whitechapel.detectives.Detectives;
import com.wingulabs.whitechapel.gameBoard.GameBoard;
import com.wingulabs.whitechapel.jack.Jack;
/**
 * Control the detective moves through console.
 * @author ant
 *
 */
public class DetectiveConsoleController extends AbstractDetectiveController {

    /**
     * an array contains whether a detective at an index uses rush or not.
     */
    private boolean[] rushUsed;

    private Detectives detectives;

    public DetectiveConsoleController(final GameBoard gb, final Detectives detectives) {
        super(gb);
        this.detectives = detectives;
    }

    private String selectDetectivesDestination(final Set<String> possibleLocation, final int detectiveIndex,
            final List<String> finalDestination) {
        Set<String> noDuplicateLocation = new HashSet<String>(possibleLocation);
        noDuplicateLocation.removeAll(getOtherDetectiveLocation(detectiveIndex));
        String updatedLocation = ConsoleUtility.selectLocationfromSet(noDuplicateLocation);
        return updatedLocation;
    }

    @Override
    protected final List<String> detectivesMove() {

        rushUsed = new boolean[detectives.getDetectives().length];
        List<String> finalDestination = new ArrayList<String>();
        System.out.println("Detectives' turn: ");
        Detective[] dts = detectives.getDetectives();
        // for each detective
        for (int i = 0; i < dts.length; i++) {
            String detectiveLocation = dts[i].getLocation();
            // Set contains option that detective can move.
            Set<String> moveChoices = new HashSet<String>();
            Detective currentDetective = dts[i];
            
            System.out.println(currentDetective.getColor() + " detective's move:");
            System.out.println("The detective current location: " + currentDetective.getLocation());
            System.out.println("Select one from the options below: ");
            System.out.println("\t 1- Stay or move 1 or 2 squares. ");
            System.out.println("\t 2- Rush (Move 3 squares without investigation). ");
            
            int choice = ConsoleUtility.getIndexSelection(1, 2);
            Set<String> adjacentLocation = GraphUtility.getAdjacentSquare(detectiveLocation, gb);
            moveChoices.addAll(adjacentLocation);
            
            // Makes 2 squares move.
            for (String location : adjacentLocation) {
                moveChoices.addAll(GraphUtility.getAdjacentSquare(location, gb));
            }
            // Rush
            if (choice == 2) {
                rushUsed[i] = true;
                // get first level
                Set<String> firstSquareSet = GraphUtility.getAdjacentSquare(detectiveLocation, gb);
                Set<String> vertexInThreeMoves = new HashSet<String>();
                
                for (String firstSquare : firstSquareSet) {
                    // get second level
                    Set<String> secondSquareSet = GraphUtility.getAdjacentSquare(firstSquare, gb);
                    // add all the first and second vertex to the set.
                    vertexInThreeMoves.addAll(secondSquareSet);
                    
                    for (String secondSquare : secondSquareSet) {
                        Set<String> thirdSquareSet = GraphUtility.getAdjacentSquare(secondSquare, gb);
                        // add all the second and third vertex.
                        vertexInThreeMoves.addAll(thirdSquareSet);
                    }
                }
                // keep the third level and remove all other vertex from the
                // set.
                // this is rush option of jack.
                vertexInThreeMoves.removeAll(moveChoices);
                moveChoices = vertexInThreeMoves;
            }

            String updatedLocation = selectDetectivesDestination(moveChoices, i, finalDestination);
            // update the past move of the detective.
            // set the detective location.
            finalDestination.add(updatedLocation);
            System.out.println();
        }
        return finalDestination;
    }

    @Override
    final boolean investigation(final Jack jack) {

        boolean caughtJack = false;
        System.out.println("Investigation Starting......");
        Detective[] detectivesList = detectives.getDetectives();
        // get each detectives.
        for (int i = 0; i < detectivesList.length; i++) {
            Detective d = detectivesList[i];
            System.out.println(d.getColor() + " detective's turn:");
            // if the detective does not use rush, start investigate.
            if (!rushUsed[i]) {
                Set<String> adjacentCircles = GraphUtility.getAdjacentCircle(d.getLocation(), gb);
                
                System.out.println("Detective, do you want to search clues or arrest? ");
                System.out.println("\t 1- Search for clues");
                System.out.println("\t 2- Attempt to Arrest");
                
                int choice = ConsoleUtility.getIndexSelection(1, 2);
                if (choice == 1) {
                    List<String> searchCluesOrder = ConsoleUtility.getSearchCluesOrder(adjacentCircles);
                    boolean jackAnswerYes = false;
                    for (String circle : searchCluesOrder) {
                        if (jackAnswerYes) {
                            break;
                        }
                        if (jack.getPastMove().contains(circle)) {
                            jackAnswerYes = true;
                            
                            System.out.println("Jack has been to " + circle);
                        } else {
                            System.out.println("Jack has not been to " + circle);
                        }
                    }
                } else {
                    System.out.println("Attempt arrest: ");
                    String location = ConsoleUtility.selectLocationfromSet(adjacentCircles);
                    if (location.equals(jack.getCurrentLocation())) {
                        caughtJack = true;
                        System.out.println("You caught Jack! Mission accomplished. ");
                        return true;
                    } else {
                        System.out.println("Jack is not here! ");
                    }
                }
            } else {
                System.out.println("You cannot investigate since you have used rush.");
            }
        }
        return caughtJack;
    }

    private Set<String> getOtherDetectiveLocation(int detectiveIndex) {
        Set<String> otherDetectiveLocation = new HashSet<String>();
        Detective[] detectivesArray = detectives.getDetectives();
        for (int i = 0; i < detectivesArray.length; i++) {
            if (i == detectiveIndex) {
                continue;
            }
            otherDetectiveLocation.add(detectivesArray[i].getLocation());
        }
        return otherDetectiveLocation;
    }

}
