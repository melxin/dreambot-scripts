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
package fishing.barbarian;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.widgets.message.Message;

@ScriptManifest(
        author = "7ctx",
        name = "Barbarian_Fishing",
        version = 1.0,
        description = "Barbarian Fishing",
        category = Category.FISHING)

public class Main extends AbstractScript implements ChatListener
{
    private Timer timer;
    NPC Fishing_spot;
    private int fish_count = 0;

    public void init() 
    {
        timer = new Timer();
        SkillTracker.start(Skill.FISHING);
        SkillTracker.start(Skill.AGILITY);
        SkillTracker.start(Skill.STRENGTH);
        log("Initialized");

    }

    @Override
    public void onStart() 
    {
        init();
        log("Welcome to Barbarian Fishing script by 7ctx.");
        log("If you experience any issues while running this script please report them to me on the forums.");
        log("Enjoy the script, gain some Fishing levels!.");
    }

    @Override
    public void onMessage(Message msg) 
    {
        if (msg.getMessage().contains("You catch a leaping trout.")
                || msg.getMessage().contains("You catch a leaping salmon.")
                || msg.getMessage().contains("You catch a leaping sturgeon.")) 
        {
            fish_count++;
        }
    }

    private enum State 
    {
        FISH, DROP
    };

    private State getState() 
    {
        if (!Inventory.contains(Constants.Barbarian_rod) || !Inventory.contains(Constants.Feathers)) 
        {
            log("You need a Barbarian rod & Feathers to use this script!");
            this.stop();
        }

        if (Dialogues.inDialogue()) 
        {
            Dialogues.clickContinue();
            return State.FISH;
        }
        
        if (Inventory.count(Constants.Leaping_trout) > new Random().nextInt(6 + 1) + 10
                || Inventory.count(Constants.Leaping_salmon) > new Random().nextInt(6 + 1) + 10
                || Inventory.count(Constants.Leaping_sturgeon) > new Random().nextInt(6 + 1) + 10
                || Inventory.isFull()) 
        {
            return State.DROP;
        }
        return State.FISH;
    }

    @Override
    public int onLoop() 
    {
        switch (getState()) 
        {
            case FISH:
                Fishing_spot = NPCs.closest("Fishing spot");
                if (!getLocalPlayer().isInteracting(Fishing_spot) && Fishing_spot.interactForceRight("Use-rod")) 
                {
                    sleepUntil(() -> !getLocalPlayer().isInteracting(Fishing_spot), 240000);
                }
                break;
                
            case DROP:
                // getInventory().dropAllExcept(Constants.Barbarian_rod, Constants.Feathers);
                Inventory.dropAll(Item -> Item != null && Item.getName().contains("Leaping"));
                sleepUntil(() -> !Inventory.contains(Constants.Leaping_trout)
                        || !Inventory.contains(Constants.Leaping_salmon)
                        || !Inventory.contains(Constants.Leaping_sturgeon), 240000);
                sleep(Calculations.random(977, 1477));
                Mouse.click(Fishing_spot);
                break;
        }
        return Calculations.random(950, 1050);
    }

    @Override
    public void onPaint(Graphics g) 
    {
        g.setColor(Color.cyan);
        g.drawString("Runtime: " + timer.formatTime(), 10, 35);
        g.drawString("Fishing exp (p/h): " + SkillTracker.getGainedExperience(Skill.FISHING) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.FISHING) + ")", 10, 65);
        g.drawString("Agility exp (p/h): " + SkillTracker.getGainedExperience(Skill.AGILITY) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.AGILITY) + ")", 10, 80);
        g.drawString("Strength exp (p/h): " + SkillTracker.getGainedExperience(Skill.STRENGTH) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.STRENGTH) + ")", 10, 95);
        g.drawString("Fish gained (p/h): " + fish_count + "(" + timer.getHourlyRate(fish_count) + ")", 10, 110);
    }
}
