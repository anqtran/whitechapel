package com.wingulabs.whitechapel.detectivesMoveEngine.test;

import com.wingulabs.whitechapel.detectivesMoveEngine.MoveTree.Vertex;
import com.wingulabs.whitechapel.gameBoard.GameBoard;
import com.wingulabs.whitechapel.detectivesMoveEngine.MyMoveTree;

public class MyMoveTreeTester {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        final String rootLabel = "C1";
        MyMoveTree testTree = new MyMoveTree(GameBoard.SINGLETON, rootLabel);
        MyMoveTree expectedTree = new MyMoveTree(GameBoard.SINGLETON, rootLabel);

        final Vertex v1C2 = new Vertex("C2");
        final Vertex v1C3 = new Vertex("C3");

        testTree.addVertex(v1C2);
        testTree.addEdge(testTree.getRoot(), v1C2);
        testTree.addVertex(v1C3);
        testTree.addEdge(testTree.getRoot(), v1C3);

        expectedTree.addVertex(v1C2);
        expectedTree.addEdge(expectedTree.getRoot(), v1C2);

        testTree.removeVertexYes("C2");

        for (Vertex v : testTree.vertexSet()) {
            System.out.println(v.getLabel());
        }
        for (Vertex v : expectedTree.vertexSet()) {
            System.out.println("Compare" + v.getLabel());
        }
        assert testTree.equals(expectedTree);
    }

}
