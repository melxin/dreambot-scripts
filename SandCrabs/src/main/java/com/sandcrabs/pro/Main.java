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
package com.sandcrabs.pro;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.widgets.message.Message;

@ScriptManifest(
        author = "7ctx",
        name = "SandCrabs_Afker_PRO",
        version = 1.0,
        description = "Afk at Sand Crabs, start script at tile between 3 Sand Crabs",
        category = Category.COMBAT)

public class Main extends SandCrabs implements ChatListener 
{
    private final Timer timer = new Timer();

    /**
     * Start script
     */
    @Override
    public void onStart() 
    {
        super.onStart();

        Client.getInstance().setMouseMovementAlgorithm(new WindMouse()); // Set custom mouse movement algorithm
        
        setUseFood(true); // Enable the use of food
        log("setUseFood = true");
        
        setFoodName("Tuna"); // food to use
        log("foodName = " + getFoodName());
        
        setFoodAmount(27); // Food amount to take from bank
        log("setFoodAmount = " + getFoodAmount());
        
        setTrainingSkill(Skill.STRENGTH); // Set skill to train
        log("setTrainingSkill = " + getTrainingSkill().getName());
        
        SkillTracker.start(Skill.HITPOINTS); // Start skill tracker for hitpoints
        SkillTracker.start(getTrainingSkill()); // Start skill tracker for trainingSkill

        setAfkTile(getLocalPlayer().getTile()); // Set tile to afk at
        log("setAfkTile = " + getLocalPlayer().getTile());
        
        Area aggroArea = new Area(getAfkTile().getX() + 40, getAfkTile().getY(), getAfkTile().getX() + 50, getAfkTile().getY() + 12);
        setAggroResetArea(aggroArea); // Set aggro area
        log("setAggroResetArea = " + aggroArea);
        
        setSpecialAttackEnabled(false); // Enable/Disable special attack
        log("setSpecialAttackEnabled = " + isSpecialAttackEnabled());
        
        log("Initialized");

        log("Welcome to Sand Crabs Afker script by 7ctx.");
        log("If you experience any issues while running this script please report them to me on the forums.");
    }

    /**
     * Message received
     *
     * @param msg
     */
    @Override
    public void onMessage(Message msg) 
    {
        if (msg.getMessage().equalsIgnoreCase("Oh dear, you are dead!")) 
        {
            log("Oh dear, you are dead!");
            setStopScript(true);
            this.stop();
        }

        if (msg.getMessage().contains("There is no ammo left in your quiver.")) 
        {
            log("There is no ammo left in your quiver!");
            setStopScript(true);
        }
    }

    /**
     * Is in area boolean
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
        BANK, WALK_TO_CRABS, RESET_AGGRO, EAT_FOOD, STOP_SCRIPT, SLEEP
    };

    /**
     * State getter
     *
     * @return State
     */
    private State getState() 
    {
        // Stop at given level
        int stopLvl = 70;
        if (isStopScript() || Client.getGameState() == GameState.LOGIN_SCREEN || Skills.getBoostedLevels(getTrainingSkill()) == stopLvl)
        {
          log("Stopping script..");
          return State.STOP_SCRIPT;
        }
        
        // Loot clue scrolls
        GroundItem clue = GroundItems.closest(Item -> Item != null && Item.getName().toLowerCase().contains("clue") && Item.isOnScreen());
        if (clue != null) 
        {
            clue.interact();
        }
        
        // Toggle auto retalitate on
        if (!Combat.isAutoRetaliateOn()) 
        {
            log("Toggle auto retalitate to on");
            Combat.toggleAutoRetaliate(true);
        }

        // Toggle Special attack
        if (isSpecialAttackEnabled() && Combat.getSpecialPercentage() >= Calculations.random(75, 90)) 
        {
            if (getLocalPlayer().isInCombat()) 
            {
                Combat.toggleSpecialAttack(true);
            }
        }
        
        // Bank
        if (isUseFood() && !Inventory.contains(Item -> Item != null && Item.getName().contains(getFoodName()))) 
        {
            log("Banking..");
            return State.BANK;
        }

        // Eat food
        int currentHealtPercent = getLocalPlayer().getHealthPercent();
        if (isUseFood() && currentHealtPercent <= 45 && Inventory.contains(Item -> Item != null && Item.getName().contains(getFoodName()))) 
        {
            log("Eat food: " + getFoodName() + " at hp: " + Skills.getBoostedLevels(Skill.HITPOINTS) + " | " + currentHealtPercent + "%");
            return State.EAT_FOOD;
        } 
        else 
        {
            if (currentHealtPercent <= 35) 
            {
                return State.STOP_SCRIPT;
            }
        }

        // Walk to afk tile
        if (Inventory.contains(Item -> Item != null && Item.getName().contains(getFoodName()) 
                && getLocalPlayer().distance(getAfkTile()) > 0 
                && !isResetAggro()) 
                && !isStopScript()) 
        {
            log("Walking to afk tile: " + getAfkTile());
            return State.WALK_TO_CRABS;
        }

        // Reset aggro
        List<NPC> sandCrabs = NPCs.all(npc -> npc != null && npc.getName().equals("Sand Crab") && npc.distance(getLocalPlayer()) <= 3 && npc.distance(getAfkTile()) <= 3);
        if (!isStopScript()) 
        {
            if (getLocalPlayer().distance(getAfkTile()) <= 1 && !getLocalPlayer().isInCombat()) 
            {
                Instant start = Instant.now();
                sleep(Calculations.random(7000, 10000));
                Instant finish = Instant.now();
                if (finish.isAfter(start)) 
                {
                    log("Aggro timer elapsed: " + Duration.between(start, finish).getSeconds() + 's');
                    setResetAggro(true);
                }
            }

            if (sandCrabs == null || sandCrabs.isEmpty() || isResetAggro()) 
            {
                log("Reset aggro..");
                return State.RESET_AGGRO;
            }
        }
        
        log("Sleeping..");
        return State.SLEEP;
    }

