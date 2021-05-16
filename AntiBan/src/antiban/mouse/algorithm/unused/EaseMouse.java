package antiban.mouse.algorithm.unused;

import java.awt.Point;
import static java.lang.Double.min;
import static java.lang.Math.hypot;
import static java.lang.Math.max;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;
import org.dreambot.api.Client;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.input.mouse.algorithm.MouseMovementAlgorithm;
import org.dreambot.api.input.mouse.destination.AbstractMouseDestination;
import org.dreambot.api.methods.Calculations;
import static org.dreambot.api.methods.MethodProvider.sleep;
import org.dreambot.api.methods.input.mouse.MouseSettings;

public class EaseMouse implements MouseMovementAlgorithm 
{
    private final int _mouseSpeed = MouseSettings.getSpeed() > 15 ? MouseSettings.getSpeed() - 10 : 15;
    private final int _mouseSpeedLow = round(_mouseSpeed / 2);
    private int _mouseGravity = Calculations.random(5, 10);

    private static double distance(double x1, double y1, double x2, double y2) 
    {
        return sqrt((pow((round(x2) - round(x1)), 2) + pow((round(y2) - round(y1)), 2)));
    }

    @Override
    public boolean handleMovement(AbstractMouseDestination abstractMouseDestination) 
    {
        //Get a suitable point for the mouse's destination
        Point suitPos = abstractMouseDestination.getSuitablePoint();
        easeMouse(suitPos, Calculations.random(1,3), Calculations.random(1,3), (Calculations.random(4) == 1));
        return distance(Client.getMousePosition(), suitPos) < 1;
    }
    
    /*******************************************************************************
     * void easeMouse(Point point, int ranX, int ranY, boolean reverse)
     * By: holic
     * Description: Mouse speed decreases as it approaches destination point.
     * @param point   The destination point
     * @param ranX    Random offset for X coord
     * @param ranY    Random offset for Y coord
     * @param reverse if true, speed starts slow and increases through the first 15% of the path.
     *******************************************************************************/
    public void easeMouse(Point point, int ranX, int ranY, boolean reverse) 
    {
        double randSpeed;
        Point curPoint = Client.getMousePosition();
        randSpeed = (Calculations.random(_mouseSpeedLow, _mouseSpeed) / 8.0);
        easeWindMouse(curPoint.x, curPoint.y, Calculations.random(point.x, point.x + ranX), Calculations.random(point.y, point.y + ranY), _mouseGravity, Calculations.random(3, 5), 3 * randSpeed, reverse);
        _mouseGravity = Calculations.random(5, 10);
    }

    /*******************************************************************************
     * void easeWindMouse(double xs, double ys, double xe, double ye, double gravity,
     * double wind, double targetArea, boolean reverse)
     * By: Flight & Benland100
     * Description: Mouse movement based on distance to determine speed, slows as it
     * approaches destination point.
     * @param xs         The start point x
     * @param ys         The start point y
     * @param xe         The destination point x
     * @param ye        The destination point y
     * @param gravity    How hard to pull mouse to target
     * @param wind       How much to deviate from destination path
     * @param targetArea Accuracy of target, area in which target is accepted
     * @param reverse    if true, speed starts slow and increases through the first 15% of the path.
     *******************************************************************************/
    public void easeWindMouse(double xs, double ys, double xe, double ye, double gravity, double wind, double targetArea, boolean reverse) 
    {
        long T;
        double veloX = 0, veloY = 0, windX = 0, windY = 0, veloMag, dist, randomDist, D = 0;
        int lastX, lastY, W, TDist;
        double sqrt2, sqrt3, sqrt5, PDist, maxStep, dModA, dModB, nModA = 0, nModB = 0;

        sqrt2 = sqrt(2);
        sqrt3 = sqrt(3);
        sqrt5 = sqrt(5);

        TDist = (int) distance(round(xs), round(ys), round(xe), round(ye));
        if (TDist < 1) 
        {
            TDist = 1;
        }

        dModA = 0.88;
        dModB = 0.95;

        if (TDist > 220) 
        {
            nModA = 0.08;
            nModB = 0.04;
        } 
        else if (TDist <= 220) 
        {
            nModA = 0.20;
            nModB = 0.10;
        }

        T = System.currentTimeMillis();
        do 
        {
            if (System.currentTimeMillis() - T > 5000) 
            {
                break;
            }

            dist = hypot(xs - xe, ys - ye);
            wind = min(wind, dist);
            if (dist < 1) 
            {
                dist = 1;
            }
            PDist = (dist / TDist);
            if (PDist < 0.01) 
            {
                PDist = 0.01;
            }

            if (reverse) 
            {
                if (PDist <= dModA) 
                {
                    D = (round((round(dist) * 0.3)) / 5);
                    if (D < 20) 
                    {
                        D = 20;
                    }

                } 
                else if (PDist > dModA) 
                {
                    if (PDist < dModB) 
                    {
                        D = Calculations.random(5, 8);
                    } else if (PDist >= dModB) {
                        D = Calculations.random(3, 4);
                    }
                }
            }

            if (PDist >= nModA) 
            {
                D = (round((round(dist) * 0.3)) / 5);
                if (D < 20) 
                {
                    D = 20;
                }
            } 
            else if (PDist < nModA) 
            {
                if (PDist >= nModB) 
                {
                    D = Calculations.random(5, 8);
                } 
                else if (PDist < nModB) 
                {
                    D = Calculations.random(3, 4);
                }
            }

            if (D <= round(dist)) 
            {
                maxStep = D;
            } 
            else 
            {
                maxStep = round(dist);
            }
            maxStep = maxStep * 0.85;

            if (dist >= targetArea) 
            {
                windX = windX / sqrt3 + (Calculations.random((int) (round(wind) * 2 + 1)) - wind) / sqrt5;
                windY = windY / sqrt3 + (Calculations.random((int) (round(wind) * 2 + 1)) - wind) / sqrt5;
            } 
            else 
            {
                windX = windX / sqrt2;
                windY = windY / sqrt2;
            }

            veloX = veloX + windX;
            veloY = veloY + windY;
            veloX = veloX + gravity * (xe - xs) / dist;
            veloY = veloY + gravity * (ye - ys) / dist;

            if (hypot(veloX, veloY) > maxStep) 
            {
                int tmp = maxStep > 2 ? Calculations.random((int) (round(maxStep) / 2)) : 1;
                randomDist = maxStep / 2.0 + tmp;
                veloMag = sqrt(veloX * veloX + veloY * veloY);
                veloX = (veloX / veloMag) * randomDist;
                veloY = (veloY / veloMag) * randomDist;
            }

            lastX = (int) round(xs);
            lastY = (int) round(ys);
            xs = xs + veloX;
            ys = ys + veloY;

            if (lastX != round(xs) || lastY != round(ys)) 
            {
                Mouse.hop((int) round(xs), (int) round(ys));
            }

            W = (Calculations.random((round(100 / _mouseSpeed))) * 6);
            W = max(W, 5);
            if (reverse) 
            {
                if (PDist > dModA) 
                {
                    W = (int) round(W * 2.5);
                }
            } 
            else 
            {
                W = (int) round(W * 1.2);
            }
            sleep(W);
        } 
        while (hypot(xs - xe, ys - ye) > 1);

        if (round(xe) != round(xs) || round(ye) != round(ys)) 
        {
            Mouse.hop((int) round(xe), (int) round(ye));
        }
    }

    private double distance(Point p1, Point p2) 
    {
        return sqrt((p2.y - p1.y) * (p2.y - p1.y) + (p2.x - p1.x) * (p2.x - p1.x));
    }
}