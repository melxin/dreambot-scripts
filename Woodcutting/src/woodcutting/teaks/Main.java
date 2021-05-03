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
package woodcutting.teaks;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;

@ScriptManifest(
        author = "T7emon",
        name = "Woodcutting_Private",
        version = 0.1,
        description = "Woodcutting Private",
        category = Category.WOODCUTTING)

public class Main extends AbstractScript 
{
    private Timer timer;

    @Override
    public void onStart() 
    {
        timer = new Timer();
        SkillTracker.start(Skill.WOODCUTTING);
        log("Starting!");
    }

    private enum State 
    {
        CUT, DROP, BANK, SLEEP
    };

    private State getState() 
    {
        if (Inventory.count(Constants.teak_logs) > new Random().nextInt(6 + 1) + 10 || Inventory.isFull()) 
        {
            return State.DROP;
        } 
        else 
        {
            return State.CUT;
        }
    }

    @Override
    public int onLoop() 
    {
        switch (getState()) 
        {
            case DROP:
                Inventory.dropAll(Constants.teak_logs);
                break;
            case CUT:
                GameObject tree = GameObjects.closest(Constants.tree);
                if (tree.isOnScreen()) 
                {
                    if (tree.interact()) 
                    {
                        //tree.interactForceRight("Chop down");
                        sleep(3000, 4000);
                        sleepUntil(() -> !getLocalPlayer().isAnimating(), 15000);
                        // sleepUntil(() -> tree == null, 15000); 
                    }
                }
                break;
        }
        return Calculations.random(0, 0);
    }

    @Override
    public void onPaint(Graphics2D g1) 
    {
        g1.setColor(Color.RED);
        g1.drawString("Runtime: " + timer.formatTime(), 10, 35); //35
        g1.drawString("Woodcutting exp (p/h): " + SkillTracker.getGainedExperience(Skill.WOODCUTTING) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.WOODCUTTING) + ")", 10, 65); //65
    }

    @Override
    public void onExit() 
    {
        log("Stopping!");
    }
}
