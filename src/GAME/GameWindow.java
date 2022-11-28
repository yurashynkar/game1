package GAME;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame{
    private static GameWindow game_window;
    private static Image back_ground;
    private static Image drop;
    private static Image game_over;
    private static Image restart;
    private static long last_frame_time;
    private static float drop_y = -150;
    private static float drop_x;
    private static float drop_v = 220;
    private static int score = 0;
    private static float restart_x = 40;
    private static float restart_y = 60;

    public static void main(String[] args) throws IOException {
        back_ground = ImageIO.read(GameWindow.class.getResourceAsStream("bg.jpeg"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png"));
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.jpeg"));
        restart = ImageIO.read(GameWindow.class.getResourceAsStream("res.png"));
        game_window = new GameWindow();
        JLabel record = new JLabel("Score: " + score);

        record.setSize(220, 150);
        record.setPreferredSize(new Dimension(100, 25));
        record.setFont(new Font("Score: " + score, Font.PLAIN, 19));
        record.setOpaque(true);
        record.setBackground(Color.white);

        game_window.setSize(900, 525);
        game_window.setResizable(false);
        last_frame_time = System.nanoTime();
        game_window.setTitle("game");
        game_window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        game_window.setLocation(200, 150);
        GameField game_field = new GameField();
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float drop_x_right = drop_x + drop.getWidth(null);
                float drop_t_botton = drop_y + drop.getWidth(null);
                boolean is_drop = x >= drop_x && x <= drop_x_right && y >= drop_y && y<=drop_t_botton;
                if (is_drop) {
                    drop_y = -100;
                    drop_x = (int) (Math.random() * (game_field.getWidth() - drop.getHeight(null)));
                    record.setText("Score: " + score);
                    score++;
                    drop_v = drop_v + 20;

                }
                else  if (is_drop == false) {
                    if (score<=0){
                        score--;
                    }
                }
                float restart_x_left = restart_x + restart.getWidth(null);
                float restart_y_bottom = restart_y + restart.getHeight(null);
                boolean if_restart = x >= restart_x && x <= restart_x_left && y >= restart_y && y <=restart_y_bottom;
                if (if_restart){
                    drop_y = -100;
                    drop_x = (int)(Math.random() * (game_field.getWidth() - drop.getHeight(null)));
                    score = 0; drop_v = 200;
                    game_window.setTitle("Score: " +score);
                }
            }
        });
        game_window.add(game_field);
        game_field.add(record);
        game_window.setVisible(true);
    }

    public static void onRepaint (Graphics g){
        g.drawImage(back_ground, 0, 0, null);
        long corrent_time = System.nanoTime();
        float delta_time = (corrent_time - last_frame_time) * 0.000000001f;
        last_frame_time = corrent_time;
        drop_y = drop_y + drop_v * delta_time;
        g.drawImage(drop,(int) drop_x,(int) drop_y, null);
        if (drop_y > game_window.getHeight()){
            g.drawImage(game_over, 400, 200, null);
            g.drawImage(restart, (int) restart_x, (int) restart_y, null);
        }
    }

    private static class GameField extends JPanel{
        @Override
        protected void paintComponent (Graphics g){
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}
