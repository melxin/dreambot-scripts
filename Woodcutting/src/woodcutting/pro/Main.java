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
package woodcutting.pro;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import org.dreambot.api.Client;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.emotes.Emote;
import org.dreambot.api.methods.emotes.Emotes;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.message.Message;

@ScriptManifest(
        author = "7ctx",
        name = "Woodcutting Pro",
        version = 0.1,
        description = "Woodcutting Pro",
        category = Category.WOODCUTTING)

public class Main extends AbstractScript implements ChatListener
{
    // Variables
    private Timer Timer;
    private Timer antibanTimer;
    private int logsCount;
    private int birdnestCount;

    /**
     * On start
     */
    @Override
    public void onStart() 
    {
        Client.getInstance().setMouseMovementAlgorithm(new WindMouse());
        
        log("Welcome to " + Constants.getName());
        // Start the Frame..
        java.awt.EventQueue.invokeLater(() -> 
        {
            new JFrame().setVisible(true);
        });
        
        // Wait for Start button to be clicked..
        if (!Constants.getStart()) 
        {
            sleepUntil(() -> Constants.getStart(), 15000000);
        }
        
        log("Tree selected : " + Constants.getTree());
        
        // Finally Start..
        Timer = new Timer();
        antibanTimer = new Timer();
        SkillTracker.start(Skill.WOODCUTTING);
        Constants.setStatus("Start");
    }

    /**
     * Message received
     *
     * @param msg
     */
    @Override
    public void onMessage(Message msg) 
    {
        if (msg.getMessage().contains("You get some")) 
        {
            logsCount++;
        }
        if (msg.getMessage().contains("A bird's nest falls out of the tree.")) 
        {
            birdnestCount++;
        }
    }

    /**
     * State enumeration
     */
    private enum State 
    {
        DROP, CUT
    };

    private State getState() 
    {
        // Stop at given woodcutting level
        if (Skills.getRealLevel(Skill.WOODCUTTING) == Constants.getStop() && Constants.getStop() > 0) 
        {
            log("Stopping Script at lvl: " + Constants.getStop());
            this.stop();
        }

        // ANTIBAN
        if (Dialogues.inDialogue()) 
        {
            sleep(Calculations.random(4000, 9000));
            Walking.walk(getLocalPlayer().getTile().getRandomizedTile());
            sleep(Calculations.random(3000, 9000));
            Tabs.open(Tab.SKILLS);
            sleep(Calculations.random(2000, 5000));
            Skills.hoverSkill(Skill.WOODCUTTING);
            return State.CUT;
        }
        
        if (antibanTimer.elapsed() >= Calculations.random(240000, 800000)) 
        {
            Random random = new Random(180);
            log("Antiban");
            Constants.setStatus("Antiban");
            if (random.nextInt() <= 100) 
            {
                Tabs.open(Tab.SKILLS);
                sleepUntil(() -> Tabs.isOpen(Tab.SKILLS), 5000);
                Skills.hoverSkill(Skill.WOODCUTTING);
                sleep(Calculations.random(2000, 4000));
                antibanTimer.reset();
            } 
            else 
            {
                if (random.nextInt() > 100 && random.nextInt() < 150) 
                {
                    Tabs.open(Tab.EMOTES);
                    sleepUntil(() -> Tabs.isOpen(Tab.EMOTES), 5000);
                    Emotes.doEmote(Emote.DANCE);
                    antibanTimer.reset();
                } 
                else 
                {
                    if (random.nextInt() >= 150) 
                    {
                        sleep(Calculations.random(120000, 240000));
                        antibanTimer.reset();
                    }
                }
            }
        }

        // Take Bird nest from the ground (Don't worry no eggs in there)
        for (GroundItem item : GroundItems.all()) 
        {
            if (item != null && item.getName().toLowerCase().contains("bird nest")) 
            {
                log("Found a Bird nest");
                Constants.setStatus("Take Bird nest");
                item.interact();
            }
        }
        
        // Drop else Cut..
        if (Inventory.count(Item -> Item != null && Item.getName().toLowerCase().contains("logs")) > new Random().nextInt(6 + 1) + 10 || Inventory.isFull()) 
        {
            return State.DROP;
        } 
        else 
        {
            return State.CUT;
        }
    }

    /**
     * The loop
     *
     * @return sleep
     */
    @Override
    public int onLoop() 
    {
        switch (getState()) 
        {
            case DROP:
                for (Item item : Inventory.all()) 
                {
                    if (item != null && item.getName().toLowerCase().contains("log")) 
                    {
                        Constants.setStatus("Drop logs");
                        Inventory.dropAll(Item -> Item != null && Item.getName().toLowerCase().contains("log"));
                    }
                }
                break;

            case CUT:
                for (GameObject Tree : GameObjects.all()) 
                {
                    // Tree = getGameObjects().closest(GameObject -> GameObject != null && GameObject.getName().equals(Constants.getTree()));
                    if (Tree != null && Tree.getName().equals(Constants.getTree()) && !getLocalPlayer().isAnimating()) 
                    {
                        sleep(Calculations.random(350, 2300));
                        Constants.setStatus("Chop " + Constants.getTree());
                        if (Tree.interact()) 
                        {
                            sleep(Calculations.random(300, 2200));
                            Mouse.moveMouseOutsideScreen();
                            sleepUntil(() -> !getLocalPlayer().isAnimating() || getLocalPlayer().distance(Tree) > 2, 60000);
                        }
                    }

                    // TO DROP INVENTORY WHEN NOT CUTTING THE TREE. BAD WAY TO DO THIS LOL
                    if (!getLocalPlayer().isAnimating()) 
                    {
                        for (Item item : Inventory.all()) 
                        {
                            if (item != null && item.getName().toLowerCase().contains("log")) 
                            {
                                Constants.setStatus("Drop logs");
                                Inventory.dropAll(Item -> Item != null && Item.getName().toLowerCase().contains("log"));
                            }
                        }
                    }
                }
                break;
        }
        return Calculations.random(1000, 2200);
    }

    /**
     * Paint
     * 
     * @param g1
     */
    @Override
    public void onPaint(Graphics2D g1) 
    {
        g1.setColor(Color.GREEN);
        g1.drawString("Runtime: " + Timer.formatTime(), 10, 35);
        g1.drawString("Status: " + Constants.getStatus(), 10, 65);
        g1.drawString("Woodcutting exp (p/h): " + SkillTracker.getGainedExperience(Skill.WOODCUTTING) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.WOODCUTTING) + ")", 10, 80);
        g1.drawString("Logs chopped (p/h): " + logsCount + "(" + Timer.getHourlyRate(logsCount) + ")", 10, 95);
        g1.drawString("Bird nest gained (p/h): " + birdnestCount + "(" + Timer.getHourlyRate(birdnestCount) + ")", 10, 110);
    }

    @Override
    public void onExit() 
    {
        log("Stopping!");
    }
}
