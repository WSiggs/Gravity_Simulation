package Physics;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;

/**
 * @author Siggy
 *         $
 */
public class Content extends Canvas
{
    private static final int K_WIDTH = 640;
    private static final int K_HEIGHT = 480;

    private static final int K_ARROW_LENGTH = 20;
    private static final int K_ARROW_WIDTH = 20;

    private static final int K_PLANE_RADIUS = 200;

    private Point2D origin = new Point2D.Double(K_WIDTH / 2.0, K_HEIGHT / 2.0);

    private double rotation = 0;

    private Point2D x_pos;
    private Point2D x_neg;
    private Point2D y_pos;
    private Point2D y_neg;

    private Point2D grav;

    private Point2D vertex;


    public Content()
    {
        setPreferredSize(new Dimension(K_WIDTH, K_HEIGHT));

        x_pos = new Point2D.Double( K_PLANE_RADIUS * Math.cos(Math.toRadians(rotation)) + origin.getX(),
                                    K_PLANE_RADIUS * Math.sin(Math.toRadians(rotation))  + origin.getY());

        x_neg = new Point2D.Double( K_PLANE_RADIUS * Math.cos(Math.toRadians(180 + rotation)) + origin.getX(),
                                    K_PLANE_RADIUS * Math.sin(Math.toRadians(180 + rotation)) + origin.getY());

        y_pos = new Point2D.Double( K_PLANE_RADIUS * Math.cos(Math.toRadians(270 + rotation)) + origin.getX(),
                                    K_PLANE_RADIUS * Math.sin(Math.toRadians(270 + rotation)) + origin.getY());

        y_neg = new Point2D.Double( K_PLANE_RADIUS * Math.cos(Math.toRadians(90 + rotation)) + origin.getX(),
                                    K_PLANE_RADIUS * Math.sin(Math.toRadians(90 + rotation)) + origin.getY());

        grav = new Point2D.Double(K_WIDTH/2.0, 400);

        vertex = new Point2D.Double(150 * Math.cos(Math.toRadians(90 + rotation)) + origin.getX(),
                                    150 * Math.sin(Math.toRadians(90 + rotation)) + origin.getY());
    }

    public void draw(int rotation, double mass)
    {
        updatePoints();

        this.rotation = rotation;

        //super.paintComponent(g);
        BufferStrategy bs = getBufferStrategy();
        if (bs == null)
        {
            createBufferStrategy(1);
            return;
        }

        Graphics2D g2d = (Graphics2D)bs.getDrawGraphics();

        g2d.clearRect(0, 0, K_WIDTH, K_HEIGHT);

        g2d.drawString("X", (int)x_pos.getX() + 5, (int)x_pos.getY() + 5);
        g2d.drawString("Y", (int)y_pos.getX() + 5, (int)y_pos.getY() + 5);

        g2d.scale(1.2, 1.2);
        g2d.drawString("Angle: " + String.valueOf(rotation), 10, 20);
        //g2d.drawString("Mass: " + String.valueOf(mass), 10, 35);

        g2d.scale(1.0/1.2, 1.0/1.2);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(4));

        // DRAW X POSITIVE
        g2d.drawLine((int)origin.getX(), (int)origin.getY(), (int)x_pos.getX(), (int)x_pos.getY());
        g2d.draw(drawArrowForLine((int)x_pos.getX(), (int)x_pos.getY(), rotation, K_ARROW_LENGTH, K_ARROW_WIDTH));

        // DRAW Y POSITIVE
        g2d.drawLine((int)origin.getX(), (int)origin.getY(), (int)y_pos.getX(), (int)y_pos.getY());
        g2d.draw(drawArrowForLine((int)y_pos.getX(), (int)y_pos.getY(), (270 + rotation), K_ARROW_LENGTH, K_ARROW_WIDTH));

        g2d.setStroke(new BasicStroke(2));
        // DRAW X NEGATIVE
        g2d.drawLine((int)origin.getX(), (int)origin.getY(), (int)x_neg.getX(), (int)x_neg.getY());
        g2d.draw(drawArrowForLine((int)x_neg.getX(), (int)x_neg.getY(), (180 + rotation), K_ARROW_LENGTH, K_ARROW_WIDTH));

        // DRAW Y NEGATIVE
        g2d.drawLine((int)origin.getX(), (int)origin.getY(), (int)y_neg.getX(), (int)y_neg.getY());
        g2d.draw(drawArrowForLine((int)y_neg.getX(), (int)y_neg.getY(), (90 + rotation), K_ARROW_LENGTH, K_ARROW_WIDTH));

        // X AND Y COMPONENTS
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));

        g2d.drawLine((int)origin.getX(), (int)origin.getY(), (int)vertex.getX(), (int)vertex.getY());
        if(Math.abs(rotation % 90) >= 5)
        {
            g2d.draw(drawArrowForLine((int) vertex.getX(), (int) vertex.getY(), (90  + rotation), 15, 10));
        }


        g2d.drawLine((int)vertex.getX(), (int)vertex.getY(), (int)grav.getX(), (int)grav.getY());
        if(Math.abs(rotation % 90) >= 5)
        {
            if(rotation > 0)
                g2d.draw(drawArrowForLine((int) grav.getX(), (int) grav.getY(), rotation, 15, 10));
            else
                g2d.draw(drawArrowForLine((int) grav.getX(), (int) grav.getY(), 180+rotation, 15, 10));
        }

        // GRAV VECTOR
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(2));

        g2d.drawLine((int)origin.getX(), (int)origin.getY(), (int)grav.getX(), (int)grav.getY());
        g2d.draw(drawArrowForLine((int)grav.getX(), (int)grav.getY(), 90, 15, 15));


        g2d.dispose();
        bs.show();
    }

    public Path2D drawArrowForLine(int x1, int y1, double angleDegrees, double length, double width)
    {
        Path2D result = new Path2D.Double();

        double radB = Math.toRadians(-angleDegrees + width);
        double radC = Math.toRadians(-angleDegrees - width);

        result.moveTo(length * -Math.cos(radB) + x1, length * Math.sin(radB) + y1);
        result.lineTo(x1, y1);
        result.lineTo(length * -Math.cos(radC) + x1, length * Math.sin(radC) + y1);

        return result;

    }

    public void updatePoints()
    {
        //TODO: get rotation value

        x_pos.setLocation(K_PLANE_RADIUS * Math.cos(Math.toRadians(rotation)) + origin.getX(),
                            K_PLANE_RADIUS * Math.sin(Math.toRadians(rotation))  + origin.getY());

        x_neg.setLocation(K_PLANE_RADIUS * Math.cos(Math.toRadians(180 + rotation)) + origin.getX(),
                            K_PLANE_RADIUS * Math.sin(Math.toRadians(180 + rotation))  + origin.getY());

        y_pos.setLocation(K_PLANE_RADIUS * Math.cos(Math.toRadians(270 + rotation)) + origin.getX(),
                            K_PLANE_RADIUS * Math.sin(Math.toRadians(270 + rotation)) + origin.getY());

        y_neg.setLocation(K_PLANE_RADIUS * Math.cos(Math.toRadians(90 + rotation)) + origin.getX(),
                            K_PLANE_RADIUS * Math.sin(Math.toRadians(90 + rotation)) + origin.getY());

        vertex.setLocation(-(160.0 * Math.cos(Math.toRadians(rotation)) * Math.sin(Math.toRadians(rotation)))+ origin.getX(),
                            (160.0 * Math.cos(Math.toRadians(rotation)) * Math.cos(Math.toRadians(rotation))) + origin.getY());
    }

}
