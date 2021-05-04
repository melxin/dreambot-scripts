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
package agility.draynor;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;

public class Constants 
{
    // Variables
    private static final int food = 361; // 385 = Shark, 379 = Lobster, 361 = Tuna

    /**
     * Food getter
     *
     * @return food
     */
    protected static int getFood() 
    {
        return food;
    }

    /**
     * Roofs area enumeration
     */
    public enum roofs 
    {
        START_LOCATION(new Tile(3103, 3279, 0)),
        ROOF_1(new Area(3102, 3277, 3097, 3281, 3)), // x1, y2, x2, y2, z
        ROOF_2(new Area(3089, 3276, 3090, 3273, 3)),
        ROOF_3(new Area(3094, 3267, 3089, 3265, 3)),
        ROOF_4(new Area(3088, 3261, 3088, 3257, 3)),
        ROOF_5(new Area(3088, 3255, 3094, 3255, 3)),
        ROOF_6(new Area(3096, 3256, 3100, 3261, 3));

        /**
         * Area variable
         */
        private Area area;

        /**
         * Roof area setter
         */
        private roofs(Area area) 
        {
            this.area = area;
        }

        /**
         * Roof area getter
         *
         * @return area
         */
        public Area getArea() 
        {
            return area;
        }

        /**
         * Tile variable
         */
        private Tile tile;

        /**
         * Roof start tile setter
         */
        private roofs(Tile tile) 
        {
            this.tile = tile;
        }

        /**
         * Roof start tile getter
         *
         * @return tile
         */
        public Tile getTile() 
        {
            return tile;
        }
    }
}
