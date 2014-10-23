package com.mappybot.mappybot;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Graphics {
    private final Map<String, BufferedImage> images = new HashMap<>();
    private final BufferStrategy buffer;

    public Graphics() {
        buffer = initGraphics();
    }

    private static BufferStrategy initGraphics() {
        JFrame container = new JFrame("Mappybot simulator");
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(858, 596));
        panel.setLayout(null);

        Canvas canvas = new Canvas();
        canvas.setBounds(0, 0, 858, 596);
        panel.add(canvas);
        canvas.setIgnoreRepaint(true);

        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        container.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        canvas.requestFocus();
        canvas.createBufferStrategy(2);
        return canvas.getBufferStrategy();
    }

    public void redraw(Board board) throws IOException {
        Graphics2D g = (Graphics2D) buffer.getDrawGraphics();
        drawImage(g, "/board.png", 0, 0, 0);
        for (Robot robot : board.getRobots()) {
            drawImage(g, "/robot.png", 90 + robot.getX(), 50 + robot.getY(), robot.getDirection());
        }
        g.dispose();
        buffer.show();
    }

    private void drawImage(Graphics2D g, String imageUrl, int x, int y, double angle) throws IOException {
        URL url = Simulator.class.getResource(imageUrl);
        BufferedImage sourceImage;
        if (images.get(imageUrl) == null) {
            sourceImage = ImageIO.read(url);
            images.put(imageUrl, sourceImage);
        }
        else {
            sourceImage = images.get(imageUrl);
        }
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.TRANSLUCENT);
        image.getGraphics().drawImage(sourceImage, 0, 0, null);
        // g.drawImage(image, x, y, sourceImage.getWidth(), sourceImage.getHeight(), null);
        AffineTransform at = new AffineTransform();
        at.translate(x + sourceImage.getWidth() / 2, y + sourceImage.getHeight() / 2);
        at.rotate(angle);
        at.translate(-sourceImage.getWidth() / 2, -sourceImage.getHeight() / 2);
        g.drawImage(sourceImage, at, null);
    }
}
