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
package agility.pollnivneach;

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
        ROOF_1(new Area(3351, 2964, 3346, 2968, 1)),
        ROOF_2(new Area(3352, 2973, 3355, 2976, 1)),
        ROOF_3(new Area(3360, 2977, 3362, 2979, 1)),
        ROOF_4(new Area(3366, 2976, 3369, 2974, 1)),
        ROOF_5(new Area(3368, 2982, 3365, 2986, 1)),
        ROOF_6(new Area(3365, 2983, 3357, 2980, 2)),
        ROOF_7(new Area(3358, 2991, 3370, 2995, 2)),
        ROOF_8(new Area(3356, 3000, 3362, 3004, 2));

        /**
         * Area Variable
         */
        private final Area area;

        /**
         * Area Setter
         */
        private roofs(Area area) 
        {
            this.area = area;
        }

        /**
         * Area getter
         *
         * @return area
         */
        public Area getArea() 
        {
            return area;
        }
    }
}
