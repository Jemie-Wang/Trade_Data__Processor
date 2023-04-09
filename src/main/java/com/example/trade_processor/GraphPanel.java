package com.example.trade_processor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphPanel extends JPanel {
    private int width = 800;
    private int height = 400;
    private int padding = 30;
    private int labelPadding = 25;
    private int minX = -1;
    private int maxX = 0;
    private Color lineColor = new Color(44, 102, 230, 180);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 4;
    private int numberYDivisions = 10;
    private int numberXDivisions = 5;
    private List<Point> data;

    public GraphPanel() {
        this.data = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double maxY = getMaxY();
        double minY = getMinY();

        double xScale = ((double) getWidth() - 2 * padding - labelPadding) / (this.maxX - this.minX);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (maxY - minY);

        List<Point> graphPoints = new ArrayList<>();
//        for (int i = 0; i < data.size(); i++) {
//            int x1 = (int) ((data.get(i).getX() - this.minX) * xScale + padding);
//            int y1 = (int) ((maxY - data.get(i).getY()) * yScale + padding);
//            graphPoints.add(new Point(x1, y1));
//        }
        data.stream().map(point -> new Point((int) ((point.getX() - this.minX) * xScale + padding + labelPadding), (int) ((maxY - point.getY()) * yScale + padding))).forEach(graphPoints::add);


        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            if (data.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y0);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((minY + (maxY - minY) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y0);
        }

        // and for x axis
        for (int i = 0; i < numberXDivisions + 1; i++) {
            int y0 = getHeight() - padding - labelPadding;
            int y1 = y0 - pointWidth;
            int x0 = (i * (getWidth() - padding * 2 - labelPadding)) / numberXDivisions + padding + labelPadding;
            if (data.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x0, padding);
                g2.setColor(Color.BLACK);
                String xLabel = ((int) (((minX + (maxX - minX) * ((i * 1.0) / numberXDivisions)) * 100)) / 100.0) + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(xLabel);
                g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
            }
            g2.drawLine(x0, y0, x0, y1);
        }

        // create x and y axes
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
//        for (int i = 0; i < graphPoints.size(); i++) {
//            int x = graphPoints.get(i).x - pointWidth / 2;
//            int y = graphPoints.get(i).y - pointWidth / 2;
//            int ovalW = pointWidth;
//            int ovalH = pointWidth;
//            g2.fillOval(x, y, ovalW, ovalH);
//        }
        graphPoints.stream().forEach(p -> {
            int x = p.x - pointWidth / 2;
            int y = p.y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        });

    }

    private double getMinY() {
        return data.stream().mapToDouble(Point::getY).min().orElse(0);
    }

    private double getMaxY() {
        return data.stream().mapToDouble(Point::getY).max().orElse(0);
    }

    void createAndShowGui(GraphPanel mainPanel) throws IOException {
        mainPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        BufferedImage img = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        frame.printAll(g2d);
        frame.dispose();
        g2d.dispose();
        ImageIO.write(img, "png", new File("src/main/resources/Graph.png"));
    }

    void addPoint(int x, int y) {
        if (this.minX == -1) this.minX = x;
        this.maxX = x;
        this.data.add(new Point(x, y));
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    createAndShowGui();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//    }
}