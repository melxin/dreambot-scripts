/*
 * Copyright (c) 2021, 7ctx <https://github.com/7ctx> 
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
package cooking.wines;

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
import org.dreambot.api.wrappers.widgets.WidgetChild;

@ScriptManifest(
        author = "7ctx",
        name = "Wine_Maker",
        version = 1.0,
        description = "Wine Maker",
        category = Category.COOKING)

public class Main extends AbstractScript 
{
    private Timer timer;
    private boolean cooking = false;
    private boolean banking;

    public void init() 
    {
        timer = new Timer();
        SkillTracker.start(Skill.COOKING);
        log("Initialized");

    }

    @Override
    public void onStart() 
    {
        init();
        log("Welcome to Wine maker script by 7ctx.");
        log("If you experience any issues while running this script please report them to me on the forums.");
        log("Enjoy the script, gain some Cooking levels!.");
    }

    private void bank() 
    {
        banking = true;
        //getBank().open(BankLocation.CASTLE_WARS);
        Bank.openClosest();
        sleepUntil(() -> Bank.isOpen(), 1500);
        if (!Bank.contains(Constants.grapes) || !Bank.contains(Constants.jug_of_water)) 
        {
            this.stop();
        }
        Bank.depositAllItems();
        sleepUntil(() -> Inventory.getEmptySlots() > 20, 1500);
        Bank.withdraw(Constants.jug_of_water, 14);
        sleepUntil(() -> Inventory.count(Constants.jug_of_water) == 14, 1500);
        Bank.withdraw(Constants.grapes, 14);
        sleepUntil(() -> Inventory.count(Constants.grapes) == 14, 1500);
        //sleep(Calculations.random(622, 788));
        Bank.close();
        banking = false;
        cooking = true;
        //sleep(1000);
    }

    @Override
    public int onLoop() 
    {
        if (cooking && !banking) 
        {
            Inventory.getRandom(Constants.jug_of_water).useOn(Constants.grapes);
            sleep(Calculations.random(1120, 1368));
            WidgetChild Dialog = Widgets.getWidgetChild(270, 14, 38);
            Dialog.interact();
            sleep(Calculations.random(4030, 5099));
            //getWidgets().getWidget(WIDGET_ID).interact();
            //getInventory().get(Constants.iron_ore).interact();
            sleepUntil(() -> !getLocalPlayer().isAnimating(), 1500);
            cooking = false;
        }
        if (!Inventory.contains(Constants.grapes) || !Inventory.contains(Constants.jug_of_water)) 
        {
            bank();
        }
        if (Dialogues.inDialogue()) 
        {
            Dialogues.clickContinue();
            cooking = true;
        }
        return Calculations.random(466, 676);
    }

    @Override
    public void onPaint(Graphics g) 
    {
        //g.drawString("State: " + getState().toString(), 10, 35);
        g.drawString("Runtime: " + timer.formatTime(), 10, 35);
        g.drawString("Cooking exp (p/h): " + SkillTracker.getGainedExperiencePerHour(Skill.COOKING), 10, 65);

    }
}
