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
package magic.superheat;

import java.awt.Graphics;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.Category;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.items.Item;

@ScriptManifest(
        author = "7ctx",
        name = "Superheater",
        version = 1.0,
        description = "Superheat iron ore",
        category = Category.MAGIC)

public class Main extends AbstractScript 
{
    private Timer timer;
    private boolean superheat = false;
    private boolean banking;

    public void init() 
    {
        timer = new Timer();
        SkillTracker.start(Skill.MAGIC);
        SkillTracker.start(Skill.SMITHING);
        log("Initialized");

    }

    @Override
    public void onStart() 
    {
        init();
        log("Welcome to Superheat script by 7ctx.");
        log("If you experience any issues while running this script please report them to me on the forums.");
        log("Enjoy the script, gain some Magic levels!.");
    }

    private void bank() 
    {
        banking = true;
        if (Bank.openClosest()) 
        {
            //GameObject bank = getGameObjects().closest(gameObject -> gameObject != null && gameObject.hasAction("Bank"));
            //bank.interact();
            sleepUntil(() -> Bank.isOpen(), 5000);
            sleep(Calculations.random(980, 1030));
        }
        
        if (Bank.isOpen() && !Bank.contains(Constants.iron_ore)) 
        {
            log("Ran out of Ores");
            this.stop();
        }
        
        Bank.depositAllExcept(Constants.nature_rune);
        sleepUntil(() -> Inventory.emptySlotCount() >= 26, 3000);
        Bank.withdraw(Constants.iron_ore, 27);
        sleepUntil(() -> Inventory.count(Constants.iron_ore) >= 27, 1500);
        Bank.close();
        banking = false;
    }

    @Override
    public int onLoop() 
    {
        superheat = true;
        /*alching = false;
        // Alching
        if (alching) 
        {
            getMagic().castSpell(Normal.HIGH_LEVEL_ALCHEMY);
            Item item = getInventory().get("Steel platelegs");
            item.interact();
            sleepUntil(() -> !getLocalPlayer().isAnimating(), 3000);
        }*/
        
        // Superheat
        if (superheat) 
        {
            if (!Inventory.contains(Constants.nature_rune)) // Stop if there are no nature runes in inventory
            {
                this.stop();
            }

            if (!Inventory.contains(Constants.iron_ore) && Magic.isSpellSelected()) // Click mouse when spell is selected and out of iron ores
            {
                Mouse.click();
            } 
            else 
            {
                if (!Inventory.contains(Constants.iron_ore)) 
                {
                    bank();
                }
            }

            if (!Magic.isSpellSelected() && Inventory.contains(Constants.iron_ore) && !banking) 
            {
                Magic.castSpell(Normal.SUPERHEAT_ITEM);
                Item item = Inventory.get(Constants.iron_ore);
                if (Magic.isSpellSelected()) 
                {
                    item.interact();
                }
            }
        }
        return Calculations.random(0, 0);
    }

    @Override
    public void onPaint(Graphics g) 
    {
        g.drawString("Runtime: " + timer.formatTime(), 10, 35);
        g.drawString("Magic exp (p/h): " + SkillTracker.getGainedExperience(Skill.MAGIC) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.MAGIC) + ")", 10, 65);
        g.drawString("Smithing exp (p/h): " + SkillTracker.getGainedExperience(Skill.SMITHING) + "(" + SkillTracker.getGainedExperiencePerHour(Skill.SMITHING) + ")", 10, 75);

    }
}
