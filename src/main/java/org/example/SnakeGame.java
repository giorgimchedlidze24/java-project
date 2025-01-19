package org.example;
import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
public class SnakeGame {
    public static void main(String[]args){
        new GameFrame();
    }
}

class GameFrame extends JFrame {
    GameFrame(){
        GamePanel panel=new GamePanel();
        this.add(panel);
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}




class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH=600;
    static final int SCREEN_HEIGHT=600;
    static final int UNIT_SIZE=25;
    static final int GAME_UNITS=(SCREEN_WIDTH* SCREEN_HEIGHT)/UNIT_SIZE ;
    static final int DELAY=150;
    final int X[]=new int [GAME_UNITS];
    final int y[]=new int [GAME_UNITS];
    int bodyParts=6;
    int applesEaten;
    int applex;
    int appley;
    char dirrection='R';
    boolean running=false;
    Timer timer;
    Random random;
    GamePanel(){
        random=new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.blue);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }

    public void startGame(){
        newApple();
        running = true;
        timer= new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g){

        if(running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            g.setColor(Color.red);
            g.fillOval(applex, appley, UNIT_SIZE, UNIT_SIZE);
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(X[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(X[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD,100));
            FontMetrics matrics = getFontMetrics(g.getFont());
            g.drawString("score: "+applesEaten,(SCREEN_WIDTH - matrics.stringWidth("score: "+applesEaten))/2,g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }
    public void newApple(){
        applex=random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appley=random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

    }
    public void move(){
        for(int i= bodyParts;i>0;i--){
            X[i]=X[i-1];
            y[i]=y[i-1];
        }
        switch (dirrection){
            case'U':
                y[0]=y[0]-UNIT_SIZE;
                break;
            case'D':
                y[0]=y[0]+UNIT_SIZE;
                break;
            case'L':
                X[0]=X[0]-UNIT_SIZE;
                break;
            case'R':
                X[0]=X[0]+UNIT_SIZE;
                break;
        }
    }
    public void checkApple(){
        if((X[0]==applex) && (y[0]==appley)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions() {
        //if head collide body
        for (int i = bodyParts; i > 0; i--) {
            if ((X[0] == X[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        //if head collide with left boarder
        if (X[0] < 0) {
            running = false;
        }
        //head touch right
        if (X[0] > SCREEN_WIDTH) {
            running = false;
        }
        //head touch top
        if (y[0] < 0) {
            running = false;
        }
        //head touch bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if(!running){
            timer.stop();
            ///// running=false;
        }
    }
    public void gameOver(Graphics g){
        //
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,40));
        FontMetrics matrics1 = getFontMetrics(g.getFont());
        g.drawString("score: "+applesEaten,(SCREEN_WIDTH - matrics1.stringWidth("score: "+applesEaten))/2,g.getFont().getSize());
        //game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics matrics2 = getFontMetrics(g.getFont());
        g.drawString("game over",(SCREEN_WIDTH - matrics2.stringWidth("game over"))/2,SCREEN_HEIGHT/2);
    }
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e)
        {
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(dirrection !='R'){
                        dirrection='L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if(dirrection !='L'){
                        dirrection='R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(dirrection !='D'){
                        dirrection='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(dirrection !='U'){
                        dirrection='D';
                    }
                    break;
            }

        }
    }
}