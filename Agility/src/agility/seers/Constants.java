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
package agility.seers;

import org.dreambot.api.methods.map.Area;

public class Constants 
{
    // Variables
    private static final int food = 361; // 385 = Shark, 379 = Lobster, 361 = Tuna

    /**
     * Food Getter
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
        ROOF_1(new Area(2730, 3497, 2722, 3490, 3)), // x1, y2, x2, y2, z
        ROOF_2(new Area(2713, 3493, 2705, 3496, 2)),
        ROOF_3(new Area(2716, 3482, 2710, 3477, 2)),
        ROOF_4(new Area(2714, 3472, 2700, 3469, 3)),
        ROOF_5(new Area(2703, 3466, 2698, 3460, 2));

        /**
         * Area variable
         */
        private final Area area;

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
    }
}