    /**
     * Loop
     *
     * @return Calculations.random
     */
    @Override
    public int onLoop() 
    {
        switch (getState()) 
        {
            case BANK:
                super.doBanking();
                break;

            case WALK_TO_CRABS:
                Walking.walkExact(getAfkTile());
                sleepUntil(() -> getLocalPlayer().getTile() == getAfkTile(), Calculations.random(1750, 2750));
                break;

            case RESET_AGGRO:
                setResetAggro(true);
                Walking.walk(getAggroResetArea().getRandomTile());
                sleepUntil(() -> inArea(getAggroResetArea()), Calculations.random(3500, 5000));
                if (inArea(getAggroResetArea())) 
                {
                    setResetAggro(false);
                }
                break;

            case EAT_FOOD:
                //Inventory.interact(item -> item != null && item.getName().contains(getFoodName()), "Eat");
                Inventory.getRandom(item -> item != null && item.getName().contains(getFoodName())).interact();
                break;
                
            case STOP_SCRIPT:
                if (!isStopScript()) 
                {
                setStopScript(true);
                }
                Tile safeTile = new Tile(1720, 3465, 0);
                Walking.walk(safeTile.getRandomizedTile());
                sleepUntil(() -> getLocalPlayer().distance(safeTile) <= 3, Calculations.random(1750, 2750));
                if (getLocalPlayer().distance(safeTile) <= 3 && !getLocalPlayer().isInCombat()) 
                {
                    this.stop();
                } 
                else 
                {
                    Walking.walkExact(safeTile);
                }
                break;

            case SLEEP:
                int random = Calculations.random(1, 3);
                if (random == 1 && Mouse.isMouseInScreen()) 
                {
                    Mouse.move(new Point(Calculations.random(1, 763), Calculations.random(1, 500))); // Move mouse to random point before moving outside screen
                    sleep(Calculations.random(210, 1300));
                    Mouse.moveMouseOutsideScreen();
                } 
                else 
                {
                    if (random == 2 && Mouse.isMouseInScreen()) 
                    {
                        sleep(Calculations.random(350, 2200));
                        Mouse.moveMouseOutsideScreen();
                    } 
                    else 
                    {
                        if (random == 3 && Mouse.isMouseInScreen()) 
                        {
                            Mouse.move(new Point(Calculations.random(1, 763), Calculations.random(1, 500))); // Move mouse to random point
                        }
                    }
                }
                
                // Increase Sand Crabs kills
                if (getLocalPlayer().isInCombat() && !isStopScript() && !isResetAggro()) 
                {
                    List<NPC> interactNPCs = NPCs.all(npc -> npc != null && npc.getName().equals("Sand Crab") && npc.distance(getLocalPlayer()) <= 1);
                    for (NPC n : interactNPCs) 
                    {
                        if (n.isInteractedWith()) 
                        {
                            log("Interacting with NPC: " + n.getName() + " | lvl: " + n.getLevel() + " | tile: " + n.getTile());
                            sleepUntil(() -> !n.isInteractedWith() || !n.isOnScreen() || getLocalPlayer().getHealthPercent() < 45, 60000);
                            if (!n.isInteractedWith() || !n.isOnScreen()) 
                            {
                                log("Killed NPC: " + n.getName() + " | lvl: " + n.getLevel() + " | tile: " + n.getTile());
                                super.incrementSandCrabKills();
                            }
                        }
                    }
                }
                
                sleep(Calculations.random(2200, 3300));
                break;
        }
        return Calculations.random(900, 1700);
    }

    /**
     * Paint
     *
     * @param g2
     */
    @Override
    public void onPaint(Graphics2D g2) 
    {
        g2.setColor(Color.BLACK);
        g2.drawString("Runtime: " + timer.formatTime(), 10, 35);
        g2.drawString("Kills: " + getSandCrabKills(), 10, 65);
        g2.drawString("Hitpoints exp (p/h): " + SkillTracker.getGainedExperience(Skill.HITPOINTS) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.HITPOINTS) + ")", 10, 80);
        g2.drawString(getTrainingSkill().getName() + " exp (p/h): " + SkillTracker.getGainedExperience(getTrainingSkill()) + "(" + SkillTracker.getGainedExperiencePerHour(getTrainingSkill()) + ")", 10, 95);

    }
}
