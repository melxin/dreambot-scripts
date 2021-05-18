/**
 * All credits go to Benjamin J. Land a.k.a. BenLand100
 * 
 * Thanks holic <https://dreambot.org/forums/index.php?/topic/21147-windmouse-custom-mouse-movement-algorithm/>
 * for conversion, modifications and sharing
 */
package com.sandcrabs;

import java.awt.Point;
import org.dreambot.api.Client;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.input.mouse.algorithm.MouseMovementAlgorithm;
import org.dreambot.api.input.mouse.destination.AbstractMouseDestination;
import org.dreambot.api.methods.Calculations;
import static org.dreambot.api.methods.MethodProvider.log;
import org.dreambot.api.methods.input.mouse.MouseSettings;

public class WindMouse implements MouseMovementAlgorithm 
{
    private int _mouseSpeed = MouseSettings.getSpeed() > 15 ? MouseSettings.getSpeed() - 10 : 15;
    private final int _mouseSpeedLow = Math.round(_mouseSpeed / 2);
    private int _mouseGravity = Calculations.random(4, 20);
    private int _mouseWind = Calculations.random(1, 10);

    @Override
    public boolean handleMovement(AbstractMouseDestination abstractMouseDestination) 
    {
        //Get a suitable point for the mouse's destination
        Point suitPos = abstractMouseDestination.getSuitablePoint();

        // Select which implementation of WindMouse you'd like to use
        // by uncommenting out the line you want to use below:

        //windMouse(suitPos.x, suitPos.y); //Original implementation
        windMouse2(suitPos); //Tweaked implementation

        return distance(Client.getMousePosition(), suitPos) < 2;
    }

    public static void sleep(int min, int max) 
    {
        try 
        {
            Thread.sleep(Calculations.random(min,max));
        } 
        catch (InterruptedException e) 
        {
            log(e.getMessage());
        }
    }
    public static void sleep(int ms) 
    {
        try 
        {
            Thread.sleep(ms);
        } 
        catch (InterruptedException e) 
        {
            log(e.getMessage());
        }
    }
    /**
     * Tweaked implementation of WindMouse
     * Moves to a mid point on longer moves to seem a little more human-like
     * Remove the if statement below if you'd rather straighter movement
     * @param point   The destination point
     */
    public void windMouse2(Point point) 
    {
        Point curPos = Client.getMousePosition();
        if (distance(point, curPos) > 250 && Calculations.random(1) == 2) 
        {
            Point rp = randomPoint(point, curPos);
            windMouse2(curPos.x, curPos.y, rp.x, rp.y, _mouseGravity, _mouseWind, _mouseSpeed, Calculations.random(5, 25));
            sleep(1, 150);
        }
        windMouse2(curPos.x, curPos.y, point.x, point.y, _mouseGravity, _mouseWind, _mouseSpeed, Calculations.random(5, 25));
        _mouseGravity = Calculations.random(4, 20);
        _mouseWind = Calculations.random(1, 10);
        _mouseSpeed = Calculations.random(_mouseSpeedLow, MouseSettings.getSpeed());
    }

    /**
     * Tweaked implementation of WindMouse by holic
     * All credit to Benjamin J. Land for the original. (see below)
     *
     * @param xs         The x start
     * @param ys         The y start
     * @param xe         The x destination
     * @param ye         The y destination
     * @param gravity    Strength pulling the position towards the destination
     * @param wind       Strength pulling the position in random directions
     * @param targetArea Radius of area around the destination that should
     *                   trigger slowing, prevents spiraling
     */
    private void windMouse2(double xs, double ys, double xe, double ye, double gravity, double wind, double speed, double targetArea) 
    {
        double dist, veloX = 0, veloY = 0, windX = 0, windY = 0;

        double sqrt2 = Math.sqrt(2);
        double sqrt3 = Math.sqrt(3);
        double sqrt5 = Math.sqrt(5);

        int tDist = (int) distance(xs, ys, xe, ye);
        long t = System.currentTimeMillis() + 10000;

        while (!(Math.hypot((xs - xe), (ys - ye)) < 1)) 
        {
            if (System.currentTimeMillis() > t) break;

            dist = Math.hypot((xs - xe), (ys - ye));
            wind = Math.min(wind, dist);
            if ((dist < 1)) 
            {
                dist = 1;
            }

            long d = (Math.round((Math.round(((double) (tDist))) * 0.3)) / 7);
            if ((d > 25)) 
            {
                d = 25;
            }

            if ((d < 5)) 
            {
                d = 5;
            }

            double rCnc = Calculations.random(6);
            if ((rCnc == 1)) 
            {
                d = 2;
            }

            double maxStep = (Math.min(d, Math.round(dist))) * 1.5;
            if ((dist >= targetArea)) 
            {
                windX = (windX / sqrt3) + ((Calculations.random((int) ((Math.round(wind) * 2) + 1)) - wind) / sqrt5);
                windY = (windY / sqrt3) + ((Calculations.random((int) ((Math.round(wind) * 2) + 1)) - wind) / sqrt5);
            } 
            else 
            {
                windX = (windX / sqrt2);
                windY = (windY / sqrt2);
            }

            veloX += windX + gravity * (xe - xs) / dist;
            veloY += windY + gravity * (ye - ys) / dist;

            if ((Math.hypot(veloX, veloY) > maxStep)) 
            {
                maxStep = ((maxStep / 2) < 1) ? 2 : maxStep;

                double randomDist = (maxStep / 2) + Calculations.random((int) (Math.round(maxStep) / 2));
                double veloMag = Math.sqrt(((veloX * veloX) + (veloY * veloY)));
                veloX = (veloX / veloMag) * randomDist;
                veloY = (veloY / veloMag) * randomDist;
            }

            int lastX = ((int) (Math.round(xs)));
            int lastY = ((int) (Math.round(ys)));
            xs += veloX;
            ys += veloY;
            if ((lastX != Math.round(xs)) || (lastY != Math.round(ys))) 
            {
                Mouse.hop(new Point((int) Math.round(xs), (int) Math.round(ys)));
            }

            int w = Calculations.random((int) (Math.round(100 / speed))) * 6;
            if ((w < 5)) 
            {
                w = 5;
            }

            w = (int) Math.round(w * 0.9);
            sleep(w);
        }

        if (((Math.round(xe) != Math.round(xs)) || (Math.round(ye) != Math.round(ys)))) 
        {
            Mouse.hop(new Point(((int) (Math.round(xe))), ((int) (Math.round(ye)))));
        }
    }

