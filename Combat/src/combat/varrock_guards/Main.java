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
package combat.varrock_guards;

import static combat.varrock_guards.Constants.Bones;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.widgets.message.Message;

@ScriptManifest(
        author = "7ctx",
        name = "Varrock_Guards_Killer",
        version = 1.0,
        description = "Kills Varrock Guards at Varrock Castle.",
        category = Category.COMBAT)

public class Main extends AbstractScript implements ChatListener 
{
    private Timer timer;
    private int npc_kills = 0;
    private int used_food = 0;
    private int food_in_bank = 0;
    private int bones_buried = 0;

    /**
     * Initialize
     */
    public void init() 
    {
        timer = new Timer();
        SkillTracker.start(Skill.HITPOINTS);
        SkillTracker.start(Skill.STRENGTH);
        SkillTracker.start(Skill.ATTACK);
        SkillTracker.start(Skill.DEFENCE);
        SkillTracker.start(Skill.MAGIC);
        SkillTracker.start(Skill.RANGED);
        SkillTracker.start(Skill.PRAYER);
        log("Initialized");
    }

    /**
     * Start
     */
    @Override
    public void onStart() 
    {
        java.awt.EventQueue.invokeLater(() -> 
        {
            new Jframe().setVisible(true);
        });
        init();
        log("Welcome to Varrock Guards Killer script by 7ctx.");
        log("If you experience any issues while running this script please report them to me on the forums.");
        log("Enjoy the script, gain some Combat levels!.");
    }

    /**
     * Exit
     */
    @Override
    public void onExit() 
    {
        log("Stopping Varrock Guards Killer Script...");
    }

    /**
     * Message received
     *
     * @param msg
     */
    @Override
    public void onMessage(Message msg) 
    {
        if (msg.getMessage().contains("There is no ammo left in your quiver.")) 
        {
            log("There is no ammo left in your quiver.");
            Walking.walkExact(new Tile(3212, 3438, 0));
            sleep(5000);
            this.stop();
        }
    }

    /**
     * inArea boolean
     */
    private boolean inArea(Area area) 
    {
        return area.contains(getLocalPlayer().getTile());
    }

    /**
     * State enumeration
     */
    private enum State 
    {
        BANK, WALK, EAT, FIGHT, SLEEP
    };

