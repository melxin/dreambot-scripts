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
package antiban.agility.falador;

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
        START_LOCATION(new Tile(3036, 3341, 0)),
        ROOF_1(new Area(3036, 3342, 3040, 3342, 3)), // x1, y2, x2, y2, z
        ROOF_2(new Area(3044, 3343, 3051, 3349, 3)),
        ROOF_3(new Area(3050, 3357, 3049, 3358, 3)),
        ROOF_4(new Area(3048, 3361, 3045, 3367, 3)),
        ROOF_5(new Area(3041, 3364, 3035, 3361, 3)),
        ROOF_6(new Area(3029, 3354, 3026, 3352, 3)),
        ROOF_7(new Area(3020, 3353, 3009, 3358, 3)),
        ROOF_8(new Area(3018, 3349, 3022, 3343, 3)),
        ROOF_9(new Area(3014, 3346, 3011, 3344, 3)),
        ROOF_10(new Area(3009, 3342, 3013, 3336, 3)),
        ROOF_11(new Area(3012, 3333, 3017, 3334, 3)),
        ROOF_12(new Area(3019, 3332, 3024, 3335, 3));

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
