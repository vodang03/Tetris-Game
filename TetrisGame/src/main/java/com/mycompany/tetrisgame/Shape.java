/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetrisgame;

import static com.mycompany.tetrisgame.Board.BLOCK_SITE;
import static com.mycompany.tetrisgame.Board.BOARD_HEIGHT;
import static com.mycompany.tetrisgame.Board.BOARD_WIDTH;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author B2110075-Võ Đặng-CT276_02
 */
public class Shape {
    private int x=4, y=0;
    private int normal = 600;
    private int fast = 50;
    private int delayTimeForMoverment = normal;
    private int beginTime;
    
    private int deltaX=0;
    private boolean collision = false;
    
    private int[][] coords;
    private Board board;
    private Color color;
    
    private int score = 0;
    
    public Shape(int[][] coords, Board board, Color color){
        this.coords = coords;
        this.board = board; 
        this.color = color;
        this.delayTimeForMoverment = normal;
    }
    
    public void SetX(int x){
        this.x = x;
    }
    public void SetY(int y){
        this.y = y;
    }
    
    public void reset(){
        this.x=4;
        this.y=0;
        collision = false;
    }
    
    public void update(){
        if(collision){
            placeOnBoard();//Cố định khối hình tại vị trí cuối cùng trên bảng
            int linesCleared = clearLine();
            if (linesCleared > 0) {
                board.addScore(linesCleared);
            }
            board.setCurrentShape(); // Đặt hình dạng mới lên board
            return;
        }
        moveHorizontally();
        moveVertically();
    }
    
    //Trả về số dòng bị phá.
    private int clearLine(){
        int linesCleared = 0;
        int bottomLine = board.getBoard().length - 1;
        for(int topLine = board.getBoard().length - 1; topLine > 0; topLine--){
            int count=0;
            for(int col=0; col<board.getBoard()[0].length; col++){
                if(board.getBoard()[topLine][col] != null){
                    count++;
                }
                board.getBoard()[bottomLine][col] = board.getBoard()[topLine][col];
            }
            if(count < board.getBoard()[0].length){
                bottomLine--;
            } else {
                linesCleared++;
            }
        }
        return linesCleared;
    }
    
    public void render(Graphics g){
        //Vẽ các khối hình
        for(int i=0; i<coords.length; i++){
            for(int j=0; j<coords[0].length; j++){
                if (coords[i][j] != 0){
                    g.setColor(color);
                    g.fillRect(j*BLOCK_SITE+x*BLOCK_SITE, i*BLOCK_SITE+y*BLOCK_SITE, BLOCK_SITE, BLOCK_SITE);
                }
            }
        }
    }
    
    // Nếu có va chạm, đặt hình dạng lên board và kiểm tra dòng đầy
    public void placeOnBoard(){
        for(int row=0; row < coords.length; row++){
            for(int col=0; col<coords[0].length; col++){
                if(coords[row][col] != 0){
                    board.getBoard()[y + row][x + col] = color;
                }
            }
        }
    }
    
    public void moveHorizontally(){
        boolean moveX=true;
        if(!(x+deltaX+coords[0].length>BOARD_WIDTH) && !(x+deltaX<0)){
            for(int row=0; row < coords.length; row++){
                for(int col=0; col < coords[row].length; col++){
                    if(coords[row][col] != 0){
                        if(board.getBoard()[y+row][x+deltaX+col] != null){
                            moveX = false;                        
                        }
                    }
                }
            }
            if(moveX){
                x+=deltaX;
            }
        }
        deltaX=0;
    }
    
    public void moveVertically(){
        if((int)System.currentTimeMillis() - beginTime > delayTimeForMoverment){
            if(!(y+1+coords.length > BOARD_HEIGHT)){
                for(int row=0; row < coords.length; row++){
                    for(int col=0; col < coords[row].length; col++){
                        if(coords[row][col] != 0){
                            if(board.getBoard()[y+1+row][x+deltaX+col] != null){
                                collision = true;
                            }
                        }
                    }
                }
                if(!collision){
                    y++;
                }
            }else{
                collision = true;
            }
            beginTime = (int)System.currentTimeMillis();
        }
    }
    
    public int[][] getCoords(){
        return coords;
    }
    public void Speedup(){
        delayTimeForMoverment = fast;
        // Tăng tốc độ di chuyển của khối
    }
    public void Speeddown(){
        delayTimeForMoverment = normal;
        // Đặt lại tốc độ di chuyển về bình thường
    }
    public void MoveR(){
        deltaX=1;
        // Di chuyển hình dạng sang phải
    }
    public void MoveL(){
        deltaX=-1;
        // Di chuyển hình dạng sang trái
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    
    public Color getColor(){
        return color;
    }
    
    //Xoay Các khối hình 90 độ
    public void rotateShape() {
        int[][] newCoords = new int[coords[0].length][coords.length];
        for (int i = 0; i < coords.length; i++) {
            for (int j = 0; j < coords[i].length; j++) {
                newCoords[j][coords.length - 1 - i] = coords[i][j];
            }
        }
        // Tính toán kích thước và vị trí mới của hình dạng sau khi quay
        int newWidth = newCoords[0].length;
        int newHeight = newCoords.length;
        int newX = x;
        int newY = y;
        
        // Kiểm tra xem hình dạng sau khi quay có vượt ra khỏi biên không gian chơi game không
        if (newX + newWidth > Board.BOARD_WIDTH) {
            newX = Board.BOARD_WIDTH - newWidth; // Điều chỉnh vị trí nếu vượt ra khỏi biên bên phải
        }
        if (newX < 0) {
            newX = 0; // Điều chỉnh vị trí nếu vượt ra khỏi biên bên trái
        }
        if (newY + newHeight > Board.BOARD_HEIGHT) {
            newY = Board.BOARD_HEIGHT - newHeight; // Điều chỉnh vị trí nếu vượt ra khỏi biên dưới cùng
        }
        
        //Kiểm tra xung đột
        for (int row = 0; row < newHeight; row++) {
            for (int col = 0; col < newWidth; col++) {
                if (newCoords[row][col] != 0) {
                    int absX = newX + col;
                    int absY = newY + row;
                    if (absX < 0 || absX >= Board.BOARD_WIDTH || absY >= Board.BOARD_HEIGHT || ((absY >= 0) && (board.getBoard()[absY][absX] != null))) {
                        // Điều chỉnh vị trí nếu xảy ra xung đột
                        x = Math.min(Math.max(0, x), Board.BOARD_WIDTH - newWidth);
                        y = Math.min(y, Board.BOARD_HEIGHT - newHeight);
                        return;
                    }
                }
            }
        }
        // Cập nhật hình dạng chỉ khi nó không vượt ra khỏi biên
        if (newX >= 0 && newX + newWidth <= Board.BOARD_WIDTH && newY >= 0 && newY + newHeight <= Board.BOARD_HEIGHT) {
            coords = newCoords;
            x = newX;
            y = newY;
        }
    }
}
