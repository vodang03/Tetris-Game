/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetrisgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;


/**
 *
 * @author B2110075-Võ Đặng-CT276_02
 */
public class Board extends JPanel implements KeyListener, MouseListener, MouseMotionListener{    
    public static int currentScore;
    
    //Biến sẽ lưu hình ảnh của nút Pause
    private final BufferedImage pause;
    
    //Biến sẽ lưu khối hình sẽ xuất hiện tiếp theo
    private Shape nextShape; 
    
    //Lưu trạng thái của Game
    public static int STATE_GAME_PLAY = 0;
    public static int STATE_GAME_PAUSE = 1;
    public static int STATE_GAME_OVER = 2;
    
    public static int state = STATE_GAME_PLAY;
    private int score = 0;
    
    public static final int BOARD_WIDTH = 10; //Chiều dài bảng
    public static final int BOARD_HEIGHT = 20; //Chiều cao bảng
    public static final int BLOCK_SITE = 30; //Độ rộng của ô
    private Timer looper;
    private Color[][] board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
    
    private Random random;
    
    private Color[] colors = {Color.decode("#DC3333"), Color.decode("#ff7f27"), Color.decode("#fff200"),
         Color.decode("#4AF041"), Color.decode("#58C7F7"), Color.decode("#a349a4"), Color.decode("#2731C9")
    };
    
    private Shape[] shapes = new Shape[7];
    private Shape currentShape;
    