    /**
     * State getter
     *
     * @return State
     */
    private State getState() 
    {
        // Bury bones
        if (Constants.bury_bones && Inventory.contains(Item -> Item != null && Item.getName().contains("Bones"))) 
        {
            Inventory.interact("Bones", "Bury");
            bones_buried++;
        }

        // Stop when running out of food..
        if (!Inventory.contains(Constants.food) && Constants.food_enabled) 
        {
            log("You ran out of food.. Going to bank...");
            return State.BANK;
        }

        // Continue in dialogue
        if (Dialogues.inDialogue()) 
        {
            Dialogues.clickContinue();
        }

        // Walk
        if (!inArea(Constants.Varrock_Castle) && Inventory.contains(Constants.food)) 
        {
            return State.WALK;
        }

        // Eat
        if (getLocalPlayer().getHealthPercent() < 40) {
            return State.EAT;
        }

        // Fight
        if (inArea(Constants.Varrock_Castle)) 
        {
            //log("In Varrock Castle Area!");
            if (NPCs.closest(Constants.NPC).isOnScreen()
                    && !NPCs.closest(Constants.NPC).isInCombat()
                    && !getLocalPlayer().isInCombat()
                    && getLocalPlayer().getHealthPercent() >= 40) 
            {
                sleep(Calculations.random(1017, 2117));
                return State.FIGHT;
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
            case BANK:
                Bank.open(BankLocation.VARROCK_WEST);
                sleepUntil(() -> Bank.isOpen(), Calculations.random(3207, 4017));
                if (Bank.isOpen() && !Bank.contains(Constants.food)) 
                {
                    log("No food in bank! Stopping...");
                    this.stop();
                } 
                else 
                {
                    if (Bank.isOpen()) 
                    {
                        //getBank().depositAllItems();
                        //sleepUntil(() -> getInventory().fullSlotCount() == 0, 5000);
                        Bank.withdraw(Constants.food, Constants.food_amount);
                        //sleepUntil(() -> getInventory().count(Constants.food) == Constants.food_amount, 5000);
                        sleepUntil(() -> Inventory.contains(Constants.food), 5000);
                        food_in_bank = Bank.count(Constants.food);
                        // getBank().close();                
                    }
                }
                break;

            case WALK:
                Walking.walk(new Tile(3212, 3460, 0).getRandomizedTile(1));
                log("Walking to Varrock Castle...");
                sleep(Calculations.random(1777, 3477));
                break;

            case FIGHT:
                Bones = GroundItems.closest(Item -> Item != null && Item.getName().contains("Bones"));
                if (Constants.bury_bones && Bones != null && getLocalPlayer().getTile().distance(Bones) <= 2) 
                {
                    Bones.interactForceRight("Take");
                    sleepUntil(() -> Bones == null, 5000);
                }
                NPC npc = NPCs.closest(Constants.NPC);
                int random = new Random().nextInt(1000);
                log("Random Number: " + random);
                if (random <= 950 && npc.interact()) 
                {
                    log("Left-Click Attacking " + npc.getName());
                    sleepUntil(() -> !npc.isInteractedWith(), 10000);
                    npc_kills++;
                    sleep(Calculations.random(897, 1377));
                } 
                else 
                {
                    if (random > 950 && npc.interactForceRight("Attack")) 
                    {
                        log("Right-Click Attacking " + npc.getName());
                        sleepUntil(() -> !npc.isInteractedWith(), 10000);
                        npc_kills++;
                        sleep(Calculations.random(997, 1777));
                    }
                }
                break;

            case EAT:
                if (Inventory.getRandom(Constants.food).interact()) 
                {
                    log("Eating: " + Constants.food);
                    used_food++;
                    sleep(Calculations.random(1177, 1307));
                }
                break;

            case SLEEP:
                Calculations.random(277, 577);
                break;
        }
        return Calculations.random(777, 2777);
    }

    /**
     * Paint
     *
     * @param g
     */
    @Override
    public void onPaint(Graphics g) 
    {
        g.setColor(Color.WHITE);
        g.drawString("Runtime: " + timer.formatTime(), 10, 35);
        g.drawString("Kills: " + npc_kills, 10, 50);
        g.drawString("Used food: " + used_food, 10, 65);
        g.drawString("Food left in bank: " + food_in_bank, 10, 80);
        g.drawString("Hitpoints exp (p/h): " + SkillTracker.getGainedExperience(Skill.HITPOINTS) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.HITPOINTS) + ")", 10, 95);
        g.drawString("Strength exp (p/h): " + SkillTracker.getGainedExperience(Skill.STRENGTH) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.STRENGTH) + ")", 10, 110);
        g.drawString("Attack exp (p/h): " + SkillTracker.getGainedExperience(Skill.ATTACK) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.ATTACK) + ")", 10, 125);
        g.drawString("Defence exp (p/h): " + SkillTracker.getGainedExperience(Skill.DEFENCE) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.DEFENCE) + ")", 10, 140);
        g.drawString("Magic exp (p/h): " + SkillTracker.getGainedExperience(Skill.MAGIC) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.MAGIC) + ")", 10, 155);
        g.drawString("Ranged exp (p/h): " + SkillTracker.getGainedExperience(Skill.RANGED) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.RANGED) + ")", 10, 170);
        if (Constants.bury_bones) 
        {
            g.drawString("Prayer exp (p/h): " + SkillTracker.getGainedExperience(Skill.PRAYER) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.PRAYER) + ")", 10, 185);
            g.drawString("Bones Buried: " + bones_buried, 10, 200);
        }
    }
}
