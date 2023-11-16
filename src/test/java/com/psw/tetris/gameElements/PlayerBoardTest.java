package com.psw.tetris.gameElements;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.psw.tetris.gameElements.boardTypes.PlayerBoard;
import com.psw.tetris.gameElements.shapeTypes.IShape;
import com.psw.tetris.gameElements.shapeTypes.JShape;
import com.psw.tetris.gameElements.shapeTypes.LShape;
import com.psw.tetris.gameElements.shapeTypes.OShape;
import com.psw.tetris.gameElements.shapeTypes.SShape;
import com.psw.tetris.gameElements.shapeTypes.TShape;
import com.psw.tetris.gameElements.shapeTypes.ZShape;

import java.awt.Color;
import java.awt.geom.Point2D;

public class PlayerBoardTest{
    private PlayerBoard board;
    private Color color;

    //TODO: trocar o que tem true para valores entre 0 a 7 -> shape factory accepted values :))
    @Test 
    public void holdTetrominoTest(){

        color = Color.RED;

        board = new PlayerBoard(0,0,0,color); // ->meter valores prov 0 

        Tetromino active = board.getTetromino(); // peça valida 
        Tetromino next = board.getNextTetromino(); // peça valida 
        Tetromino hold = board.getHoldTetromino(); // null   

        assertEquals(active, true);
        assertEquals(next, true);  
        assertEquals(hold,null);// antes de dar hold esta variavel tem que ser nula 

        board.holdTetromino();

        Tetromino newActive = board.getTetromino();
        Tetromino newNext = board.getNextTetromino();
        Tetromino newHold = board.getHoldTetromino();

        assertEquals(newActive,next);
        assertEquals(newHold,active);
        assertEquals(newNext,true);

        board.holdTetromino();

        assertEquals(newActive,active);
        assertEquals(newHold,hold);

        

        // nothing happens to Hold next since they are only swapped 
        //TODO: test if the positions of the Tetromino is correct after the change :)) 

// ------------------------------------------//--------------------------------------------------------//
    }
// ------------------------------------------//--------------------------------------------------------//









}