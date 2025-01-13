/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tetrisgame;

import javax.swing.JFrame;

/**
 *
 * @author B2110075-Võ Đặng-CT276_02
 */
public class TetrisGame {
    public static final int WIDTH=480, HEIGHT=720;
    
    private Board board;
    private JFrame window;
    
    public TetrisGame(){
        window = new JFrame("TetrisGame");
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        
        board = new Board();
        window.add(board);
        window.addKeyListener(board);
        window.setVisible(true);
    }
    
    public static void main(String[] args){
        new TetrisGame();
        //new LoginForm().setVisible(true);
    }
}
