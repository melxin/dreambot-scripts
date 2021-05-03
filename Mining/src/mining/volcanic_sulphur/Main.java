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
package mining.volcanic_sulphur;

import java.awt.Color;
import java.awt.Graphics2D;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;

@ScriptManifest(
        author = "7ctx",
        name = "Volcanic_sulphur_Miner",
        version = 1.0,
        description = "Volcanic_sulphur miner",
        category = Category.MINING)

public class Main extends AbstractScript 
{
    private Timer timer;
    private int ore_count = 0;
    private boolean banking = false;

    public void init() 
    {
        timer = new Timer();
        SkillTracker.start(Skill.MINING);
        log("Initialized");

    }

    public void bank() 
    {
        banking = true;
        GameObject bank = GameObjects.closest(28594); //7455 //7468
        sleepUntil(() -> bank.interact(), 5000);
        sleepUntil(() -> Bank.isOpen(), 5000);
        Bank.depositAllItems();
        Bank.withdraw(Constants.food, 5);
        sleep(1000);
        Bank.close();
        banking = false;
        Walking.walkExact(new Tile(1447, 3863, 0));

    }

    @Override
    public void onStart() 
    {
        init();
        log("Welcome to Volcanic sulphur mining script by 7ctx.");
        log("This is to get Volcanic sulphur for lovakengj favor.");
    }

    private enum State 
    {
        MINE, BANK, SLEEP
    };

    private State getState() 
    {
        if (Skills.getBoostedLevels(Skill.HITPOINTS) < 40) 
        {
            Inventory.interact(item -> item != null && item.getName().contains("Shark"), "Eat");
        }

        if (Inventory.isFull() || !Inventory.contains(Constants.food)) 
        {
            return State.BANK;
        }

        return State.MINE;
    }

    @Override
    public int onLoop() 
    {
        switch (getState()) 
        {
            case MINE:
                GameObject ore = GameObjects.getTopObjectOnTile(new Tile(1445, 3863, 0)); //7455 //7468
                if (ore != null) 
                {
                    if (ore.getID() == 28498 || ore.getID() == 28497 || ore.getID() == 28496) 
                    {
                    sleepUntil(() -> ore.interact(), 5000);
                    sleep(Calculations.random(900, 1050));
                    sleepUntil(() -> !getLocalPlayer().isAnimating(), 5000);
                    sleepUntil(() -> ore.getID() != 28498 || ore.getID() != 28497 || ore.getID() != 28496, 5000);
                    ore_count++;
                }
                }
                break;
                
            case BANK:
                bank();
                break;
                
            case SLEEP:
                sleep(Calculations.random(100, 200));
                break;
        }
        return Calculations.random(50, 100);
    }

    @Override
    public void onPaint(Graphics2D g1) 
    {
        g1.setColor(Color.RED);
        g1.drawString("Runtime: " + timer.formatTime(), 10, 35);
        g1.drawString("Mining exp (p/h): " + SkillTracker.getGainedExperience(Skill.MINING) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.MINING) + ")", 10, 65);
        g1.drawString("sulphur gained (p/h): " + ore_count + "(" + timer.getHourlyRate(ore_count) + ")", 10, 80);
    }
}
