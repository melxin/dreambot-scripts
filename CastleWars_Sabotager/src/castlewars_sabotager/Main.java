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
package castlewars_sabotager;

import java.awt.Color;
import java.awt.Graphics;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.walking.pathfinding.impl.obstacle.impl.DestructableObstacle;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.widgets.message.Message;

@ScriptManifest(
        author = "7ctx",
        name = "CastleWars_Sabotager",
        version = 1.0,
        description = "Sabotage CastleWars games (currently only supports saradomin team)",
        category = Category.MINIGAME)

public class Main extends CastleWars implements ChatListener 
{
    private Timer timer;
    
    /**
     * Script start
     */
    @Override
    public void onStart() 
    {
        timer = new Timer();
        log("Initialized");
        log("Welcome to CastleWars sabotager by 7ctx.");
        // Walking.getAStarPathFinder().addObstacle(new PassableObstacle("Barricade", "Burn", null, null, null));
        Walking.getAStarPathFinder().addObstacle(new DestructableObstacle("Barricade", "Burn", null, null, null));
        Map.addObstacleActions("Burn");
    }

    /**
     * Game message received
     *
     * @param msg
     */
    @Override
    public void onGameMessage(Message msg) 
    {
        if (msg.getMessage().equalsIgnoreCase("Oh dear, you are dead!")) 
        {
            super.inRespawnRoom();
        }

        if (msg.getMessage().equalsIgnoreCase("Your team has already setup 10 barricades.")) 
        {
            /*setupBarricades = false;
            sleep(2500);
            setupBarricades = true;*/
        }

        if (msg.getMessage().equalsIgnoreCase("You can't set up a barricade here.")) 
        {
            Walking.walkExact(getLocalPlayer().getTile().getRandomizedTile(4));
        }
    }

    /**
     * Do logic
     *
     * @return Calculations.random
     */
    @Override
    public int onLoop() 
    {
        // Game ended
        if (Dialogues.inDialogue() && super.inLobby()) 
        {
            super.onGameEnd();
        }
        
        // Walk to lobby if outside lobby
        if (!super.gameStarted() && !super.inLobby() && !super.inWaitingRoom()) 
        {
            log("Walking to lobby");
            Walking.walk(Locations.castlewarsLobby.getArea().getCenter());
        }

        // Enter Saradomin Portal
        if (super.inLobby() && !super.inWaitingRoom()) 
        {
            log("In castlewars lobby area");
            super.enterWaitingRoom();
        }

        // Wait until next game
        if (super.inWaitingRoom()) 
        {
            log("In saradoming waiting room area");
            super.waitUntilGame();
        }

        // Leave respawn room
        if (super.inRespawnRoom()) 
        {
            log("In respawn room area");
            if (!Walking.isRunEnabled()) 
            {
                Walking.toggleRun();
            }
            super.leaveRespawnRoom();
        }

        // Use explosive potion/Tinderbox on barricade when stuck
        if (Walking.shouldWalk(1) && getLocalPlayer().isStandingStill() && !super.inWaitingRoom()) 
        {
            log("Found obstacle!");
            if (Inventory.contains(Item -> Item.getName().contains("Explosive potion"))) 
            {
                super.useExplosive();
            } 
            else 
            {
                if (Inventory.contains(Item -> Item.getName().equals("Tinderbox"))) 
                {
                    super.useTinderbox();
                } 
                else 
                {
                    Walking.walkExact(getLocalPlayer().getTile().getRandomizedTile(4));
                }
            }
        }

        // Leave first floor
        if (super.onFirstFloor()) 
        {
            log("On first floor area");
            super.leaveFirstFloor();
        }

        // Grab Explosive potions, tinderbox and barricades
        if (super.gameStarted() && !super.inRespawnRoom() && !super.inLobby()) 
        {
            if (!Inventory.contains(Item -> Item.getName().equals("Explosive potion"))
                    && !Inventory.contains(Item -> Item.getName().equals("Tinderbox"))) 
            {
                super.grabExplosives();
            } 
            else 
            {
                if (super.inSupplyArea() && !Inventory.contains(Item -> Item.getName().equals("Tinderbox"))) 
                {
                    super.grabTinderbox();
                } 
                else 
                {
                    if (!Inventory.contains(Item -> Item.getName().equals("Barricade"))) 
                    {
                        super.grabBarricades();
                    }
                }
            }

            // Setup barricades
            if (Inventory.contains(Item -> Item.getName().equals("Barricade"))
                    && Inventory.contains(Item -> Item.getName().equals("Explosive potion"))) 
            {
                super.setupBarricades();
            }
        }
        return Calculations.random(388, 500);
    }

    /**
     * Paint UI
     *
     * @param g, Graphics
     */
    @Override
    public void onPaint(Graphics g) 
    {
        if (timer == null) 
        {
            return;
        }

        g.setColor(Color.BLACK);
        g.drawString("Runtime: " + timer.formatTime(), 10, 320); // 35
        g.drawString("Game count: " + super.getGameCount(), 10, 335); // 65
        //g.drawString("Games exp (p/h): " + getSkillTracker().getGainedExperience(Skill.FISHING) + "(" + getSkillTracker().getGainedExperiencePerHour(Skill.FISHING) + ")", 10, 65); //65
    }
}
