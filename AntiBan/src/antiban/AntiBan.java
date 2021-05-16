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
package antiban;

import java.awt.Point;
import java.util.Random;
import org.dreambot.api.Client;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.input.event.impl.mouse.impl.click.ClickMode;
import org.dreambot.api.methods.Calculations;
import static org.dreambot.api.methods.MethodProvider.sleep;
import org.dreambot.api.methods.emotes.Emotes;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;

/**
 * AntiBan QUICKLY WRITTEN, POOR WRITTEN AND UNFIHSIED ~ NOT DEEMED TO BE SAFE..
 * 
 * @author t7emon
 */
public class AntiBan 
{
    private String status;
    private final Skill skill;
    
    // Constructor
    public AntiBan(Skill skill) 
    {
        this.skill = skill;
    }
    
    /**
     * Set status
     * 
     * @param status
     * @return AntiBan status
     */
    private String setStatus(String status) 
    {
        return this.status = status;
    }
    
    /**
     * Get status
     * 
     * @return Antiban status
     */
    public String getStatus() 
    {
        return status;
    }
    
    /**
     * Perform AntiBan
     */
    public void perform() 
    {
        int random = new Random().nextInt(22 + 1);
        sleep(Calculations.random(440, 1750));
        switch (random) 
        {
            // Click mouse on random
            case 1:
                int randX = new Random().nextInt(10);
                int randY = new Random().nextInt(12);
                Point randomMouseClickPoint = new Point(Client.getLocalPlayer().getX() + randX, Client.getLocalPlayer().getY() + randY);
                this.setStatus("AntiBan 1: Click mouse on random point -> " + randomMouseClickPoint);
                Mouse.click(randomMouseClickPoint);
                break;

            // Move mouse to random    
            case 2:
                int randomMouseX = new Random().nextInt(763);
                int randomMouseY = new Random().nextInt(500);
                Point randomMouseMovePoint = new Point(randomMouseX, randomMouseY);
                this.setStatus("AntiBan 2: Move mouse to random point -> " + randomMouseMovePoint);
                Mouse.move(randomMouseMovePoint);
                break;

            // Click random object
            case 3:
                GameObject randomClickObject = GameObjects.closest(o -> o != null && o.isOnScreen());
                if (randomClickObject == null) 
                {
                    this.perform();
                    return;
                }
                Mouse.click(randomClickObject, true);
                this.setStatus("AntiBan 3: Click on random object -> " + randomClickObject.getName());
                Mouse.move(new Point(new Random().nextInt(763), new Random().nextInt(500)));
                break;

            // Rotate camera to random object
            case 4:
                GameObject randomRotateObject = GameObjects.closest(o -> o != null && o.isOnScreen());
                if (randomRotateObject == null) 
                {
                    this.perform();
                    return;
                }
                this.setStatus("AntiBan 4: Rotate camera to randon object -> " + randomRotateObject.getName());
                Camera.keyboardRotateToEntity(randomRotateObject);
                break;

            // Rotate camera to my local player
            case 5:
                this.setStatus("AntiBan 5: Rotate camera to my local player");
                Camera.keyboardRotateToEntity(Client.getLocalPlayer());
                break;

            // Move mouse outside screen
            case 6:
                this.setStatus("AntiBan 6: Move mouse outside screen");
                Mouse.moveMouseOutsideScreen();
                break;
                
            // Move mouse to random tile
            case 7:
                Tile randomMouseMoveTile = Client.getLocalPlayer().getTile().getRandomizedTile();
                this.setStatus("AntiBan 7: Move mouse to random tile -> " + randomMouseMoveTile);
                Mouse.move(randomMouseMoveTile);
                break;
                
           // move mouse to random object   
            case 8:
                GameObject randomMouseMoveObject = GameObjects.closest(o -> o != null && o.isOnScreen());
                if (randomMouseMoveObject == null) 
                {
                    this.perform();
                    return;
                }
                this.setStatus("AntiBan 8: Move mouse to random object -> " + randomMouseMoveObject.getName());
                Mouse.move(randomMouseMoveObject);
                break;
                
             // Hover over random npc
            case 9:
                NPC randomHoverNPC = NPCs.closest(n -> n != null && n.isOnScreen());
                if (randomHoverNPC == null) 
                {
                    this.perform();
                    return;
                }
                this.setStatus("AntiBan 9: Hover mouse over random npc -> " + randomHoverNPC.getName());
                Mouse.move(randomHoverNPC.getCenterPoint());
            break;
            
            // Hover over over random object
            case 10:
                 GameObject randomHoverObject = GameObjects.closest(o -> o != null && o.isOnScreen());
                 if (randomHoverObject == null) 
                 {
                     this.perform();
                     return;
                 }
                 this.setStatus("AntiBan 10: Hover mouse over random object -> " + randomHoverObject.getName());
                 Mouse.move(randomHoverObject.getCenterPoint());
                break;
                
            // Move mouse outside screen and sleep 3 to 9 seconds
            case 11:
                Mouse.moveMouseOutsideScreen();
                this.setStatus("AntiBan 11: Move mouse outside screen and sleep");
                sleep(Calculations.random(3000, 9000));
                break;
                
            // Rotate camera to random npc    
            case 12:
                NPC randomRotateNPC = NPCs.closest(n -> n != null && n.isOnScreen());
                if (randomRotateNPC == null) 
                {
                    this.perform();
                    return;
                }
                this.setStatus("AntiBan 12: Rotate camera to random npc -> " + randomRotateNPC.getName());
                Camera.keyboardRotateToEntity(randomRotateNPC);
                break;
                
            // Hover over training skill    
            case 13:
                this.setStatus("AntiBan 13: Hover over skill -> " + skill.getName());
                Tabs.open(Tab.SKILLS);
                Skills.hoverSkill(skill);
                sleep(Calculations.random(700, 3000));
                Mouse.move(new Point(new Random().nextInt(760 + 1), new Random().nextInt(500 + 1)));
            break;
            
            // Open magic tab and move mouse random
            case 14:
                this.setStatus("AntiBan 14: Open magic tab");
                Tabs.open(Tab.MAGIC);
                Mouse.move(new Point(new Random().nextInt(600 + 5), new Random().nextInt(300 + 7)));
                break;
                
            // Perform random emote   
            case 15:
                this.setStatus("AntiBan 15: Perform random emote");
                Tabs.open(Tab.EMOTES);
                Emotes.doRandomEmote();
                break;
                
            // Click mouse random    
            case 16:
                this.setStatus("AntiBan 16: Click mouse randomly");
                Mouse.click();
                break;
                
            // Examine random ground item    
            case 17:
                GroundItem randomGroundItemHover = GroundItems.closest(i -> i != null && i.isOnScreen() && i.hasAction("Examine"));
                if (randomGroundItemHover == null) 
                {
                    this.perform();
                    return;
                }
                this.setStatus("AntiBan 17: Examine random ground item -> " + randomGroundItemHover.getName());
                Mouse.move(randomGroundItemHover);
                randomGroundItemHover.interactForceRight("Examine");
                break;
            
            // Move mouse to randomized area tile
            case 18:
               Area randomArea = Area.generateArea(20, Client.getLocalPlayer().getTile().getRandomizedTile());
               Tile randomTile = randomArea.getRandomTile();
               this.setStatus("AntiBan 18: Move mouse to randomized area tile -> " + randomTile);
               Mouse.move(randomTile);
               Mouse.click();
               break;
              
               // Click mouse randomly multiple times
            case 19:
                int mouseClickCount = new Random().nextInt(6 + 1);
                this.setStatus("AntiBan 19: Click mouse multiple times -> " + mouseClickCount);
                int i = 0;
                while (i < mouseClickCount) 
                {
                    Mouse.click();
                    i++;
                }
                break;
                
            // Examine tree's    
            case 20:
                GameObject tree = GameObjects.closest(o -> o != null && o.getName().toLowerCase().contains("tree") && o.hasAction("Examine") && o.isOnScreen());
                if (tree == null) 
                {
                    this.perform();
                    return;
                }
                this.setStatus("AntiBan 20: Examine random tree -> " + tree.getName());
                tree.interactForceRight("Examine");
                break;
                
                // Right click mouse randomly
            case 21:
                this.setStatus("AntiBan 20: Right-click mouse randomly");
                Mouse.click(ClickMode.RIGHT_CLICK);
                sleep(Calculations.random(450, 1356));
                Mouse.move(new Point(new Random().nextInt(550 + 1), new Random().nextInt(220 + 3)));
                break;
                
            // Do nothing    
            case 22:
                break;
        }
    }
}