    public Board(){     
        addMouseListener(this);
        addMouseMotionListener(this);
        
        //Chèn hình ảnh của nút Pause
        pause = ImageLoader.loadImage("/pause.png");
        
        random = new Random();
        
        shapes[0] = new Shape(new int[][]{
            {1,1,1,1}
        },this, colors[4]);// Chữ I
        
        shapes[1] = new Shape(new int[][]{
            {1, 1, 1},
            {0, 1, 0},
        },this, colors[5]);// Chữ T
        
        shapes[2] = new Shape(new int[][]{
            {1, 1, 1},
            {1, 0, 0},
        },this, colors[1]);// Chữ L
        
        shapes[3] = new Shape(new int[][]{
            {1, 1, 1},
            {0, 0, 1},
        },this, colors[6]);// Chữ J
        
        shapes[4] = new Shape(new int[][]{
            {1, 1},
            {1, 1},
        },this, colors[2]);// Hình Vuông
        
        shapes[5] = new Shape(new int[][]{
            {0, 1, 1},
            {1, 1, 0},
        },this, colors[3]);// Chữ z ngược
        
        shapes[6] = new Shape(new int[][]{
            {1, 1, 0},
            {0, 1, 1},
        },this, colors[0]);// Chữ z thuận
        
        //Chọn random khối xuất hiện đầu tiên
        currentShape = shapes[random.nextInt(shapes.length)];
        currentShape.reset();
        
        // Tạo hình dạng khối tiếp theo
        nextShape = shapes[random.nextInt(shapes.length)];
        nextShape.reset();
        
        looper = new Timer(60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();// Vẽ lại giao diện người dùng sau khi update
            }
        });
        looper.start();
    }
    
    private void update(){
        if(state == STATE_GAME_PLAY){
            currentShape.update(); // Phương thức update được khởi tạo bên lớp Shape
        }
    }
    
    //Tạo một khối hình mới cho lượt tiếp theo
    public void setCurrentShape(){
        currentShape = nextShape;
        nextShape = shapes[random.nextInt(shapes.length)];
        nextShape.reset();
        currentShape.reset();
        checkGameOver();
    }
    
    //Kiểm tra xem có bị Game Over chưa
    public void checkGameOver() {
        int[][] coords = currentShape.getCoords();
        for(int row=0; row < coords.length; row++){
            for (int col = 0; col < coords[0].length; col++) {
                //Kiểm tra xem khối hình hiện tại có chạm đỉnh của khu vực trò chơi chưa.
                if (board[row + currentShape.getY()][col + currentShape.getX()] != null) {
                    state = STATE_GAME_OVER;
                    openSaveForm();
                    return;
                }
            }
        }
    }
    
    // Tạo và hiển thị một cửa sổ SaveForm
    public void openSaveForm() {
        SaveForm loginForm = new SaveForm();
        loginForm.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Cho màu nền của cửa sổ màu đen
        g.setColor(Color.black);
        g.fillRect(0,0,getWidth(),getHeight());
        
        currentShape.render(g);
        
        //Vẽ nút dừng/tiếp tục trò chơi
        if (pause != null) {
            g.drawImage(pause, 350, 500, 50, 50, this);
        }
        for(int row=0; row<board.length; row++){
            for(int col=0; col<board[0].length; col++){
                if (board[row][col] != null){
                    g.setColor(board[row][col]);
                    g.fillRect(col*BLOCK_SITE, row*BLOCK_SITE, BLOCK_SITE, BLOCK_SITE);
                }
            }
        }

        //Tạo khung khu vực trò chơi
        g.setColor(Color.white);
        for(int row=0; row <= BOARD_HEIGHT; row++){
            g.drawLine(0, BLOCK_SITE * row, BLOCK_SITE * BOARD_WIDTH, BLOCK_SITE * row);
        }
        for(int col=0; col <= BOARD_WIDTH; col++){
            g.drawLine(BLOCK_SITE * col, 0, BLOCK_SITE * col, BLOCK_SITE * BOARD_HEIGHT);
        }
        
        //Hiển thị Khối hình tiếp theo sẽ xuất hiện.
        g.setColor(Color.WHITE);
        g.drawString("Next Shape:", BOARD_WIDTH * BLOCK_SITE + 20, 50);
        int[][] nextCoords = nextShape.getCoords();
        Color nextColor = nextShape.getColor();
        for (int i = 0; i < nextCoords.length; i++) {
            for (int j = 0; j < nextCoords[0].length; j++) {
                if (nextCoords[i][j] != 0) {
                    g.setColor(nextColor);
                    g.fillRect((BOARD_WIDTH + j + 1) * BLOCK_SITE, (i + 2) * BLOCK_SITE, BLOCK_SITE, BLOCK_SITE);
                }
            }
        }
        
        //Hiển thị dòng chữ Game Over
        if(state == STATE_GAME_OVER){
            String gamePausedString = "GAME OVER";
            g.setColor(Color.red);
            g.setFont(new Font("Georgia", Font.BOLD, 30));
            g.drawString(gamePausedString, 40, TetrisGame.HEIGHT / 2 - 40);
        }
        //Hiển thị dòng chữ Game Pause
        if(state == STATE_GAME_PAUSE){
            String gameOverString = "GAME PAUSED";
            g.setColor(Color.yellow);
            g.setFont(new Font("Georgia", Font.BOLD, 30));
            g.drawString(gameOverString, 35, TetrisGame.HEIGHT / 2 - 40);
        }
        //Hiển thị điểm số
        g.setColor(Color.WHITE);
        g.setFont(new Font("Georgia", Font.BOLD, 20));
        g.drawString("SCORE", TetrisGame.WIDTH - 140, TetrisGame.HEIGHT / 2);
        g.drawString(score + "", TetrisGame.WIDTH - 140, TetrisGame.HEIGHT / 2 + 30);
        
        //Hiển thị tên của người thực hiện (Võ Đặng-B2110075-CT276_02)
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Georgia", Font.BOLD, 20));
        g.drawString("Author Vo Dang-B2110075-CT276_02", 45, 650);
    }
    
    public Color[][] getBoard(){
        return board;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                currentShape.rotateShape();//Xoay thay đổi hình dáng của khối
                break;
            case KeyEvent.VK_DOWN:
                currentShape.Speedup();//Tăng tốc độ rơi của khối
                break;
            case KeyEvent.VK_RIGHT:
                currentShape.MoveR();//Di chuyển khối sang phải
                break;
            case KeyEvent.VK_LEFT:
                currentShape.MoveL();//Di chuyển khối sang trái
                break;
            default:
                break;
        }
    }
    
    //Điểm cộng khi hàng được lấp đầy
    public void addScore(int x) {
        score += x*100;
        currentScore = score;
    }
    //Phương thức để lấy số điểm hiện tại
    public static int getScore(){
        return currentScore;
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_DOWN){
            currentShape.Speeddown();
        }
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        // Kiểm tra xem click có nằm trong vùng hình ảnh pause không
        int pauseX = 350; // X-coordinate của hình pause.png
        int pauseY = 500; // Y-coordinate của hình pause.png
        int pauseWidth = 50; // Chiều rộng của hình pause.png
        int pauseHeight = 50; // Chiều cao của hình pause.png
        //Nếu con trỏ nhấn vào ảnh thì game sẽ dừng lại.
        if (mouseX >= pauseX && mouseX <= pauseX + pauseWidth &&
            mouseY >= pauseY && mouseY <= pauseY + pauseHeight) {
            if(state == STATE_GAME_PLAY){
                state = STATE_GAME_PAUSE;
            }else if(state == STATE_GAME_PAUSE){
                state = STATE_GAME_PLAY;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
