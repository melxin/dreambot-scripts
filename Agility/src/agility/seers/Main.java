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
package agility.seers;

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
        name = "Agility_Seers",
        version = 1.0,
        description = "Agility Seers",
        category = Category.AGILITY)

public class Main extends AbstractScript implements ChatListener
{
    // Variables
    private Timer timer;
    private int marks = 0;
    private int laps = 0;

    /**
     * Start
     */
    @Override
    public void onStart() 
    {
        timer = new Timer();
        SkillTracker.start(Skill.AGILITY);
        log("Welcome to Agility Bot by 7ctx.");
        log("If you experience any issues while running this script please report them to me on the forums.");
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
                || inArea(Constants.roofs.ROOF_5.getArea());
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
        ENTER, ROOFS, SLEEP
    };

    /**
     * State getter
     *
     * @return State
     */
    private State getState() 
    {
        // Stop at given agility level
        if (Skills.getBoostedLevels(Skill.AGILITY) == 70) 
        {
            log("Stopping... destination level 70 reached");
            this.stop();
        }

        // Stop before deing
        if (Skills.getBoostedLevels(Skill.HITPOINTS) < 4) 
        {
            this.stop();
        }

        // Eat food
        if (Skills.getBoostedLevels(Skill.HITPOINTS) < 7) 
        {
            Inventory.get(Constants.getFood()).interact("Eat");
        }

        // Drink Stamina potion.
        if (Walking.getRunEnergy() <= 15 && Inventory.contains(item -> item != null && item.getName().contains("Stamina"))) 
        {
            Inventory.interact(item -> item != null && item.getName().contains("Stamina"), "Drink");
        }

        // Enter when not inRoof & Animating & Moving | HealthBarVisible.
        if (!inRoof() 
                && !getLocalPlayer().isMoving()
                && !getLocalPlayer().isAnimating()
                || getLocalPlayer().isHealthBarVisible()) 
        {
            return State.ENTER;
        }

        // Take mark of grace if one is in distance range.. otherwise continue doing roofs.
        GroundItem mark = GroundItems.closest(GroundItem -> GroundItem != null && GroundItem.getName().equalsIgnoreCase("Mark of grace"));
        if (mark != null && mark.distance(getLocalPlayer().getTile()) <= 5) 
        {
            mark.interact("Take");
            sleep(5000);
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
            case ENTER:
                log("STATUS = ENTER");
                GameObject wall = GameObjects.closest(gameObject -> gameObject != null && gameObject.getName().equals("Wall"));
                Walking.walk(wall.getTile());
                if (wall.isOnScreen() && wall.interactForceRight("Climb-up")) 
                {
                    sleepUntil(() -> inArea(Constants.roofs.ROOF_1.getArea()), 3500);
                }
                break;

            case ROOFS:
                GroundItem mark = GroundItems.closest(GroundItem -> GroundItem != null && GroundItem.getName().equalsIgnoreCase("Mark of grace"));

                // Rooftop_1
                if (inArea(Constants.roofs.ROOF_1.getArea())) 
                {
                    log("In ROOF_1 Area.");
                    if (Constants.roofs.ROOF_1.getArea().contains(mark)) 
                    {
                        mark.interactForceRight("Take");
                        sleepUntil(() -> !mark.exists(), 3500);
                        marks++;
                    }
                    GameObject gap = GameObjects.closest(gameObject -> gameObject != null && gameObject.getX() == 2720 && gameObject.getY() == 3492);
                    if (gap.interact()) 
                    {
                        sleepUntil(() -> inArea(Constants.roofs.ROOF_2.getArea()), 3500);
                    }
                }

                // Rooftop_2
                if (inArea(Constants.roofs.ROOF_2.getArea())) 
                {
                    log("In ROOF_2 Area.");
                    if (Constants.roofs.ROOF_2.getArea().contains(mark)) 
                    {
                        mark.interactForceRight("Take");
                        sleepUntil(() -> !mark.exists(), 3500);
                        marks++;
                    }
                    GameObject rope = GameObjects.closest(gameObject -> gameObject != null && gameObject.getX() == 2710 && gameObject.getY() == 3489);
                    if (rope.interact()) 
                    {
                        sleepUntil(() -> inArea(Constants.roofs.ROOF_3.getArea()), 3500);
                    }
                }

                // Rooftop_3
                if (inArea(Constants.roofs.ROOF_3.getArea())) 
                {
                    log("In ROOF_3 Area.");
                    if (Constants.roofs.ROOF_3.getArea().contains(mark)) 
                    {
                        mark.interactForceRight("Take");
                        sleepUntil(() -> !mark.exists(), 3500);
                        marks++;
                    }
                    GameObject gap = GameObjects.closest(gameObject -> gameObject != null && gameObject.getX() == 2710 && gameObject.getY() == 3476);
                    if (gap.interact()) 
                    {
                        sleepUntil(() -> inArea(Constants.roofs.ROOF_4.getArea()), 3500);
                    }
                }

                // Rooftop_4
                if (inArea(Constants.roofs.ROOF_4.getArea())) 
                {
                    log("In ROOF_4 Area.");
                    if (Constants.roofs.ROOF_4.getArea().contains(mark)) 
                    {
                        mark.interactForceRight("Take");
                        sleepUntil(() -> !mark.exists(), 3500);
                        marks++;
                    }
                    GameObject gap = GameObjects.closest(gameObject -> gameObject != null && gameObject.hasAction("Jump"));
                    if (gap.interact()) 
                    {
                        sleepUntil(() -> inArea(Constants.roofs.ROOF_5.getArea()), 3500);
                    }
                }

                // Rooftop_5
                if (inArea(Constants.roofs.ROOF_5.getArea())) 
                {
                    log("In ROOF_5 Area.");
                    if (Constants.roofs.ROOF_5.getArea().contains(mark)) 
                    {
                        mark.interactForceRight("Take");
                        sleepUntil(() -> !mark.exists(), 3500);
                        marks++;
                    }
                    GameObject edge = GameObjects.closest(gameObject -> gameObject != null && gameObject.hasAction("Jump"));
                    if (edge.interact()) 
                    {
                        sleepUntil(() -> !inArea(Constants.roofs.ROOF_5.getArea()), 3500);
                        laps++;
                        Walking.walk(new Tile(2724, 3474).getRandomizedTile());
                    }
                }
                break;

            case SLEEP:
                sleep(Calculations.random(625, 880));
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
        g1.drawString("Runtime: " + timer.formatTime(), 10, 35);
        g1.drawString("Agility exp (p/h): " + SkillTracker.getGainedExperience(Skill.AGILITY) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.AGILITY) + ")", 10, 65);
        g1.drawString("Laps (p/h): " + laps + "(" + timer.getHourlyRate(laps) + ")", 10, 80);
        g1.drawString("Marks (p/h): " + marks + "(" + timer.getHourlyRate(marks) + ")", 10, 95);
    }
}