    /**
     * Internal mouse movement algorithm from SMART. Do not use this without credit to either
     * Benjamin J. Land or BenLand100. This was originally synchronized to prevent multiple
     * motions and bannage but functions poorly with DB3.
     *
     * BEST USED IN FIXED MODE
     *
     * @param xs         The x start
     * @param ys         The y start
     * @param xe         The x destination
     * @param ye         The y destination
     * @param gravity    Strength pulling the position towards the destination
     * @param wind       Strength pulling the position in random directions
     * @param minWait    Minimum relative time per step
     * @param maxWait    Maximum relative time per step
     * @param maxStep    Maximum size of a step, prevents out of control motion
     * @param targetArea Radius of area around the destination that should
     *                   trigger slowing, prevents spiraling
     * @result The actual end point
     */
    /*private Point windMouseImpl(double xs, double ys, double xe, double ye, double gravity, double wind, double minWait, double maxWait, double maxStep, double targetArea) 
    {
        final double sqrt3 = Math.sqrt(3);
        final double sqrt5 = Math.sqrt(5);

        double dist, veloX = 0, veloY = 0, windX = 0, windY = 0;
        while ((dist = Math.hypot(xs - xe, ys - ye)) >= 1) 
        {
            wind = Math.min(wind, dist);
            if (dist >= targetArea) 
            {
                windX = windX / sqrt3 + (2D * Math.random() - 1D) * wind / sqrt5;
                windY = windY / sqrt3 + (2D * Math.random() - 1D) * wind / sqrt5;
            } 
            else 
            {
                windX /= sqrt3;
                windY /= sqrt3;
                if (maxStep < 3) 
                {
                    maxStep = Math.random() * 3D + 3D;
                } 
                else 
                {
                    maxStep /= sqrt5;
                }
            }
            veloX += windX + gravity * (xe - xs) / dist;
            veloY += windY + gravity * (ye - ys) / dist;
            double veloMag = Math.hypot(veloX, veloY);
            if (veloMag > maxStep) 
            {
                double randomDist = maxStep / 2D + Math.random() * maxStep / 2D;
                veloX = (veloX / veloMag) * randomDist;
                veloY = (veloY / veloMag) * randomDist;
            }
            int lastX = ((int) (Math.round(xs)));
            int lastY = ((int) (Math.round(ys)));
            xs += veloX;
            ys += veloY;
            if ((lastX != Math.round(xs)) || (lastY != Math.round(ys))) 
            {
                setMousePosition(new Point((int) Math.round(xs), (int) Math.round(ys)));
            }
            double step = Math.hypot(xs - lastX, ys - lastY);
            sleep((int) Math.round((maxWait - minWait) * (step / maxStep) + minWait));
        }
        return new Point((int) xs, (int) ys);
    }*/

    /**
     * Moves the mouse from the current position to the specified position.
     * Approximates human movement in a way where smoothness and accuracy are
     * relative to speed, as it should be.
     *
     * @param x The x destination
     * @param y The y destination
     * @return windMouseImpl ->
     * @result The actual end point
     */
    /*public Point windMouse(int x, int y) 
    {
        Point c = Client.getMousePosition();
        double speed = (Math.random() * 15D + 15D) / 10D;
        return windMouseImpl(c.x, c.y, x, y, 9D, 3D, 5D / speed, 10D / speed, 10D * speed, 8D * speed);
    }*/

    /*private void setMousePosition(Point p) 
    {
        Mouse.hop(p.x, p.y);
    }*/

    private static double distance(double x1, double y1, double x2, double y2) 
    {
        return Math.sqrt((Math.pow((Math.round(x2) - Math.round(x1)), 2) + Math.pow((Math.round(y2) - Math.round(y1)), 2)));
    }

    public double distance(Point p1, Point p2) 
    {
        return Math.sqrt((p2.y - p1.y) * (p2.y - p1.y) + (p2.x - p1.x) * (p2.x - p1.x));
    }

    public static float randomPointBetween(float corner1, float corner2) 
    {
        if (corner1 == corner2) 
        {
            return corner1;
        }
        float delta = corner2 - corner1;
        float offset = Calculations.getRandom().nextFloat() * delta;
        return corner1 + offset;
    }

    public Point randomPoint(Point p1, Point p2) 
    {
        int randomX = (int) randomPointBetween(p1.x, p2.x);
        int randomY = (int) randomPointBetween(p1.y, p2.y);
        return new Point(randomX, randomY);
    }

}
