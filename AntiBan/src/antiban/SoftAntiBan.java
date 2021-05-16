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
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

/**
 * AntiBan QUICKLY WRITTEN, POOR WRITTEN AND UNFIHSIED ~ NOT DEEMED TO BE SAFE..
 * 
 * @author t7emon
 */
public class SoftAntiBan 
{
    private String status;
    private final Skill skill;
    
    // Constructor
    public SoftAntiBan(Skill skill) 
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
        int random = new Random().nextInt(12 + 1);
        sleep(Calculations.random(440, 1750));
        switch (random) 
        {
            // Move mouse to random    
            case 1:
                int randomMouseX = new Random().nextInt(763);
                int randomMouseY = new Random().nextInt(500);
                Point randomMouseMovePoint = new Point(randomMouseX, randomMouseY);
                this.setStatus("AntiBan 1: Move mouse to random point -> " + randomMouseMovePoint);
                Mouse.move(randomMouseMovePoint);
                break;

            // Rotate camera to random object
            case 2:
                GameObject randomRotateObject = GameObjects.closest(o -> o != null && o.isOnScreen());
                if (randomRotateObject == null) 
                {
                    this.perform();
                    return;
                }
                this.setStatus("AntiBan 2: Rotate camera to randon object -> " + randomRotateObject.getName());
                Camera.keyboardRotateToEntity(randomRotateObject);
                break;

            // Rotate camera to my local player
            case 3:
                this.setStatus("AntiBan 3: Rotate camera to my local player");
                Camera.keyboardRotateToEntity(Client.getLocalPlayer());
                break;

            // Move mouse outside screen
            case 4:
                this.setStatus("AntiBan 4: Move mouse outside screen");
                Mouse.moveMouseOutsideScreen();
                sleep(Calculations.random(300, 3000));
                break;
                
            // Move mouse to random tile
            case 5:
                Tile randomMouseMoveTile = Client.getLocalPlayer().getTile().getRandomizedTile();
                this.setStatus("AntiBan 5: Move mouse to random tile -> " + randomMouseMoveTile);
                Mouse.move(randomMouseMoveTile);
                break;
                
           // move mouse to random object   
            case 6:
                GameObject randomMouseMoveObject = GameObjects.closest(o -> o != null && o.isOnScreen());
                if (randomMouseMoveObject == null) 
                {
                    this.perform();
                    return;
                }
                this.setStatus("AntiBan 6: Move mouse to random object -> " + randomMouseMoveObject.getName());
                Mouse.move(randomMouseMoveObject);
                break;
                
            // Rotate camera to random npc    
            case 7:
                NPC randomRotateNPC = NPCs.closest(n -> n != null && n.isOnScreen());
                if (randomRotateNPC == null) 
                {
                    this.perform();
                    return;
                }
                this.setStatus("AntiBan 7: Rotate camera to random npc -> " + randomRotateNPC.getName());
                Camera.keyboardRotateToEntity(randomRotateNPC);
                break;
                
            // Hover over training skill    
            case 8:
                this.setStatus("AntiBan 8: Hover over skill -> " + skill.getName());
                Tabs.open(Tab.SKILLS);
                Skills.hoverSkill(skill);
                sleep(Calculations.random(1000, 1500));
                Mouse.move(new Point(new Random().nextInt(760 + 1), new Random().nextInt(500 + 1)));
                sleep(Calculations.random(700, 1100));
                Mouse.move(new Point(new Random().nextInt(760 + 1), new Random().nextInt(500 + 1)));
            break;
                
            // Click mouse random    
            case 9:
                this.setStatus("AntiBan 9: Click mouse randomly");
                Mouse.click();
                break;
            
            // Move mouse to randomized area tile and click
            case 10:
               Area randomArea = Area.generateArea(20, Client.getLocalPlayer().getTile().getRandomizedTile());
               Tile randomTile = randomArea.getRandomTile();
               this.setStatus("AntiBan 11: Move mouse to randomized area tile -> " + randomTile);
               Mouse.move(randomTile);
               Mouse.click();
               break;
              
               // Click mouse randomly multiple times
            case 11:
                int mouseClickCount = new Random().nextInt(8 + 1);
                this.setStatus("AntiBan 11: Click mouse multiple times -> " + mouseClickCount);
                int i = 0;
                while (i < mouseClickCount) 
                {
                    Mouse.click();
                    i++;
                }
                Mouse.move(new Point(new Random().nextInt(763 + 1), new Random().nextInt(500 + 1)));
                break;
                
            // Right click mouse randomly
            case 12:
                this.setStatus("AntiBan 12: Right-click mouse randomly");
                Mouse.click(ClickMode.RIGHT_CLICK);
                sleep(Calculations.random(450, 1356));
                Mouse.move(new Point(new Random().nextInt(763 + 1), new Random().nextInt(500 + 1)));
                break;
        }
    }
}
