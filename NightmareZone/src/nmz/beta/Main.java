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
package nmz.beta;

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
        name = "NMZ_Script-Beta",
        version = 2.0,
        description = "Nightmare-Zone Afker",
        category = Category.MINIGAME)

public class Main extends AbstractScript implements ChatListener
{
    private Timer timer;
    private boolean createdDream;

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
     * Banking method
     */
    private void bank() 
    {
        Bank.open(BankLocation.YANILLE);
        NPCs.closest("Banker").interactForceRight("Bank");
        sleepUntil(() -> Bank.isOpen(), 2500);
        if (Bank.isOpen()) 
        {
            Bank.depositAllItems();
            sleepUntil(() -> Inventory.emptySlotCount() == 28, 2500);
            Bank.withdraw(Constants.Prayer_potion_4, Constants.Prayer_potion_4_count);
            sleepUntil(() -> Inventory.count(Constants.Prayer_potion_4) == Constants.Prayer_potion_4_count, 2500);
            Bank.withdraw(Constants.Overload_4, Constants.Prayer_potion_4_count);
            sleepUntil(() -> Inventory.count(Constants.Overload_4) == Constants.Prayer_potion_4_count, 2500);
            Bank.close();
            sleepUntil(() -> !Bank.isOpen(), 2500);
        }
    }

    @Override
    public void onMessage(Message msg) 
    {
        if (msg.getMessage().contains("You wake up feeling refreshed.")) 
        {
            bank();
            log("Should Bank...");
        }
    }

    /**
     * NMZ Dream method
     */
    private void Dream() 
    {
        NPC Dominic = NPCs.closest("Dominic Onion");
        Dominic.interact("Dream");
        sleep(Calculations.random(2000, 4000));
        Dialogues.clickOption(4);
        sleep(Calculations.random(1900, 2700));
        Dialogues.clickContinue();
        sleep(Calculations.random(2000, 3100));
        Dialogues.clickOption(1);
        log("Created Dream...");
        createdDream = true;
        sleep(Calculations.random(2100, 3400));
        GameObject vial = GameObjects.closest(gameObject -> gameObject != null && gameObject.hasAction("Drink"));
        vial.interact();
        sleep(Calculations.random(3000, 7000));
        Widgets.getWidgetChild(129, 6, 9).interact();
        log("Entered Dream...");
        createdDream = false;
    }

    @Override
    public void onStart() 
    {
        init();
        log("Welcome to NMZ script by 7ctx.");
        log("If you experience any issues while running this script please report them to me on the forums.");
        log("Enjoy the script, gain some Combat levels!.");
    }

    /**
     * Is in area boolean
     */
    private boolean inArea(Area area) 
    {
        return area.contains(getLocalPlayer().getTile());
    }

    private enum State 
    {
        BANK, WALK, DREAM, PRAYER, DRINK_POT, SLEEP
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
        
        if (!Inventory.contains(Constants.Prayer_potion_4) || !Inventory.contains(Constants.Overload_4)) 
        {
            log("State = BANK");
            return State.BANK;
        }

        if (!inArea(Constants.NMZ_Dream_Area) 
                && !inArea(Constants.NMZ_Yanille_Area)
                && Inventory.count(Constants.Overload_4) == Constants.Overload_4_count 
                && Inventory.count(Constants.Prayer_potion_4) == Constants.Prayer_potion_4_count) 
        {
            log("State = WALK");
            return State.WALK;
        }

        if (inArea(Constants.NMZ_Yanille_Area) 
                && Inventory.count(Constants.Overload_4) > 5 
                || Inventory.count(Constants.Prayer_potion_4) >= 10 
                && !createdDream) 
        {
            log("State = DREAM");
            return State.DREAM;
        }

        if (!Prayers.isActive(Prayer.PROTECT_FROM_MELEE) && inArea(Constants.NMZ_Dream_Area)) 
        {
            log("State = PROTECT_FROM_MELEE");
            return State.PRAYER;
        }
        
        if (Skills.getBoostedLevels(Skill.PRAYER) < 13 || Skills.getBoostedLevels(Skill.HITPOINTS) > 90) 
        {
            sleep(Calculations.random(3000, 10000));
            log("State = DRINK_POT");
            return State.DRINK_POT;
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
                
            case WALK:
                Walking.walk((Constants.NMZ_Location_Tile).getRandomizedTile(3));
                sleepUntil(() -> inArea(Constants.NMZ_Yanille_Area), 2700);
                break;
                
            case DREAM:
                Dream();
                break;

            case PRAYER:
                if (!Tabs.isOpen(Tab.PRAYER)) 
                {
                    Tabs.open(Tab.PRAYER);
                    Prayers.toggle(true, Prayer.PROTECT_FROM_MELEE);
                }
                break;
                
            case DRINK_POT:
                if (Skills.getBoostedLevels(Skill.HITPOINTS) > 90) 
                {
                    Inventory.interact(item -> item != null && item.getName().contains("Overload"), "Drink");
                    //sleep(3000);
                }
                
                if (Skills.getBoostedLevels(Skill.PRAYER) < 13) 
                {
                    Calculations.random(3000, 7500);
                    Inventory.interact(item -> item != null && item.getName().contains("Prayer potion"), "Drink");
                    //sleep(3000);
                }
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
