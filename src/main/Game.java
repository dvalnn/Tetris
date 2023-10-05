package main;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;

    private final int FPS_SET = 60;
    private final int UPS_SET = 100;

    public Game() {
        System.out.println("Hello Game!");
        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);

        // requests focus so that the GamePanel can receive keyboard inputs
        gamePanel.requestFocus();

        startGameLoop();
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        gamePanel.updatePanel();
    }

    @Override
    public void run() {

        // time per frame and time per update in nanoseconds
        double timePerFrame = 1000000000 / FPS_SET;
        double timePerUpdate = 1000000000 / UPS_SET;

        double deltaFrame = 0;
        double deltaUpdate = 0;

        int frames = 0;
        int updates = 0;

        long previousTime = System.nanoTime();

        long lastCheck = System.currentTimeMillis();

        while (true) {
            long currentTime = System.nanoTime();

            deltaFrame += (currentTime - previousTime) / timePerFrame;
            deltaUpdate += (currentTime - previousTime) / timePerUpdate;
            previousTime = currentTime;

            if (deltaUpdate >= 1) {
                update();
                updates++;
                deltaUpdate--;
            }

            if (deltaFrame >= 1) {
                gamePanel.repaint();
                frames++;
                deltaFrame--;
            }

            // prints the FPS and UPS every second
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS " + frames + " | UPS " + updates);
                frames = 0;
                updates = 0;
            }

        }
    }
}