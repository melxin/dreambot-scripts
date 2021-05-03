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
package magic.superheatpro;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.Category;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;

@ScriptManifest(
        author = "7ctx",
        name = "Superheat pro",
        version = 1.0,
        description = "Superheat Iron and Gold ore",
        category = Category.MAGIC)

public class Main extends AbstractScript 
{
    // Variables
    private Timer timer;
    private int superHeatsAmount;
    private int random;

    /**
     * Initialize
     */
    public void init() 
    {
        timer = new Timer();
        SkillTracker.start(Skill.MAGIC);
        SkillTracker.start(Skill.SMITHING);
        log("Initialized");

    }

    /**
     * Start
     */
    @Override
    public void onStart() 
    {
        init();
        log("Welcome to Superheat pro script by 7ctx.");
        log("To use this script, you need Nature runes in your inventory and Iron/Gold ores in bank.");

        java.awt.EventQueue.invokeLater(() -> 
        {
            new Ui().setVisible(true);
        });

        // SleepUntil Start Button was pressed in Frame
        if (!Ui.start) 
        {
            sleepUntil(() -> Ui.start, 9999999);
        }
    }

    /**
     * Stop
     */
    @Override
    public void onExit() 
    {
        log("Stopping Script..");
        log("Superheats: " + superHeatsAmount);
        log("Magic exp gained: " + SkillTracker.getGainedExperience(Skill.MAGIC));
        log("Smithing exp gained: " + SkillTracker.getGainedExperience(Skill.SMITHING));
    }

    /**
     * State enumeration
     */
    private enum State 
    {
        BANK,
        SUPERHEAT
    };

    /**
     * State getter
     */
    private State getState() 
    {
        // Stop at given magic lvl
        if (Skills.getBoostedLevels(Skill.MAGIC) == Ui.magicLvlToStopAt) 
        {
            log("Destination magic lvl reached: " + Skills.getBoostedLevels(Skill.MAGIC));
            this.stop();
        }

        // Stop at given smithing lvl
        if (Skills.getBoostedLevels(Skill.SMITHING) == Ui.smithingLvlToStopAt) 
        {
            log("Destination smithing lvl reached: " + Skills.getBoostedLevels(Skill.SMITHING));
            this.stop();
        }

        // Stop if there are no nature runes in inventory
        if (!Inventory.contains(Constants.getNatureRune())) 
        {
            log("Ran out of nature runes..");
            this.stop();
        }

        // Click mouse when spell is selected and out of ores
        if (!Inventory.contains(Constants.getOre()) && Magic.isSpellSelected()) 
        {
            int randomMousePointX;
            int randomMousePointY;
            randomMousePointX = new Random().nextInt((400 - 25) + 1) + 25;
            randomMousePointY = new Random().nextInt((450 - 100) + 1) + 100;
            Mouse.move(new Point(randomMousePointX, randomMousePointY));
            sleep(Calculations.random(600, 950));
            Mouse.click();
        }

        /**
         * Continue in dialogue
         */
        /* if (getDialogues().inDialogue()) 
              {
                  getDialogues().clickContinue();
              }*/
        
        // Bank state trigger
        if (!Inventory.contains(Constants.getOre()) && !Magic.isSpellSelected()) 
        {
            return State.BANK;
        }

        /**
         * Superheat state trigger
         */
        return State.SUPERHEAT;
    }

    /**
     * The mighty loop
     *
     * @return sleep
     */
    @Override
    public int onLoop() 
    {
        switch (getState()) 
        {

            // Handle bank
            case BANK:
                NPC banker = NPCs.closest(npc -> npc.getName().contains("Banker"));
                if (random >= 700) 
                {
                    banker.interactForceRight("Bank");
                    sleepUntil(() -> Bank.isOpen(), 5000);
                    sleep(Calculations.random(1019, 1289));
                } 
                else 
                {
                    if (random < 700) 
                    {
                        if (Bank.openClosest()) 
                        {
                            sleepUntil(() -> Bank.isOpen(), 5000);
                            sleep(Calculations.random(981, 1043));
                        }
                    }
                }

                if (Bank.isOpen() && !Bank.contains(Constants.getOre())) 
                {
                    log("Ran out of Ores");
                    this.stop();
                }

                //getBank().depositAllExcept(Constants.getNatureRune());
                if (Inventory.contains(Constants.getBar())) 
                {
                    Inventory.getRandom(Constants.getBar()).interact();
                    sleepUntil(() -> Inventory.emptySlotCount() >= 26, 5000);
                }
                if (Inventory.emptySlotCount() < 26) 
                {
                    Bank.depositAllExcept(Constants.getNatureRune());
                    sleepUntil(() -> Inventory.emptySlotCount() >= 26, 5000);
                }

                //sleep(Calculations.random(761, 1522));
                if (random >= 800) 
                {
                    Bank.withdrawAll(Constants.getOre());
                }
                if (random < 800) 
                {
                    Bank.withdraw(Constants.getOre(), 27);
                }
                sleepUntil(() -> Inventory.count(Constants.getOre()) >= 27, 1500);
                sleep(Calculations.random(733, 1122));
                Bank.close();
                sleepUntil(() -> !Bank.isOpen(), 5000);
                break;

            /**
             * Handle superheat
             */
            case SUPERHEAT:
                if (Magic.castSpell(Normal.SUPERHEAT_ITEM)) 
                {
                    sleep(Calculations.random(600, 870));
                }
                Item item = Inventory.get(Constants.getOre());
                Item itemRandom = Inventory.getRandom(Constants.getOre());
                random = new Random().nextInt(1000); // We use random as sort of antiban
                if (Magic.isSpellSelected()) 
                {
                    if (random <= 995) 
                    {
                        item.interact();
                        superHeatsAmount++;
                    } 
                    else 
                    {
                        if (random > 995) 
                        {
                            itemRandom.interact();
                            superHeatsAmount++;
                        }
                    }
                }
                break;
        }
        return Calculations.random(817, 917);
    }

    /**
     * Paint
     *
     * @param g
     */
    @Override
    public void onPaint(Graphics g) 
    {
        g.setColor(Color.CYAN);
        g.drawString("Runtime: " + timer.formatTime(), 10, 35);
        g.drawString("State: " + getState(), 10, 60);
        g.drawString("Superheats (p/h): " + superHeatsAmount + "(" + timer.getHourlyRate(superHeatsAmount) + ")", 10, 75);
        g.drawString("Magic exp (p/h): " + SkillTracker.getGainedExperience(Skill.MAGIC) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.MAGIC) + ")", 10, 90);
        g.drawString("Smithing exp (p/h): " + SkillTracker.getGainedExperience(Skill.SMITHING) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.SMITHING) + ")", 10, 105);
        g.drawString("Random number : " + random, 10, 125);
    }
}
