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
package mining.mining_guild;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.message.Message;

@ScriptManifest(
        author = "7ctx",
        name = "Mining_Guild",
        version = 1.0,
        description = "Mining Guild",
        category = Category.MINING)

public class Main extends AbstractScript implements ChatListener
{
    private Timer timer;
    private int ore_count = 0;

    public void init() 
    {
        timer = new Timer();
        SkillTracker.start(Skill.MINING);
        log("Initialized");

    }

    /**
     * Is in area boolean
     */
    private boolean inArea(Area area) 
    {
        return area.contains(getLocalPlayer().getTile());
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

    @Override
    public void onStart() 
    {
        init();
        log("Welcome to Mining Guild script by 7ctx.");
        log("If you experience any issues while running this script please report them to me on the forums.");
    }

    private enum State 
    {
        MINE, DROP, SLEEP
    };

    private State getState() 
    {

        // Antiban
        if (Dialogues.inDialogue()) 
        {
            Tabs.open(Tab.SKILLS);
            sleep(Calculations.random(2300, 5500));
            Skills.hoverSkill(Skill.MINING);
            sleep(Calculations.random(2300, 5500));
            Tabs.open(Tab.INVENTORY);
        }

        if (Inventory.count(Constants.ore) > new Random().nextInt(7 + 1) + 10 || Inventory.isFull()) 
        {
            return State.DROP;
        } 
        else 
        {
            if (Constants.Iron_Rocks.distance(getLocalPlayer().getTile()) < 2) 
            {
                return State.MINE;
            }
        }

        return State.SLEEP;
    }

    @Override
    public int onLoop() 
    {
        switch (getState()) 
        {
            case MINE:
                GameObject Rock1 = GameObjects.getTopObjectOnTile(new Tile(3028, 9720, 0)); //7455 //7468
                GameObject Rock2 = GameObjects.getTopObjectOnTile(new Tile(3029, 9721, 0)); //7455 
                GameObject Rock3 = GameObjects.getTopObjectOnTile(new Tile(3030, 9720, 0)); //7455

                if (Rock1 != null && Rock1.getID() == 11364) 
                {
                    Rock1.interact();
                    sleepUntil(() -> Rock1.getID() == 11390, 1200);
                    ore_count++;
                } 
                else 
                {
                    if (Rock1 != null && Rock1.getID() == 11390 && Rock2 != null && Rock2.getID() == 11364) 
                    {
                        Rock2.interact();
                        sleepUntil(() -> Rock2.getID() == 11390, 1200);
                        ore_count++;
                    } 
                    else 
                    {
                        if (Rock2 != null && Rock2.getID() == 11390 && Rock3 != null && Rock3.getID() == 11364) 
                        {
                            Rock3.interact();
                            sleepUntil(() -> Rock3.getID() == 11390, 1200);
                            ore_count++;
                        }
                    }
                }
                break;
                
            case DROP:
                Inventory.dropAll(Constants.ore);
                break;
                
            case SLEEP:
                sleep(Calculations.random(108, 505));
                break;
        }
        return Calculations.random(204, 967);
    }

    @Override
    public void onPaint(Graphics2D g1) 
    {
        g1.setColor(Color.RED);
        g1.drawString("Runtime: " + timer.formatTime(), 10, 35);
        g1.drawString("Mining exp (p/h): " + SkillTracker.getGainedExperience(Skill.MINING) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.MINING) + ")", 10, 65);
        g1.drawString("Ores gained (p/h): " + ore_count + "(" + timer.getHourlyRate(ore_count) + ")", 10, 80);
    }
}
