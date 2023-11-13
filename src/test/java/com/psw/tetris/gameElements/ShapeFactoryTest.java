package com.psw.tetris.gameElements;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.psw.tetris.gameElements.shapeTypes.IShape;
import com.psw.tetris.gameElements.shapeTypes.JShape;
import com.psw.tetris.gameElements.shapeTypes.LShape;
import com.psw.tetris.gameElements.shapeTypes.OShape;
import com.psw.tetris.gameElements.shapeTypes.SShape;
import com.psw.tetris.gameElements.shapeTypes.TShape;
import com.psw.tetris.gameElements.shapeTypes.ZShape;

import java.awt.geom.Point2D;

// TODO test calculateMinMaxCoords() method!

/**
 * 
 * This class is used to test the creation of the tetrominoes on the board,
 * specifically the shapeFactory.First, we create a tetromino with a specific
 * shape and then we check if its points are ready to be drawn on the board.
 * This is done by comparing the points of the tetromino with a reference array
 * of points, having in consideration the center of the piece.
 */
public class ShapeFactoryTest {

        private Shape shape;

        @Test
        public void IShapeGeneratesShapeI() {
                shape = new IShape(0, new Point2D.Double(0, 0));
                Point2D[] points = shape.getPoints();
                Point2D[] referencePoints = {
                                new Point2D.Double(0, 0),
                                new Point2D.Double(1, 0),
                                new Point2D.Double(2, 0),
                                new Point2D.Double(3, 0) };

                for (int i = 0; i < points.length; i++) {
                        assertEquals(referencePoints[i], points[i]);
                }
        }

        @Test
        public void JShapeGeneratesShapeJ() {
                shape = new JShape(0, new Point2D.Double(0, 0));
                Point2D[] points = shape.getPoints();
                Point2D[] referencePoints = {
                                new Point2D.Double(0, 0),
                                new Point2D.Double(0, 1),
                                new Point2D.Double(1, 1),
                                new Point2D.Double(2, 1) };

                for (int i = 0; i < points.length; i++) {
                        assertEquals(referencePoints[i], points[i]);
                }
        }

        @Test
        public void LShapeGeneratesShapeL() {
                shape = new LShape(0, new Point2D.Double(0, 0));
                Point2D[] points = shape.getPoints();
                Point2D[] referencePoints = {
                                new Point2D.Double(0, 1),
                                new Point2D.Double(1, 1),
                                new Point2D.Double(2, 1),
                                new Point2D.Double(2, 0) };

                for (int i = 0; i < points.length; i++) {
                        assertEquals(referencePoints[i], points[i]);
                }
        }

        @Test
        public void OShapeGeneratesShapeO() {
                shape = new OShape(0, new Point2D.Double(0, 0));
                Point2D[] points = shape.getPoints();
                Point2D[] referencePoints = {
                                new Point2D.Double(0, 0),
                                new Point2D.Double(0, 1),
                                new Point2D.Double(1, 0),
                                new Point2D.Double(1, 1) };

                for (int i = 0; i < points.length; i++) {
                        assertEquals(referencePoints[i], points[i]);
                }
        }

        @Test
        public void SShapeGeneratesShapeS() {
                shape = new SShape(0, new Point2D.Double(0, 0));
                Point2D[] points = shape.getPoints();
                Point2D[] referencePoints = {
                                new Point2D.Double(0, 1),
                                new Point2D.Double(1, 1),
                                new Point2D.Double(1, 0),
                                new Point2D.Double(2, 0) };

                for (int i = 0; i < points.length; i++) {
                        assertEquals(referencePoints[i], points[i]);
                }
        }

        @Test
        public void TShapeGeneratesShapeT() {
                shape = new TShape(0, new Point2D.Double(0, 0));
                Point2D[] points = shape.getPoints();
                Point2D[] referencePoints = {
                                new Point2D.Double(0, 1),
                                new Point2D.Double(1, 1),
                                new Point2D.Double(2, 1),
                                new Point2D.Double(1, 0) };

                for (int i = 0; i < points.length; i++) {
                        assertEquals(referencePoints[i], points[i]);
                }
        }

        public void ZShapeGeneratesShapeZ() {
                shape = new ZShape(0, new Point2D.Double(0, 0));
                Point2D[] points = shape.getPoints();
                Point2D[] referencePoints = {
                                new Point2D.Double(0, 0),
                                new Point2D.Double(1, 0),
                                new Point2D.Double(1, 1),
                                new Point2D.Double(2, 1) };

                for (int i = 0; i < points.length; i++) {
                        assertEquals(referencePoints[i], points[i]);
                }
        }
        // testar UI
        // testar Gamelogic

        // -> rotação
        // -> hardrop
        // -> soft drop

        // testar a conexão com outro ordenador

}
