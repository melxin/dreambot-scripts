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
package combat.cow_killer;

import java.awt.Color;
import java.awt.Graphics;
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
        name = "Cow_Killer",
        version = 1.0,
        description = "Kill Cows",
        category = Category.COMBAT)

public class Main extends AbstractScript implements ChatListener
{
    private Timer timer;
    private int kills = 0;

    public void init() 
    {
        timer = new Timer();
        SkillTracker.start(Skill.HITPOINTS);
        SkillTracker.start(Skill.ATTACK);
        SkillTracker.start(Skill.STRENGTH);
        SkillTracker.start(Skill.DEFENCE);
        SkillTracker.start(Skill.RANGED);
        SkillTracker.start(Skill.MAGIC);
        log("Initialized");
    }

    @Override
    public void onStart() 
    {
        init();
        log("Welcome to Cow Killer script by 7ctx.");
    }

    @Override
    public void onMessage(Message msg) 
    {
        if (msg.getMessage().contains("There is no ammo left in your quiver.")) 
        {
            log("There is no ammo left in your quiver.");
            this.stop();
        }
    }

    private enum State 
    {
        BANK, FIGHT, EAT
    };

    private State getState() 
    {
        if (!Inventory.contains(Constants.food) && Constants.food_enabled) 
        {
            log("You need food We dont bank just stop");
            this.stop();
        }

        if (Dialogues.inDialogue()) 
        {
            Dialogues.clickContinue();
            return State.FIGHT;
        }

        NPC npc = NPCs.closest(NPC -> NPC.getName().contains(Constants.NPC));
        if (npc != null && npc.isOnScreen()
                && !npc.isInCombat()
                && !getLocalPlayer().isInCombat()
                && !npc.isInteractedWith()
                && !npc.isInteracting(getLocalPlayer())) 
        {
            return State.FIGHT;
        }

        if (getLocalPlayer().getHealthPercent() < 40) 
        {
            return State.EAT;
        }

        return State.FIGHT;
    }

    @Override
    public int onLoop() 
    {
        switch (getState()) 
        {
            case BANK:
                // @TODO fix banking? 
                break;

            case FIGHT:
                NPC npc = NPCs.closest(NPC -> NPC.getName().contains(Constants.NPC));
                if (npc.interact()) 
                {
                    sleepUntil(() -> !npc.isInteractedWith() && !npc.isDrawMinimapDot(), 15000); //!npc.drawnminimap
                    kills++;
                }
                break;
                
            case EAT:
                Inventory.getRandom(Constants.food).interact();
                break;
        }
        return Calculations.random(100, 300);
    }

    @Override
    public void onPaint(Graphics g) 
    {
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Runtime: " + timer.formatTime(), 10, 35);
        g.drawString("Kills: " + kills, 10, 50);
        g.drawString("Hitpoints (p/h): " + SkillTracker.getGainedExperience(Skill.HITPOINTS) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.HITPOINTS) + ")", 10, 65);
        g.drawString("Attack exp (p/h): " + SkillTracker.getGainedExperience(Skill.ATTACK) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.ATTACK) + ")", 10, 80);
        g.drawString("Strength exp (p/h): " + SkillTracker.getGainedExperience(Skill.STRENGTH) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.STRENGTH) + ")", 10, 95);
        g.drawString("Defence exp (p/h): " + SkillTracker.getGainedExperience(Skill.DEFENCE) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.DEFENCE) + ")", 10, 110);
        g.drawString("Ranged exp (p/h): " + SkillTracker.getGainedExperience(Skill.RANGED) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.RANGED) + ")", 10, 125);
        g.drawString("Magic exp (p/h): " + SkillTracker.getGainedExperience(Skill.MAGIC) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.MAGIC) + ")", 10, 140);
    }
}
