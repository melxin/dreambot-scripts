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
package fletching;

import java.awt.Graphics;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;

@ScriptManifest(
        author = "7ctx",
        name = "Fletching",
        version = 1.0,
        description = "String Bows",
        category = Category.FLETCHING)

public class Main extends AbstractScript 
{
    private Timer timer;
    private boolean fletching = false;
    private boolean banking;

    public void init() 
    {
        timer = new Timer();
        SkillTracker.start(Skill.FLETCHING);
        log("Initialized");

    }

    @Override
    public void onStart() 
    {
        init();
        log("Welcome to Fletching script by 7ctx.");
        log("If you experience any issues while running this script please report them to me on the forums.");
        log("Enjoy the script, gain some Fletching levels!.");
    }

    private void bank() 
    {
        banking = true;
        Bank.openClosest();
        sleepUntil(() -> Bank.isOpen(), 2500);
        if (!Bank.contains(Constants.Longbow_unf) || !Bank.contains(Constants.bowstring)) 
        {
            this.stop();
        }
        if (Bank.isOpen()) 
        {
            Bank.depositAllItems();
            sleepUntil(() -> Inventory.emptySlotCount() == 28, 2500);
            Bank.withdraw(Constants.Longbow_unf, 14);
            sleepUntil(() -> Inventory.count(Constants.Longbow_unf) == 14, 2500);
            Bank.withdraw(Constants.bowstring, 14);
            sleepUntil(() -> Inventory.count(Constants.bowstring) == 14, 2500);
            Bank.close();
            sleepUntil(() -> !Bank.isOpen(), 2500);
            banking = false;
            fletching = true;
            //sleep(1000);
        }
    }

    @Override
    public int onLoop() 
    {
        if (fletching && !banking) 
        {
            Inventory.getRandom(Constants.Longbow_unf).useOn(Constants.bowstring);
            sleep(Calculations.random(1120, 1368));
            Widgets.getWidgetChild(270, 14, 38).interact();
            sleep(Calculations.random(4030, 5099));
            //getWidgets().getWidget(WIDGET_ID).interact();
            //getInventory().get(Constants.iron_ore).interact();
            sleepUntil(() -> !getLocalPlayer().isAnimating(), 1500);
            fletching = false;
        }
        
        if (!Inventory.contains(Constants.Longbow_unf) || !Inventory.contains(Constants.bowstring)) 
        {
            bank();
        }
        
        if (Dialogues.inDialogue()) 
        {
            Dialogues.clickContinue();
            fletching = true;
        }
        
        return Calculations.random(466, 676);
    }

    @Override
    public void onPaint(Graphics g) 
    {
        //g.drawString("State: " + getState().toString(), 10, 35);
        g.drawString("Runtime: " + timer.formatTime(), 10, 35);
        g.drawString("Fletching exp (p/h): " + SkillTracker.getGainedExperience(Skill.FLETCHING) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.FLETCHING) + ")", 10, 65); //65

    }
}
