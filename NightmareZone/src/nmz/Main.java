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
package nmz;

import java.awt.Color;
import java.awt.Graphics2D;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.widgets.message.Message;

@ScriptManifest(
        author = "7ctx",
        name = "NMZ_Script",
        version = 1.0,
        description = "Nightmare-Zone Afker",
        category = Category.MINIGAME)

public class Main extends AbstractScript implements ChatListener 
{
    private Timer timer;
    private boolean entered = false;
    private boolean dreamCreated = false;
    private boolean banking = false;

    /**
     * Initialize
     */
    public void init() 
    {
        timer = new Timer();
        SkillTracker.start(Skill.STRENGTH);
        SkillTracker.start(Skill.ATTACK);
        SkillTracker.start(Skill.DEFENCE);
        SkillTracker.start(Skill.RANGED);
        SkillTracker.start(Skill.HITPOINTS);
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
     * Banking method
     */
    private void bank() 
    {
        banking = true;
        Bank.open(BankLocation.YANILLE);
        sleepUntil(() -> Bank.isOpen(), 2500);
        if (Bank.isOpen()) 
        {
            Bank.depositAllItems();
            sleepUntil(() -> Inventory.emptySlotCount() == 28, 2500);
            Bank.withdraw(Constants.Prayer_potion_4, 18);
            sleepUntil(() -> Inventory.count(Constants.Prayer_potion_4) == 18, 2500);
            Bank.withdraw(Constants.Overload_4, 10);
            sleepUntil(() -> Inventory.count(Constants.Overload_4) == 10, 2500);
            Bank.close();
            sleepUntil(() -> !Bank.isOpen(), 2500);
            banking = false;
        }
    }

    @Override
    public void onMessage(Message msg) 
    {
        if (msg.getMessage().contains("You wake up feeling refreshed.")) 
        {
            log("NMZ GAME ENDED");
            banking = true;
            this.dreamCreated = false;
            this.entered = false;
            this.banking = true;
            bank();
            log("Should Bank...");
        }
    }

    /**
     * Create NMZ dream method
     */
    private void createDream() 
    {
        if (inArea(Constants.NMZ_Yanille_Area) && !entered && !dreamCreated && !banking) 
        {
            NPC Dominic = NPCs.closest("Dominic Onion");
            Dominic.interact("Dream");
            sleep(Calculations.random(6000, 7500));
            Dialogues.clickOption(4);
            sleep(Calculations.random(1900, 2700));
            Dialogues.clickContinue();
            sleep(Calculations.random(2000, 3100));
            Dialogues.clickOption(1);
            sleep(Calculations.random(2200, 3200));
            Dialogues.clickContinue();
            dreamCreated = true;
            log("Created Dream...");
            sleep(Calculations.random(2100, 3400));
        }
    }

    /**
     * Enter NMZ dream method
     */
    private void enterDream() 
    {
        GameObject vial = GameObjects.closest(gameObject -> gameObject != null && gameObject.hasAction("Drink"));
        if (vial.interact()) 
        {
            sleep(Calculations.random(3000, 7000));
            Widgets.getWidgetChild(129, 6, 9).interact();
            entered = true;
            log("Entered Dream...");

        }
    }

    @Override
    public void onStart() 
    {
        init();
        log("Welcome to NMZ script by 7ctx.");
        log("If you experience any issues while running this script please report them to me on the forums.");
        log("Enjoy the script, gain some Combat levels!.");
    }

    private enum State 
    {
        BANK, WALK, DREAM, ENTER, DRINK_OVERLOAD, PROTECT_FROM_MELEE, DRINK_PRAYER_POT, SLEEP
    };

    private State getState() 
    {
        //if (getSkillTracker().getGainedExperience(Skill.STRENGTH) > 6500000) {
        //if (getSkills().getRealLevel(Skill.HITPOINTS) == 99 || timer.formatTime().toLowerCase().contains("04:00:15")) {
        // if (getSkills().getExperience(Skill.HITPOINTS) >= 13000020 || timer.formatTime().toLowerCase().contains("04:00:15")) {
        //log("Time to Stop...");
        //sleep(3000);
        //this.stop();
        //}
        
        if (getLocalPlayer().isInCombat()) 
        {
            dreamCreated = true;
            entered = true;
            banking = false;
        }
        
        if (!Inventory.contains(Constants.Prayer_potion_4)) 
        {
            log("State = BANK");
            return State.BANK;
        }
        
        if (getLocalPlayer().getHealthPercent() == 0) 
        {
            log("State = BANK");
            log("0 Hp");
            return State.BANK;
        }
        
        if (!inArea(Constants.NMZ_Yanille_Area) && !banking && !dreamCreated && !entered) 
        {
            log("State = WALK");
            return State.WALK;
        }
        
        if (inArea(Constants.NMZ_Yanille_Area) && !dreamCreated && !banking) 
        {
            log("State = DREAM");
            return State.DREAM;
        }
        
        if (!entered && dreamCreated && !banking) 
        {
            log("State = ENTER");
            return State.ENTER;
        }
        
        if (entered && dreamCreated && !banking && Skills.getBoostedLevels(Skill.HITPOINTS) > 90) 
        {
            sleep(Calculations.random(3000, 7000));
            log("State = DRINK_OVERLOAD");
            return State.DRINK_OVERLOAD;
        }

        if (entered && dreamCreated && !banking) 
        {
            if (!Prayers.isActive(Prayer.PROTECT_FROM_MELEE)) {
                log("State = PROTECT_FROM_MELEE");
                return State.PROTECT_FROM_MELEE;
            }
        }
        
        if (entered && dreamCreated && !banking && Skills.getBoostedLevels(Skill.PRAYER) < 13) 
        {
            sleep(Calculations.random(3000, 10000));
            log("State = DRINK_PRAYER_POT");
            return State.DRINK_PRAYER_POT;
        }
        return State.SLEEP;
    }

    @Override
    public int onLoop() 
    {
        switch (getState()) 
        {
            case BANK:
                bank();
                break;
                
            case DREAM:
                createDream();
                break;
                
            case WALK:
                if (Walking.walk(Constants.NMZ_Location_Tile)) 
                {
                    sleepUntil(() -> inArea(Constants.NMZ_Yanille_Area), 3700);
                }
                break;
                
            case ENTER:
                enterDream();
                break;
                
            case DRINK_OVERLOAD:
                Inventory.interact(item -> item != null && item.getName().contains("Overload"), "Drink");
                sleep(3000);
                break;
                
            case PROTECT_FROM_MELEE:
                if (!Tabs.isOpen(Tab.PRAYER)) 
                {
                    Tabs.open(Tab.PRAYER);
                    Prayers.toggle(true, Prayer.PROTECT_FROM_MELEE);
                }
                break;
                
            case DRINK_PRAYER_POT:
                Calculations.random(3000, 7500);
                Inventory.interact(item -> item != null && item.getName().contains("Prayer potion"), "Drink");
                sleep(3000);
                break;
                
            case SLEEP:
                sleep(Calculations.random(313, 1549));
                break;
        }
        return Calculations.random(300, 700);
    }

    @Override
    public void onPaint(Graphics2D g1) 
    {
        g1.setColor(Color.RED);
        g1.drawString("Runtime: " + timer.formatTime(), 10, 35);
        g1.drawString("Hitpoints exp (p/h): " + SkillTracker.getGainedExperience(Skill.HITPOINTS) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.HITPOINTS) + ")", 10, 65);
        g1.drawString("Attack exp (p/h): " + SkillTracker.getGainedExperience(Skill.ATTACK) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.ATTACK) + ")", 10, 80);
        g1.drawString("Strength exp (p/h): " + SkillTracker.getGainedExperience(Skill.STRENGTH) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.STRENGTH) + ")", 10, 95);
        g1.drawString("Defence exp (p/h): " + SkillTracker.getGainedExperience(Skill.DEFENCE) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.DEFENCE) + ")", 10, 110);
        g1.drawString("Ranged exp (p/h): " + SkillTracker.getGainedExperience(Skill.RANGED) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.RANGED) + ")", 10, 125);

    }
}
