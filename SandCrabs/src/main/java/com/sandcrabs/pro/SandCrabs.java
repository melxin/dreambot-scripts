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
package com.sandcrabs.pro;

import java.awt.Point;
import java.util.concurrent.atomic.AtomicInteger;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankType;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.wrappers.interactive.Entity;

public abstract class SandCrabs extends AbstractScript 
{
    private boolean useFood;
    private String foodName;
    private int foodAmount;
    private Tile afkTile;
    private Area aggroResetArea;
    private boolean resetAggro;
    private Skill trainingSkill;
    private boolean specialAttackEnabled;
    private boolean stopScript;
    private final AtomicInteger sandCrabKills = new AtomicInteger();
    
    /**
     * Should we use food?
     * 
     * @param useFood 
     */
    public void setUseFood(boolean useFood) 
    {
        this.useFood = useFood;
    }
    
    /**
     * Is use food enabled?
     * 
     * @return useFood
     */
    public boolean isUseFood() 
    {
        return useFood;
    }
    
    /**
     * Set food name
     * 
     * @param foodName 
     */
    public void setFoodName(String foodName) 
    {
        this.foodName = foodName;
    }
    
    /**
     * Get food name
     * 
     * @return food name
     */
    public String getFoodName() 
    {
        return foodName;
    }
    
    /**
     * Set food amount, amount of food to take from bank
     * 
     * @param foodAmount 
     */
    public void setFoodAmount(int foodAmount) 
    {
        this.foodAmount = foodAmount;
    }
    
    /**
     * Get food amount
     * 
     * @return food amount
     */
    public int getFoodAmount() 
    {
        return foodAmount;
    }
    
    /**
     * Set afk tile on start of the script, we keep returning here. should be tile inbetween 3 Sand Crabs
     * 
     * @param afkTile 
     */
    public void setAfkTile(Tile afkTile) 
    {
        this.afkTile = afkTile;
    }
    
    /**
     * Get afk tile
     * 
     * @return afk tile
     */
    public Tile getAfkTile() 
    {
        return afkTile;
    }
    
    /**
     * Set aggro reset are
     * 
     * @param aggroResetArea 
     */
    public void setAggroResetArea(Area aggroResetArea) 
    {
        this.aggroResetArea = aggroResetArea;
    }
    
    /**
     * Get aggro reset area
     * 
     * @return aggro reset area
     */
    public Area getAggroResetArea() 
    {
        return aggroResetArea;
    }
    
    /**
     * Set reset aggro boolean
     * 
     * @param setResetAggro 
     */
    public void setResetAggro(boolean setResetAggro) 
    {
        this.resetAggro = setResetAggro;
    }
    
    /**
     * isResetAggro boolean
     * 
     * @return resetAggro
     */
    public boolean isResetAggro() 
    {
        return resetAggro;
    }
    
    /**
     * Set skill to train
     * 
     * @param skill 
     */
    public void setTrainingSkill(Skill skill) 
    {
        this.trainingSkill = skill;
    }
    
    /**
     * Get skill
     * 
     * @return skill
     */
    public Skill getTrainingSkill() 
    {
        return trainingSkill;
    }
    
    /**
     * Enable special attack
     * 
     * @param specialAttackEnabled
     */
    public void setSpecialAttackEnabled(boolean specialAttackEnabled) 
    {
        this.specialAttackEnabled = specialAttackEnabled;
    }
    
    /**
     * Get is special attack enabled
     * 
     * @return special attack enabled
     */
    public boolean isSpecialAttackEnabled() 
    {
        return specialAttackEnabled;
    }
    
    /**
     * Set stop script
     * 
     * @param stopScript 
     */
    public void setStopScript(boolean stopScript) 
    {
        this.stopScript = stopScript;
    }
    
    /**
     * Stop script boolean getter
     * 
     * @return stopScript
     */
    public boolean isStopScript() 
    {
        return stopScript;
    }
    
    /**
     * Increment Sand Crabs kills by one
     */
    public void incrementSandCrabKills() 
    {
        sandCrabKills.incrementAndGet();
    }
    
    /**
     * Get current Sand Crab kills
     * 
     * @return Sand Crab kills
     */
    public int getSandCrabKills() 
    {
        return sandCrabKills.get();
    }
    
    /**
     * Handle Bank method
     */
    public void doBanking() 
    {
        Walking.walk(Bank.getClosestBankLocation().getCenter());
        Entity bank = Bank.getClosestBank(BankType.CHEST);
        if (bank == null) 
        {
            return;
        }
        sleepUntil(() -> bank.isOnScreen(), Calculations.random(2000, 3500));
        bank.interactForceRight("Use");
        log("STATUS = BANK");
        sleepUntil(() -> Bank.open(), Calculations.random(3000, 4000));
        //sleep(Calculations.random(2176, 2347));
        //getBank().depositAllExcept(Constants.KNIFE);

        if (Bank.isOpen()) 
        {
            if (!Bank.contains(Item -> Item != null && Item.getName().contains(getFoodName()))) 
            {
                log("Error: no food left in bank");
                this.stop();
                return;
            }
            sleep(Calculations.random(1200, 3000));
            if (Inventory.emptySlotCount() < getFoodAmount()) 
            {
            Bank.depositAllItems();
            sleepUntil(() -> Inventory.emptySlotCount() >= getFoodAmount(), Calculations.random(4500, 7000));
            }
            Bank.withdraw(Item -> Item != null && Item.getName().contains(getFoodName()), getFoodAmount());
            sleep(Calculations.random(1400, 2500));
        }
        
        if (Inventory.count(Item -> Item != null && Item.getName().toLowerCase().contains(getFoodName())) == getFoodAmount())
        {
            Bank.close();
            Mouse.move(new Point(Calculations.random(1, 763), Calculations.random(1, 500))); // Move mouse to random
        }
    }
}
