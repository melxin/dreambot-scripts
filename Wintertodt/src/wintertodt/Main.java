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
package wintertodt;

import java.awt.Color;
import java.awt.Graphics2D;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.message.Message;

@ScriptManifest(
        author = "7ctx",
        name = "Wintertodt",
        version = 1.0,
        description = "Wintertodt Private",
        category = Category.MINIGAME)

public class Main extends AbstractScript implements ChatListener 
{
    private Timer timer;
    boolean banking;
    boolean cutting;
    private boolean gameStarted;
    private boolean fletching;
    private int winStreak = 0;
    private int loseStreak = 0;
    //private Tile myLocation = getLocalPlayer().getTile();

    public void init() 
    {
        timer = new Timer();
        SkillTracker.start(Skill.FIREMAKING);
        SkillTracker.start(Skill.WOODCUTTING);
        SkillTracker.start(Skill.FLETCHING);
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
     * Handle Bank method here
     */
    private void bank() 
    {
        banking = true;
        Walking.walk(BankLocation.WINTERTODT.getCenter());
        Bank.open(BankLocation.WINTERTODT);
        GameObject bank = GameObjects.closest(gameObject -> gameObject != null && gameObject.hasAction("Bank"));
        bank.interactForceRight("Bank");
        log("STATUS = BANK");
        sleepUntil(() -> Bank.open(), 5000);
        //sleep(Calculations.random(2176, 2347));
        //getBank().depositAllExcept(Constants.KNIFE);

        if (Bank.isOpen()) 
        {
            sleep(900);
            Bank.depositAllExcept(item -> item != null && item.getName().equals("Tinderbox") || item.getName().toLowerCase().endsWith("axe") || item.getName().equals("Knife"));
            //getBank().depositAllExcept(item -> item != null && item.getName().toLowerCase().contains("tinderbox") && item.getName().toLowerCase().contains("axe") && item.getName().toLowerCase().contains("knife"));
            //sleep(Calculations.random(1100, 1230));
            sleepUntil(() -> Inventory.emptySlotCount() == 25, 5000);
            Bank.withdraw(Constants.food, Constants.food_amount);
            sleep(Calculations.random(1400, 1900));
        }
        if (Inventory.count(Constants.food) == Constants.food_amount) 
        {
            Bank.close();
            banking = false;
        }
    }

    /**
     * Handles Game Messages received
     * 
     * @param msg
     */
    @Override
    public void onMessage(Message msg) 
    {
        if (msg.getMessage().contains("Your inventory is too full to hold any more roots.")) 
        {
            //log("STATUS = FLETCH");
            //fletching = true;
        }

        if (msg.getMessage().contains("You did not earn enough points to be worthy of a gift from the citizens of Kourend this time.")) 
        {
            loseStreak++;
            //log("STATUS = gameEnded without enough points");
            //gameEnd = true;
        }

        if (msg.getMessage().contains("You have gained a supply crate!")) 
        {
            //log("STATUS = gameEnded" + "Supply crate = true");
            //gameEnd = true;
        }

        if (msg.getMessage().contains("The cold of the Wintertodt seeps into your bones.") && cutting) 
        {
            //log("Attempt to cut again");
            //cut(); //ugly way to do this but fuckit im lazy
        }

        if (msg.getMessage().contains("The freezing cold attack of the Wintertodt's magic hits you.")) 
        {
            //feed = true;
        }
    }

    /**
     * Enter Wintertodt area method
     */
    private void enterWintertodt() 
    {
        log("STATUS = ENTER");
        Walking.walk(Constants.DOOR_LOCATION.getRandomizedTile());
        GameObject door = GameObjects.closest(gameObject -> gameObject != null && gameObject.hasAction("Enter"));
        sleepUntil(() -> door.isOnScreen(), 3500);
        if (door.interact()) 
        {
            sleep(Calculations.random(3000, 3500));
            Walking.walk(Constants.TREE_LOCATION.getRandomizedTile()); // WALK TO TREE
            sleep(Calculations.random(3000, 3200));
            if (inArea(Constants.Wintertodt)) 
            {
                log("Entered Wintertodt Area");
            }
        }
    }

    /**
     * Leave wintertodt area method
     */
    private void leaveWintertodt() 
    {
        log("STATUS = LEAVE");
        sleep(Calculations.random(980, 1320));
        Walking.walk(Constants.LEAVE_LOCATION.getRandomizedTile());
        GameObject door = GameObjects.closest(gameObject -> gameObject != null && gameObject.hasAction("Enter"));
        sleepUntil(() -> door.isOnScreen(), 3500);
        if (door.interact()) 
        {
            sleepUntil(() -> !inArea(Constants.Wintertodt), 5000);
            if (!inArea(Constants.Wintertodt)) 
            {
                log("Succesfully left Wintertodt Area");
            }
        }
    }

    /**
     * Cuts the tree inside wintertodt area method
     */
    private void cut() 
    {
        if (!getLocalPlayer().isAnimating()) 
        {
            GameObject tree = GameObjects.closest(gameObject -> gameObject != null && gameObject.hasAction("Chop"));
            if (tree.interact()) 
            {
                log("STATUS = CUTTING");
                sleepUntil(() -> !getLocalPlayer().isAnimating(), 14000);
                //sleep(Calculations.random(4000, 5000));
            }
        }
    }

    /**
     * Fletching method
     */
    private void fletch() 
    {
        Inventory.get(Constants.knife).useOn(Constants.roots);
        sleep(Calculations.random(3500, 4000));
        log("STATUS = FLETCHING");
        sleepUntil(() -> !getLocalPlayer().isAnimating(), 12000);
    }

    @Override
    public void onStart() 
    {
        init();
        log("Welcome to Wintertodt script by 7ctx.");
        log("If you experience any issues while running this script please report them to me on the forums.");
    }

    private enum State 
    {
        BANK, ENTER, CUT, FLETCH, FEED, LEAVE, SLEEP
    };

    private State getState() 
    {
        // Make Sure we dont die..
        if (!Inventory.contains(Constants.food) && inArea(Constants.Wintertodt) && (Skills.getBoostedLevels(Skill.HITPOINTS) < 30)) 
        {
            leaveWintertodt();
            sleep(Calculations.random(4500, 5000));
            this.stop();
        }

        // Eat food here..
        if (Skills.getBoostedLevels(Skill.HITPOINTS) < 40) 
        {
            Inventory.interact(item -> item != null && item.getName().contains("Shark"), "Eat");
            //if (cutting) {
            //return State.CUT;
            //} 
        }

        // do this for banking
        if (Inventory.count(Constants.food) < Constants.food_amount && !inArea(Constants.Wintertodt) && !Bank.isOpen() || !inArea(Constants.Wintertodt) && Inventory.emptySlotCount() < 18) 
        {
            return State.BANK;
        }

        // Check if you are in wintertodt area, if not enter after banking.
        if (!inArea(Constants.Wintertodt)) 
        {
            log("Not in Area");
            cutting = false;
            fletching = false;
            //feed = false;        
            if (!banking) 
            {
                return State.ENTER;
            }
        }

        // Check if game has started..
        if (inArea(Constants.Wintertodt) && !Widgets.getWidgetChild(396, 3).getText().contains("The Wintertodt returns")) 
        {
            log("gameStarted = true");
            gameStarted = true;
        } 
        else 
        {
            gameStarted = false;
        }
        
        // Handle Stuff inside wintertodt DO YOU NEED CUTTING? OR DO YOU NEED FLETCHING? IF NONE PASS ON TO FEEDING BRAZIER CASE
        if (inArea(Constants.Wintertodt) && gameStarted) 
        {
            log("In Area");
            if (!fletching && !Inventory.contains(Constants.kindling) && Inventory.count(Constants.roots) < Constants.roots_amount) 
            { //9
                cutting = true;
            } 
            else 
            {
                if (Inventory.count(Constants.roots) > Constants.roots_amount) 
                { //8
                    cutting = false;
                    fletching = true;
                } 
                else 
                {
                    if (Inventory.count(Constants.kindling) > Constants.kindling_amount) 
                    {
                        fletching = false;
                    }
                }
            }

            if (cutting) 
            {
                return State.CUT;
            }

            if (fletching) 
            {
                return State.FLETCH;
            }

            if (!cutting && !fletching && Inventory.contains(Constants.kindling)) //feed = true;
            {
                return State.FEED;
            }
        }
        
        // Check if game has Ended & Leave
        if (inArea(Constants.Wintertodt) 
                && Widgets.getWidgetChild(396, 3).getText().contains("The Wintertodt returns") 
                && Inventory.count(Constants.food) < Constants.food_amount
                || inArea(Constants.Wintertodt) 
                && Widgets.getWidgetChild(396, 3).getText().contains("The Wintertodt returns") 
                && Inventory.contains(Constants.rewardbox)) 
        {
            return State.LEAVE;
        }

        return State.SLEEP;
    }

    @Override
    public int onLoop() 
    {
        switch (getState()) 
        {
            case BANK:
                if (Inventory.contains(Constants.rewardbox)) 
                {
                    winStreak++;
                    Inventory.get(Constants.rewardbox).interact("Open");
                    sleepUntil(() -> !Inventory.contains(Constants.rewardbox), 2000);
                }
                bank();
                break;
                
            case ENTER:
                enterWintertodt();
                break;
                
            case CUT:
                cut();
                break;
                
            case FLETCH:
                fletch();
                break;
                
            case FEED:
                GameObject brazier = GameObjects.closest(gameObject -> gameObject != null && gameObject.hasAction("Feed"));
                if (brazier.interact()) 
                {
                    log("Feed brazier");
                    sleepUntil(() -> !Inventory.contains(Constants.kindling), 5500);
                    GameObject unlitbrazier = GameObjects.closest(gameObject -> gameObject != null && gameObject.hasAction("Light"));
                    if (unlitbrazier.interact()) 
                    {
                        log("Light brazier");
                        sleepUntil(() -> !getLocalPlayer().isAnimating(), 888);
                    }
                }
                break;
                
            case LEAVE:
                leaveWintertodt();
                break;
                
            case SLEEP:
                sleep(Calculations.random(277, 399));
                break;
        }
        return Calculations.random(250, 450);
    }

    @Override
    public void onPaint(Graphics2D g1) 
    {
        g1.setColor(Color.RED);
        //g.drawString("State: " + getState().toString(), 10, 35);
        g1.drawString("Runtime: " + timer.formatTime(), 10, 175); //35
        g1.drawString("Won Games (p/h): " + winStreak + "(" + timer.getHourlyRate(winStreak) + ")", 10, 190);
        g1.drawString("Lost Games (p/h): " + loseStreak + "(" + timer.getHourlyRate(loseStreak) + ")", 10, 205);
        g1.drawString("Firemaking exp (p/h): " + SkillTracker.getGainedExperience(Skill.FIREMAKING) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.FIREMAKING) + ")", 10, 220); //65
        g1.drawString("Woodcutting exp (p/h): " + SkillTracker.getGainedExperience(Skill.WOODCUTTING) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.WOODCUTTING) + ")", 10, 235); //80
        g1.drawString("Fletching exp (p/h): " + SkillTracker.getGainedExperience(Skill.FLETCHING) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.FLETCHING) + ")", 10, 250); //95
    }
}
