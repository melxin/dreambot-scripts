/*
 * Copyright (c) 2021, 7ctx <https://github.com/7ctx/> 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package agility.pollnivneach;

import java.awt.Color;
import java.awt.Graphics2D;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.widgets.message.Message;

@ScriptManifest(
        author = "7ctx",
        name = "Agility_Pollnivneach",
        version = 1.0,
        description = "Agility Pollnivneach",
        category = Category.AGILITY)

public class Main extends AbstractScript implements ChatListener 
{
    /**
     * Variables
     */
    private Timer timer;
    private int laps = 0;
    private int marks = 0;

    /**
     * Start
     */
    @Override
    public void onStart() 
    {
        timer = new Timer();
        SkillTracker.start(Skill.AGILITY);
        log("Welcome to Pollnivneach Agility Script by 7ctx.");
    }

    /**
     * Is in area boolean
     */
    private boolean inArea(Area area) 
    {
        return area.contains(getLocalPlayer().getTile());
    }

    /**
     * Is on roof boolean
     */
    private boolean inRoof() 
    {
        return inArea(Constants.roofs.ROOF_1.getArea())
                || inArea(Constants.roofs.ROOF_2.getArea())
                || inArea(Constants.roofs.ROOF_3.getArea())
                || inArea(Constants.roofs.ROOF_4.getArea())
                || inArea(Constants.roofs.ROOF_5.getArea())
                || inArea(Constants.roofs.ROOF_6.getArea())
                || inArea(Constants.roofs.ROOF_7.getArea())
                || inArea(Constants.roofs.ROOF_8.getArea());
    }

    /**
     * Handles Game Messages received
     *
     * @param msg
     */
    @Override
    public void onMessage(Message msg) 
    {
        if (msg.getMessage().contains("")) 
        {
        }
    }

    /**
     * State enumeration
     */
    private enum State 
    {
        CLIMB, ROOFS, SLEEP
    };

    /**
     * State getter
     *
     * @return State
     */
    private State getState() 
    {
        // Make sure we don't die..
        if (Skills.getBoostedLevels(Skill.HITPOINTS) < 7) 
        {
            this.stop();
        }

        // Eats food when needed..
        if (Skills.getBoostedLevels(Skill.HITPOINTS) < 10) 
        {
            Inventory.get(Constants.getFood()).interact("Eat");
        }

        // Drink stamina potion...
        if (Walking.getRunEnergy() <= 15 && Inventory.contains(item -> item != null && item.getName().contains("Stamina"))) 
        {
            Inventory.interact(item -> item != null && item.getName().contains("Stamina"), "Drink");
        }

        // Enter/Climb when not inRoof & Animating & Moving | HealthBarVisible
        if (!inRoof()
                && !getLocalPlayer().isMoving()
                && !getLocalPlayer().isAnimating()
                || getLocalPlayer().isHealthBarVisible()) 
        {
            return State.CLIMB;
        }

        // Take mark of grace if one is in distance range.. otherwise continue doing roofs.
        GroundItem mark = GroundItems.closest(GroundItem -> GroundItem != null && GroundItem.getName().equalsIgnoreCase("Mark of grace"));
        if (mark != null && mark.distance(getLocalPlayer().getTile()) < 6) 
        {
            mark.interact("Take");
            marks++;
        } 
        else 
        {
            if (inRoof()) 
            {
                return State.ROOFS;
            }
        }
        return State.SLEEP;
    }

    /**
     * Loop
     *
     * @return sleep
     */
    @Override
    public int onLoop() 
    {
        switch (getState()) 
        {
            case CLIMB:
                GameObject Basket = GameObjects.closest(gameObject -> gameObject != null && gameObject.getName().equals("Basket") && gameObject.hasAction("Climb-on"));
                if (Basket.isOnScreen()) 
                {
                    Basket.interactForceRight("Climb-on");
                    sleep(Calculations.random(450, 670));
                } 
                else 
                {
                    Walking.walkExact(Basket.getTile());
                    sleepUntil(() -> Basket.isOnScreen(), 4000);
                }
                break;

            case ROOFS:
                // Rooftop_1
                if (inArea(Constants.roofs.ROOF_1.getArea())) 
                {
                    log("In ROOF_1 Area.");
                    GameObject Market_stall = GameObjects.closest(gameObject -> gameObject != null && gameObject.getName().equals("Market stall") && gameObject.hasAction("Jump-on"));
                    Market_stall.interact();
                    sleepUntil(() -> inArea(Constants.roofs.ROOF_2.getArea()), 5000);
                }

                // Rooftop_2
                if (inArea(Constants.roofs.ROOF_2.getArea())) 
                {
                    log("In ROOF_2 Area.");
                    sleep(550, 600);
                    Walking.walk(new Tile(3354, 2975, 1));
                    GameObject Banner = GameObjects.closest(gameObject -> gameObject != null && gameObject.getName().equals("Banner") && gameObject.hasAction("Grab"));
                    if (Banner.isOnScreen()) 
                    {
                        Banner.interactForceRight("Grab");
                        sleepUntil(() -> inArea(Constants.roofs.ROOF_3.getArea()), 3000);
                    }
                }

                // Rooftop_3
                if (inArea(Constants.roofs.ROOF_3.getArea())) 
                {
                    log("In ROOF_3 Area");
                    GameObject Gap = GameObjects.closest(gameObject -> gameObject != null && gameObject.getName().equals("Gap") && gameObject.hasAction("Leap"));
                    Gap.interact();
                    sleepUntil(() -> inArea(Constants.roofs.ROOF_4.getArea()), 3000);
                }

                // Rooftop_4
                if (inArea(Constants.roofs.ROOF_4.getArea())) 
                {
                    log("In ROOF_4 Area.");
                    GameObject Tree = GameObjects.closest(gameObject -> gameObject != null && gameObject.getName().equals("Tree") && gameObject.hasAction("Jump-to"));
                    Tree.interact();
                    sleepUntil(() -> inArea(Constants.roofs.ROOF_5.getArea()), 3000);
                }

                // Rooftop_5
                if (inArea(Constants.roofs.ROOF_5.getArea())) 
                {
                    log("In ROOF_5 Area.");
                    GameObject Rough_wall = GameObjects.closest(gameObject -> gameObject != null && gameObject.getName().equals("Rough wall") && gameObject.hasAction("Climb"));
                    Rough_wall.interact();
                    sleepUntil(() -> inArea(Constants.roofs.ROOF_6.getArea()), 5000);
                }

                // Rooftop_6
                if (inArea(Constants.roofs.ROOF_6.getArea())) 
                {
                    log("In ROOF_6 Area.");
                    GameObject Monkeybars = GameObjects.closest(gameObject -> gameObject != null && gameObject.getName().equals("Monkeybars") && gameObject.hasAction("Cross"));
                    Monkeybars.interactForceRight("Cross");
                    sleepUntil(() -> inArea(Constants.roofs.ROOF_7.getArea()), 3000);
                }

                // Rooftop_7
                if (inArea(Constants.roofs.ROOF_7.getArea())) 
                {
                    log("In ROOF_7 Area.");
                    GameObject Tree = GameObjects.closest(gameObject -> gameObject != null && gameObject.getName().equals("Tree") && gameObject.hasAction("Jump-on"));
                    Tree.interact();
                    sleepUntil(() -> inArea(Constants.roofs.ROOF_8.getArea()), 3000);
                }

                // Rooftop_8
                if (inArea(Constants.roofs.ROOF_8.getArea())) 
                {
                    log("In ROOF_8 Area.");
                    sleep(600);
                    GameObject Drying_line = GameObjects.closest(gameObject -> gameObject != null && gameObject.getName().equals("Drying line") && gameObject.hasAction("Jump-to"));
                    Drying_line.interact();
                    sleepUntil(() -> !inRoof(), 3000);
                    laps++;
                }
                break;
        }
        return Calculations.random(466, 676);
    }

    /**
     * Paint
     *
     * @param g1
     */
    @Override
    public void onPaint(Graphics2D g1) 
    {
        g1.setColor(Color.MAGENTA);
        g1.drawString("Runtime: " + timer.formatTime(), 10, 35); //35
        g1.drawString("Agility exp (p/h): " + SkillTracker.getGainedExperience(Skill.AGILITY) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.AGILITY) + ")", 10, 65); //65
        g1.drawString("Laps (p/h): " + laps + "(" + timer.getHourlyRate(laps) + ")", 10, 80);
        g1.drawString("Marks (p/h): " + marks + "(" + timer.getHourlyRate(marks) + ")", 10, 95);
    }
}
