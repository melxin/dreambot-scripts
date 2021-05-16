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
package antiban.thieving.master_farmer;

import antiban.mouse.algorithm.WindMouse;
import static antiban.thieving.master_farmer.Constants.Master_Farmer;
import java.awt.Color;
import java.awt.Graphics2D;
import org.dreambot.api.Client;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.message.Message;

@ScriptManifest(
        author = "7ctx",
        name = "Thieving_Master_Farmer_with_antiban",
        version = 1.0,
        description = "Pickpocket Master Farmers",
        category = Category.THIEVING)

public class Main extends AbstractScript implements ChatListener 
{
    private Timer timer;
    //private PricedItem crate = null;
    //private Tile myLocation = getLocalPlayer().getTile();
    private boolean banking;

    public void init() 
    {
        Client.getInstance().setMouseMovementAlgorithm(new WindMouse());
        timer = new Timer();
        //crate = new PricedItem("Supply crate",getClient().getMethodContext(), false);
        SkillTracker.start(Skill.THIEVING);
        log("Initialized");

    }

    /**
     * inArea boolean
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
        Walking.walk(BankLocation.DRAYNOR.getCenter());
        Bank.open(BankLocation.DRAYNOR);
        GameObject bank = GameObjects.closest(gameObject -> gameObject != null && gameObject.hasAction("Bank"));
        bank.interactForceRight("Bank");
        log("STATUS = BANK");
        sleepUntil(() -> Bank.open(), 5000);
        //sleep(Calculations.random(2176, 2347));
        //getBank().depositAllExcept(Constants.KNIFE);

        if (Bank.isOpen()) 
        {
            sleep(900);
            Bank.depositAllItems();
            //getBank().depositAllExcept(item -> item != null && item.getName().equals("Tinderbox") || item.getName().equals("Dragon axe") || item.getName().equals("Knife"));
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

   /*
    * Handles Game Messages received
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
        log("Welcome to Thieving (master farmer) script by 7ctx.");
        log("If you experience any issues while running this script please report them to me on the forums.");
    }

    private enum State 
    {
        THIEF, DROP, BANK, SLEEP
    };

    private State getState() 
    {
        // Eat food
        if (getLocalPlayer().getHealthPercent() <= 45) 
        {
            Inventory.interact(item -> item != null && item.getName().contains("Tuna"), "Eat");
        }
        
        // Bank when out of food
        if (!Inventory.contains(Constants.food)) 
        {
            return State.BANK;
        }
        
        if (Inventory.isFull()) 
        {    
            return State.DROP;
        } 
        else 
        {
            Master_Farmer = NPCs.closest("Master Farmer"); // MASTER FARMER
            if (Master_Farmer.hasAction("Pickpocket")) 
            {
                return State.THIEF;
            }
        }
        
        return State.SLEEP;
    }

    @Override
    public int onLoop() 
    {
        switch (getState()) 
        {
            case THIEF:
                if (Master_Farmer != null) 
                {
                    Master_Farmer.interact("Pickpocket");
                    sleep(Calculations.random(900, 1500));
                    if (getLocalPlayer().isHealthBarVisible()) 
                    {
                        sleep(Calculations.random(1500, 2000));
                        //sleepUntil(() -> !getLocalPlayer().isHealthBarVisible(), 5000);
                    }
                }
                break;
                
            case DROP:
                Inventory.dropAllExcept(item -> item != null
                        && item.getName().equals("Toadflax seed")
                        || item.getName().equals("Kwuarm seed")
                        || item.getName().equals("Ranarr seed")
                        || item.getName().equals("Dwarf weed seed")
                        || item.getName().equals("Snapdragon seed")
                        || item.getName().equals("Oak seed")
                        || item.getName().equals("Willow seed")
                        || item.getName().equals("Maple seed")
                        || item.getName().equals("Teak seed")
                        || item.getName().equals("Yew seed")
                        || item.getName().equals("Magic seed")
                        || item.getName().equals("Torstol seed")
                        || item.getName().equals("Shark")
                        || item.getName().equals("Tuna"));
                break;
                
            case BANK:
                bank();
                break;
                
            case SLEEP:
                sleep(Calculations.random(33, 99));
                break;
        }
        return Calculations.random(7, 77);
    }

    @Override
    public void onPaint(Graphics2D g1) 
    {
        g1.setColor(Color.RED);
        g1.drawString("Runtime: " + timer.formatTime(), 10, 35);
        g1.drawString("Thieving exp (p/h): " + SkillTracker.getGainedExperience(Skill.THIEVING) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.THIEVING) + ")", 10, 65);

    }
}
