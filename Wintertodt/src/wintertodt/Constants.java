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
package wintertodt;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;

public class Constants 
{
    protected static int food = 385; // Food to take from bank = Shark
    protected static int food_amount = 7; // 7

    protected static int roots_amount = 10; // Amount of roots & kindling to cut, fletch & feed
    protected static int kindling_amount = 10;

    protected static int tinderbox = 590;
    protected static int axe = 6739; // dragon axe
    protected static int knife = 946;
    protected static int hammer = 2347;

    protected static int roots = 20695;
    protected static int kindling = 20696;

    protected static int rewardbox = 20703; // Supply crate

    protected static Area Wintertodt = new Area(1612, 3998, 1648, 3968); // x1, y2, x2, y2
    
    protected static Tile DOOR_LOCATION = new Tile(1630, 3962, 0);
    protected static Tile LEAVE_LOCATION = new Tile(1631, 3969, 0); // leave at end of wintertodt to rebank
    protected static Tile TREE_LOCATION = new Tile(1639, 3991, 0); // EAST

}
